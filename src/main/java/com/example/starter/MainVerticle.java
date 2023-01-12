package com.example.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.file.FileProps;
import io.vertx.core.file.FileSystem;


public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
   /* vertx.setPeriodic(1000, id -> {
      //This handler will get called every second
      System.out.println("timer fired!");
    });*/

    //on futures
   /*FileSystem fs = vertx.fileSystem();
    Future<Void> fut = fs.createFile("/Applications/harshu.txt").compose(v -> {
      return fs.writeFile("hey", Buffer.buffer());
    }).compose(x -> {
      return fs.move("/Applications/harshu.txt", "/Applications/Kedar-Projects");
    });
*/

    //event bus  here new.sports is an handler
    EventBus eb = vertx.eventBus();
    eb.consumer("new.sports", message -> {
      System.out.println("I have received a message: " + message.body());
    });


    eb.publish("new.sports", "Hi vertx");

    vertx.createHttpServer().requestHandler(req -> {
      req.response().putHeader("Content-type", "text/plain")
        .end("end-word");
    }).listen(8888, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }
}
