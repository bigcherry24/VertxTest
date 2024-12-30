package com.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

public class ConsumerVerticle extends AbstractVerticle {
  @Override
  public void start() {
    vertx.eventBus().consumer("message.address", message -> {
      System.out.println("Received message: " + message.body());
    });
  }

  public static void main(String[] args) {
    HazelcastClusterManager clusterManager = new HazelcastClusterManager();
    VertxOptions options = new VertxOptions().setClusterManager(clusterManager);

    Vertx.clusteredVertx(options, res -> {
      if (res.succeeded()) {
        Vertx vertx = res.result();
        vertx.deployVerticle(new ConsumerVerticle());
      } else {
        System.out.println("Failed to form cluster");
      }
    });
  }
}