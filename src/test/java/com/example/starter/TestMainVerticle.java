package com.example.starter;

import com.vertx.eventloop.EventLoopVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
public class TestMainVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(TestMainVerticle.class);

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void verticle_deployed(Vertx vertx, VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  void future_ex(Vertx vertx, VertxTestContext testContext) {
    final Promise<String> promise = Promise.promise();
    LOG.debug("Start");
    vertx.setTimer(100, ar -> {
      promise.complete("success");
      testContext.completeNow();
    });

    Future<String> fut = promise.future();
    fut.onComplete(ar -> {
      if (ar.succeeded()) {
        String res = ar.result();
        System.out.println(res); //read from future
      }
      testContext.completeNow();
    }).onFailure(testContext::failNow);

  }


  @Test
  void composite_future_test(Vertx vertx, VertxTestContext testContext) {
    var one = Promise.promise();
    var two = Promise.promise();
    var three = Promise.promise();

    Future fut_one = one.future();
    Future fut_two = two.future();
    Future fut_three = three.future();

    CompositeFuture.all(fut_one, fut_two, fut_three)
      .onFailure(testContext::failNow)
      .onSuccess(resukt -> {
        LOG.debug("Success");
        testContext.completeNow();
      });

    vertx.setTimer(500, id -> {
      one.complete();
      two.complete();
      three.complete();
    });


  }
}
