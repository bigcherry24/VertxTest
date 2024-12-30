package com.example;

import java.util.concurrent.*;

public class CompletableFutureExample {
    public static void main(String[] args) {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return 42;
        });

        future.thenAccept(result -> {
            System.out.println("Result: " + result);
        });

        System.out.println("Doing other work...");

        // 블로킹 호출 (선택 사항)
        future.join();
    }
}