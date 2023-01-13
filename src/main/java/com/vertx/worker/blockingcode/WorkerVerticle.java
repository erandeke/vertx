package com.vertx.worker.blockingcode;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Kedar Erande
 */
public class WorkerVerticle extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(WorkerVerticle.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new WorkerVerticle());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOG.debug("start {}" + getClass().getName());
    startPromise.complete();
    vertx.executeBlocking(event -> {
      LOG.debug("Executing blocking code"); //executing in worker thread
      try {
        Thread.sleep(100);
        event.complete("calling executed");
      } catch (InterruptedException e) {
        e.printStackTrace();
        event.fail("Failed blocking call");
      }
    }, result -> { //executing in eventloop thread
      if (result.succeeded()) {
        LOG.debug("Blocking call done");
      } else {
        LOG.debug("Failed" + result.cause());
      }
    });
  }
}
