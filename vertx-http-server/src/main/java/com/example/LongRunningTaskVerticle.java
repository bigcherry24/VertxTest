package com.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

public class LongRunningTaskVerticle extends AbstractVerticle {

  @Override
  public void start() {
    vertx.createHttpServer().requestHandler(req -> {
      // 워커 스레드 풀을 사용하여 블로킹 작업 처리
      vertx.executeBlocking(promise -> {
        // 시간이 오래 걸리는 작업 (예: 파일 읽기, 데이터베이스 쿼리 등)
        try {
          Thread.sleep(5000); // 예시로 5초 동안 블로킹
          promise.complete("Long running task completed");
        } catch (InterruptedException e) {
          promise.fail(e);
        }
      }, res -> {
        if (res.succeeded()) {
          req.response().end(res.result().toString());
        } else {
          req.response().setStatusCode(500).end(res.cause().getMessage());
        }
      });
    }).listen(8080);
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new LongRunningTaskVerticle());
  }
}