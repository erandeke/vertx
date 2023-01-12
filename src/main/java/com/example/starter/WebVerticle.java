package com.example.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

import java.net.http.HttpResponse;

/**
 * @author Kedar Erande
 */
public class WebVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    HttpServer server = vertx.createHttpServer();

    Router router = Router.router(vertx);

    //create handler and route will be there for getting all request . We are not specificing any matchers

   /* router.route().handler(ex -> {

      HttpServerResponse response = ex.response();
      response.putHeader("Content-Type", "text/plain");
      response.end("Hello word from vertx web");
    });*/

    server.requestHandler(router).listen(9090, http -> {
      if (http.succeeded()) {
        System.out.println("Success Web verticle started on 9090");
      } else {
        System.out.println("failed to start" + http.cause());
      }
    });


    //chainng of routes

    Route route = router.route("/path-1");

    route.handler(ctx -> {
      HttpServerResponse httpServerResponse = ctx.response();
      // enable chunked responses because we will be adding data as
      // we execute over other handlers. This is only required once and
      // only if several handlers do output.
      httpServerResponse.setChunked(true);

      httpServerResponse.write("In route 1");

      //calling nxt route after 5 seconds
      ctx.vertx().setTimer(5000, ti -> ctx.next());
    });

    route.handler(ctx -> {
      HttpServerResponse httpServerResponse = ctx.response();
      httpServerResponse.write("In route 2");

      //calling nxt route after 5 seconds
      ctx.vertx().setTimer(5000, ti -> ctx.next());
    });

    route.handler(ctx -> {
      HttpServerResponse httpServerResponse = ctx.response();
      httpServerResponse.write("In route 3");

      //calling nxt route after 5 seconds
      ctx.response().end();
    });


  }
}
