package com.vertx.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * @author Kedar Erande
 */
public class PublishSubscribeEventBus {

  private static final Logger LOG = LoggerFactory.getLogger(PublishSubscribeEventBus.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new PublisherVerticle());
    vertx.deployVerticle(new MessageSubscriber());
    vertx.deployVerticle(new TextSubscriber());

  }

  static class PublisherVerticle extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.setPeriodic(Duration.ofSeconds(10).toMillis(), id ->
        {
          vertx.eventBus().publish("server.publish", "Text Message....");
        }
      );

    }
  }

  static class MessageSubscriber extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(MessageSubscriber.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      vertx.eventBus().consumer("server.publish", message -> {
        LOG.debug("receiving message by MessageSubscriber {}", message.body());
      });
    }
  }

  static class TextSubscriber extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(TextSubscriber.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      vertx.eventBus().consumer("server.publish", message -> {
        LOG.debug("receiving message by TextSubscriber {}", message.body());
      });
    }
  }


}
