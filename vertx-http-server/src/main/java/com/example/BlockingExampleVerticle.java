package com.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

public class BlockingExampleVerticle extends AbstractVerticle {

  @Override
  public void start() {
    vertx.createHttpServer().requestHandler(req -> {
      // 워커 스레드 풀에서 블로킹 작업 실행
      vertx.executeBlocking(promise -> {
        // 블로킹 작업 (예: 파일 읽기, 데이터베이스 쿼리 등)
        try {
          Thread.sleep(2000); // 예시로 2초 동안 블로킹
          promise.complete("Blocking operation completed");
        } catch (InterruptedException e) {
          promise.fail(e);
        }
      }, res -> {
        // 블로킹 작업 완료 후 결과 처리
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
    vertx.deployVerticle(new BlockingExampleVerticle());
  }
}