package com.vertx.eventbus;

import com.vertx.worker.blockingcode.WorkerVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kedar Erande
 */
public class ResquestResponse {

  private static final Logger LOG = LoggerFactory.getLogger(ResquestResponse.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new RequestVerticle());
    vertx.deployVerticle(new ResponseVerticle());
  }

  static class RequestVerticle extends AbstractVerticle {

    public static final String ADDRESS = "request.address";

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      EventBus eventBus = vertx.eventBus();
      String message = "Hey there!!";
      LOG.debug("Sending message {}", message);
      eventBus.request(ADDRESS, message, res -> {
        LOG.debug("Received Response {}", res.result().body());
      });

    }
  }

  static class ResponseVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().consumer(RequestVerticle.ADDRESS, message -> {
        LOG.debug("Received message {}", message.body());
        message.reply("Got ur message , Thanks!");
      });
    }
  }
}
