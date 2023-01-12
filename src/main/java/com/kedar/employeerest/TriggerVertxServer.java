package com.kedar.employeerest;

import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;

/**
 * @author Kedar Erande
 */
public class TriggerVertxServer {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new EmployeeVerticle(), ar -> {
      if (ar.succeeded())
        System.out.println("Server is up and running");
      else
        System.out.println("Server is down and not failed");
    });

    WebClient webClient = WebClient.create(vertx);

    webClient.get(6000, "localhost", "/api/employees")
      .send()
      .onSuccess(res -> {
        System.out.println("res obtained" + res.bodyAsString());
      })
      .onFailure(err -> {
        System.out.println("error" + err.getMessage());
      });


    webClient.post(6000, "localhost", "/api/employee/addOne")
      .sendJson(new Employee("Raj", "00", "po"))
      .onSuccess(res -> {
        System.out.println("res obtained" + res.bodyAsString());
      })
      .onFailure(err -> {
        System.out.println("error" + err.getMessage());
      });
  }
}

