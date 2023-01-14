package com.vertx.web;

import com.vertx.eventbus.PublishSubscribeEventBus;
import com.vertx.eventloop.EventLoopVerticle;
import com.vertx.web.model.Asset;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kedar Erande
 */
public class WebVerticle extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(WebVerticle.class);

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new WebVerticle(), res -> {
      if (res.succeeded()) {
        LOG.info("Res succeeded");
      } else {
        LOG.error("Res succeeded {}", res.cause());
      }
    });
  }


  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    Router router = Router.router(vertx);
    router.get("/assets").handler(ctx -> {
      final JsonArray response = new JsonArray();
      response.add(new JsonObject().put("symbol", "AAPL"));
      response.add(new JsonObject().put("symbol", "TSLA"));
      response.add(new JsonObject().put("symbol", "TCS"));
      response.add(new JsonObject().put("symbol", "ICI"));
      LOG.info("Path {} responds with {}", ctx.normalizedPath(), response);
      ctx.response().end(response.toBuffer());
    });

    router.get("/assets/:productId").handler(cts -> {

      var productId = cts.pathParam("productId");
      var asset = initRandomAssets(productId);
      JsonObject response = asset.toJsonObject();
      LOG.debug("PATH {} responds with {}", cts.normalizedPath(), asset);
      cts.response().end(response.toBuffer());
    });

    vertx.createHttpServer().requestHandler(router).exceptionHandler(error -> {
      LOG.error("HTTP server error ", error);
    }).listen(8888, http -> {
      if (http.succeeded()) {
        startPromise.complete();
      } else {
        startPromise.fail("Failed");
      }
    });
  }

  private Asset initRandomAssets(String productId) {
    return new Asset(productId, "productName", 100.09);
  }


}
