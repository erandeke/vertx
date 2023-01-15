package com.vertx.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kedar Erande
 * This is point to point message where no ack will given by destination verticle unlike request
 * response event bus
 *
 */
public class PointToPointEventBus {

  private static final Logger LOG = LoggerFactory.getLogger(PointToPointEventBus.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new Sender());
    vertx.deployVerticle(new Receiver());
  }

  static class Sender extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      LOG.debug("Message from sender");
      vertx.eventBus().send(Sender.class.getName() , "Sending messge");

    }
  }

  static class Receiver extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().consumer(Sender.class.getName(),message ->{
        LOG.debug("message received {}" ,message.body());
      });
    }
  }

}
