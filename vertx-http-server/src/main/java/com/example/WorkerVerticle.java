package com.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class WorkerVerticle extends AbstractVerticle {

  @Override
  public void start() {
    vertx.eventBus().consumer("blocking.operation", message -> {
      // 블로킹 작업 (예: 파일 읽기, 데이터베이스 쿼리 등)
      try {
        Thread.sleep(2000); // 예시로 2초 동안 블로킹
        message.reply("Blocking operation completed");
      } catch (InterruptedException e) {
        message.fail(1, e.getMessage());
      }
    });
  }

  public static void main(String[] args) {
    VertxOptions options = new VertxOptions().setWorkerPoolSize(10);
    Vertx vertx = Vertx.vertx(options);
    vertx.deployVerticle(new WorkerVerticle(), res -> {
      if (res.succeeded()) {
        System.out.println("Worker verticle deployed");
      } else {
        System.out.println("Failed to deploy worker verticle: " + res.cause());
      }
    });

    vertx.createHttpServer().requestHandler(req -> {
      vertx.eventBus().request("blocking.operation", "", reply -> {
        if (reply.succeeded()) {
          req.response().end(reply.result().body().toString());
        } else {
          req.response().setStatusCode(500).end(reply.cause().getMessage());
        }
      });
    }).listen(8080);
  }
}