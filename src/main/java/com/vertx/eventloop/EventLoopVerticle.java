package com.vertx.eventloop;

import io.vertx.core.*;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author Kedar Erande
 */


public class EventLoopVerticle extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(EventLoopVerticle.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx(new VertxOptions().setMaxEventLoopExecuteTime(500)
      .setMaxEventLoopExecuteTimeUnit(TimeUnit.MILLISECONDS)
      .setBlockedThreadCheckInterval(1)
      .setBlockedThreadCheckIntervalUnit(TimeUnit.MILLISECONDS)
      .setEventLoopPoolSize(2));

    vertx.deployVerticle(EventLoopVerticle.class.getName(), new DeploymentOptions().setInstances(4));
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOG.info("Class name" + getClass().getName());
    startPromise.complete();
    Thread.sleep(5000);
  }
}
