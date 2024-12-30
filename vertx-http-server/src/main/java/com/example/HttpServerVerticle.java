package com.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

public class HttpServerVerticle extends AbstractVerticle {

  @Override
  public void start() {
    vertx.createHttpServer().requestHandler(req -> {
      long startTime = System.currentTimeMillis();

      // 요청 처리 로직
      req.response().end("Hello from Vert.x!");

      long endTime = System.currentTimeMillis();
      long duration = endTime - startTime;
      System.out.println("Request processed in " + duration + " ms");
    }).listen(8080);
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new HttpServerVerticle());
  }
}