package com.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;

public class OrderClientVerticle extends AbstractVerticle {

  @Override
  public void start() {
    HttpClient client = vertx.createHttpClient();

    client.getNow(8080, "localhost", "/order?id=1", response -> {
      response.bodyHandler(body -> {
        System.out.println("Received response with status code " + response.statusCode());
        System.out.println("Response body: " + body.toString());
      });
    });
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new OrderClientVerticle());
  }
}