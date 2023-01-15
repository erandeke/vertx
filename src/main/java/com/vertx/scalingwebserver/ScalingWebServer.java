package com.vertx.scalingwebserver;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Kedar Erande
 */
public class ScalingWebServer extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(ScalingWebServer.class);
  private static final int PORT = 8000;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    createHttpServerForHandlingRequestAndAttachRoutes(startPromise);
  }


  private void createHttpServerForHandlingRequestAndAttachRoutes(Promise<Void> startPromise) {

    Router router = Router.router(vertx);

    router.get("/assets").handler(ctx -> {
      final JsonArray response = new JsonArray();
      response.add(new JsonObject().put("symbol", "AAPL"));
      response.add(new JsonObject().put("symbol", "TSLA"));
      response.add(new JsonObject().put("symbol", "TCS"));
      response.add(new JsonObject().put("symbol", "ICI"));
      LOG.info("Path {} responds with {}", ctx.normalizedPath(), response);
      try {
        Thread.sleep(ThreadLocalRandom.current().nextInt(100,300));
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      ctx.response().end(response.toBuffer());
    });

    vertx.createHttpServer().requestHandler(router).exceptionHandler(err -> {
      LOG.debug("Error ocurred {}", err);

    }).listen(PORT, http -> {
      if (http.succeeded()) {
        LOG.debug("HTTP server bootstrapped {}", http.result());
        startPromise.complete();
      } else {
        LOG.error("Failed to make server up {}", http.cause());
        startPromise.fail("failed");
      }
    });
  }
}
