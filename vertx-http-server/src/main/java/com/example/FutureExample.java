package com.example;

import java.util.concurrent.*;

public class FutureExample {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Integer> future = executor.submit(() -> {
            Thread.sleep(2000);
            return 42;
        });

        System.out.println("Doing other work...");

        Integer result = future.get(); // 블로킹 호출
        System.out.println("Result: " + result);

        executor.shutdown();
    }
}