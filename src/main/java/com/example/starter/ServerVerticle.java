package com.example.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

/**
 * @author Kedar Erande
 */
public class ServerVerticle extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    HttpServer httpServer = vertx.createHttpServer();

    Router router = Router.router(vertx);

    Route route = router.route(HttpMethod.POST, "/catalogue/products/:productType/:productID/");

    //create server listener

    httpServer.requestHandler(router).listen(7070, http -> {
      if (http.succeeded()) {
        System.out.println("Server for Web successfully spinned");
      } else {
        System.out.println("Failed to spin" + http.cause());
      }
    });

    route.handler(ctx -> {
      HttpServerResponse httpServerResponse = ctx.response();
      httpServerResponse.setChunked(true);

      String productType = ctx.pathParam("productType");
      String productId = ctx.pathParam("productID");
      httpServerResponse.write("I am sending the response back to client" + productType + " " + productId);

      //call next route

      ctx.vertx().setTimer(5000, ti -> ctx.next());
    });

    route.handler(ctx -> {
      HttpServerResponse httpServerResponse = ctx.response();

      httpServerResponse.write("Inside next chain");

      ctx.response().end("End of chaining");
    });


  }
}
