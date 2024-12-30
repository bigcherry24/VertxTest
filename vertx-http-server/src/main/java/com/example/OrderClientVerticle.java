package com.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpMethod;

public class OrderClientVerticle extends AbstractVerticle {

  @Override
  public void start() {
    HttpClient client = vertx.createHttpClient();

    // HTTP GET 요청 생성 및 전송
    client.request(HttpMethod.GET, 8080, "localhost", "/order?id=1")
      .onSuccess(request -> {
        request.response().onSuccess(response -> {
          response.bodyHandler(body -> {
            System.out.println("Received response with status code " + response.statusCode());
            System.out.println("Response body: " + body.toString());
          });
        });
        request.end();
      })
      .onFailure(err -> {
        System.err.println("Failed to send request: " + err.getMessage());
      });
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new OrderClientVerticle());
  }
}