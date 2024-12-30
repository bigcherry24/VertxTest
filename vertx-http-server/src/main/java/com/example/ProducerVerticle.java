package com.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

public class ProducerVerticle extends AbstractVerticle {
  @Override
  public void start() {
    vertx.setPeriodic(1000, id -> {
        vertx.eventBus().send("message.address", "Hello from Producer!");
      });
  }

  public static void main(String[] args) {
    HazelcastClusterManager clusterManager = new HazelcastClusterManager();
    VertxOptions options = new VertxOptions().setClusterManager(clusterManager);

    Vertx.clusteredVertx(new VertxOptions(), res -> {
        if (res.succeeded()) {
          Vertx vertx = res.result();
          vertx.deployVerticle(new ProducerVerticle());
        } else {
          System.out.println("Failed to form cluster");
        }
      });
  }
}