package com.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import java.util.concurrent.CompletableFuture;

public class CompletableFutureExampleVerticle extends AbstractVerticle {

  @Override
  public void start() {
    vertx.createHttpServer().requestHandler(req -> {
      // CompletableFuture를 사용하여 비동기 작업 처리
      CompletableFuture.supplyAsync(() -> {
        // 블로킹 작업 (예: 파일 읽기, 데이터베이스 쿼리 등)
        try {
          Thread.sleep(2000); // 예시로 2초 동안 블로킹
        } catch (InterruptedException e) {
          throw new IllegalStateException(e);
        }
        return "Blocking operation completed";
      }).thenAccept(result -> {
        // 비동기 작업 완료 후 결과 처리
        req.response().end(result);
      }).exceptionally(ex -> {
        req.response().setStatusCode(500).end(ex.getMessage());
        return null;
      });
    }).listen(8080);
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new CompletableFutureExampleVerticle());
  }
}