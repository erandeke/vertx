package com.example.starter;

import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

/**
 * @author Kedar Erande
 */
public class Starter {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new ServerVerticle(), s -> {
      if (s.succeeded()) {
        System.out.println("Success deployed web verticle");
      } else {
        System.out.println("Not success");
      }
    });
    vertx.deployVerticle(new WebVerticle(), a -> {
      if (a.succeeded()) {
        System.out.println("Success deployed web verticle");
      } else {
        System.out.println("Not success");
      }
    });
    vertx.deployVerticle(new MainVerticle(), a -> {
      if (a.succeeded()) {
        System.out.println("Success deployed");
      } else {
        System.out.println("Not success");
      }
    });

    //WebClient

    WebClient client = WebClient.create(vertx);

    client.get(9090, "localhost", "/path-1")
      .send()
      .onSuccess(x -> {
        System.out.println("Received response" + x.bodyAsString());
      }).onFailure(err -> System.out.println("Errored out" + err.getCause()));


   //Post request
    client.post(7070, "localhost", "/catalogue/products/Kedar/123/")
      .send().onSuccess(x -> {
        System.out.println("Received response for post" + x.bodyAsString());
      }).onFailure(err -> System.out.println("Errored out" + err.getCause()));


  }
}
