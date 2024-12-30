package com.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

public class OrderServiceVerticle extends AbstractVerticle {

  private SQLClient sqlClient;

  @Override
  public void start() {
    JsonObject config = new JsonObject()
      .put("url", "jdbc:mysql://localhost:3306/orders_db")
      .put("driver_class", "com.mysql.cj.jdbc.Driver")
      .put("user", "root")
      .put("password", "password");

    sqlClient = JDBCClient.createShared(vertx, config);

    vertx.createHttpServer().requestHandler(req -> {
      if (req.method().name().equals("GET") && req.path().equals("/order")) {
        String orderId = req.getParam("id");
        if (orderId == null) {
          req.response().setStatusCode(400).end("Order ID is required");
        } else {
          getOrderById(orderId, result -> {
            if (result.succeeded()) {
              req.response().putHeader("content-type", "application/json").end(result.result().encode());
            } else {
              req.response().setStatusCode(500).end(result.cause().getMessage());
            }
          });
        }
      } else {
        req.response().setStatusCode(404).end();
      }
    }).listen(8080);
  }

  private void getOrderById(String orderId, Handler<AsyncResult<JsonObject>> resultHandler) {
    sqlClient.getConnection(ar -> {
      if (ar.succeeded()) {
        SQLConnection connection = ar.result();
        connection.queryWithParams("SELECT * FROM orders WHERE id = ?", new JsonArray().add(orderId), res -> {
          if (res.succeeded()) {
            if (res.result().getNumRows() > 0) {
              resultHandler.handle(Future.succeededFuture(res.result().getRows().get(0)));
            } else {
              resultHandler.handle(Future.failedFuture("Order not found"));
            }
          } else {
            resultHandler.handle(Future.failedFuture(res.cause()));
          }
          connection.close();
        });
      } else {
        resultHandler.handle(Future.failedFuture(ar.cause()));
      }
    });
  }
  
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new OrderServiceVerticle());
  }
}