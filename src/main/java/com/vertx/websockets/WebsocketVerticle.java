package com.vertx.websockets;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kedar Erande
 */
public class WebsocketVerticle extends AbstractVerticle {


  private static final Logger LOG = LoggerFactory.getLogger(Websockethandler.class);

  private static final Integer PORT = 4000;

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new WebsocketVerticle());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    vertx.createHttpServer().webSocketHandler(new Websockethandler(vertx))
      .listen(PORT, ws -> {
        if (ws.succeeded()) {
          startPromise.complete();
          LOG.info("Http server started on port {}", PORT);
        } else {
          startPromise.fail(ws.cause());
        }
      });
  }
}
