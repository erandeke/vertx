package com.vertx.scalingwebserver;

import com.vertx.web.WebVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kedar Erande
 */
public class MainVerticle extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle())
      .onFailure(err -> LOG.error("Failed to deploy verticle {}", err))
      .onSuccess(id -> LOG.debug("Succes in deploying the main verticle {}", id));

  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.deployVerticle(ScalingWebServer.class.getName(), new DeploymentOptions().setInstances(2))
      .onSuccess(id -> {
        LOG.info("Success in deploying the ScalingWebServerVerticle {}", id);
        startPromise.complete();
      }).onFailure(startPromise::fail);
  }


}
