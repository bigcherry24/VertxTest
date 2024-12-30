package com.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.file.FileSystem;

public class FileReadVerticle extends AbstractVerticle {

  @Override
  public void start() {
    FileSystem fs = vertx.fileSystem();

    // 비동기 파일 읽기
    fs.readFile("largefile.txt", result -> {
      if (result.succeeded()) {
        // 파일 읽기 성공
        String fileContent = result.result().toString();
        System.out.println("File read successfully: " + fileContent);
      } else {
        // 파일 읽기 실패
        System.err.println("Failed to read file: " + result.cause());
      }
    });

    // 파일 읽기 요청 후에도 다른 작업을 수행할 수 있습니다.
    System.out.println("File read request sent, continuing with other tasks...");

    // 주기적으로 콘솔에 메시지를 출력하는 작업 추가
    vertx.setPeriodic(1000, id -> {
      System.out.println("Performing other tasks while waiting for file read to complete...");
    });
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new FileReadVerticle());
  }
}