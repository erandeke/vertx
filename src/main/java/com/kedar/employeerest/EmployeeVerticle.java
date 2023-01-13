package com.kedar.employeerest;

import io.vertx.core.*;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kedar Erande
 */
public class EmployeeVerticle extends AbstractVerticle {

  Map<String, Employee> employeeHashMap = new HashMap<>();

  private void populateEmployeeData() {
    Employee e1 = new Employee("Kedar", "101", "IT");
    this.employeeHashMap.put(e1.getEmpId(), e1);
    Employee e2 = new Employee("Pran", "102", "CS");
    this.employeeHashMap.put(e2.getEmpId(), e2);
  }

  private void getAllEmployees(RoutingContext routingContext) {
    routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
      .end(Json.encodePrettily(employeeHashMap.values()));
  }

  private void addOneEmployee(RoutingContext routingContext) {
    Employee employee = Json.decodeValue(routingContext.getBodyAsString(), Employee.class);
    employeeHashMap.put(employee.getEmpId(), employee);
    //send the response
    routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
      .end(Json.encodePrettily(employee));
  }



  @Override
  public void start(Promise<Void> startPromise) throws Exception {


  }


  private void startWebApp(Handler<AsyncResult<HttpServer>> next) {
    populateEmployeeData();

    HttpServer httpServer = vertx.createHttpServer();
    Router router = Router.router(vertx);

    httpServer.requestHandler(router).listen(6000, ar -> {
      if (ar.succeeded())
        System.out.println("success");
      else
        System.out.println("failed" + ar.cause());
    });

    router.route("/firstroute").handler(ctx -> {
      HttpServerResponse httpServerResponse = ctx.response();
      httpServerResponse.setChunked(true);
      httpServerResponse.write("In Response");
    });

    router.get("/api/employees").handler(this::getAllEmployees);
    router.post("/api/employee*").handler(BodyHandler.create()); //highly required for reading any request body
    router.post("/api/employee/addOne").handler(this::addOneEmployee);
  }

  private void completeStartup(AsyncResult<HttpServer> http, Future<Void> fut) {
    if (http.succeeded()) {
      System.out.println(fut.succeeded());
    } else {
      System.out.println(fut.failed());
    }
  }

}
