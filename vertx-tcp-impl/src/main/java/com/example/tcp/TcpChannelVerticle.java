package com.example.tcp;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpChannelVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(TcpChannelVerticle.class);
    private int port;
    private NetServer netServer;
    private NetSocket socket;
    private EventBus eventBus;

    public TcpChannelVerticle(int port) {
        this.port = port;
    }

    @Override
    public void start() throws Exception {
        startServerConnection();
        eventBus = vertx.eventBus();
        super.start();
    }

    @Override
    public void stop() {
        if (netServer != null) {
            netServer.close(res -> {
                LOGGER.info("Is server closed correctly={}, reason={}", res.succeeded(), res.cause());
            });
        }
    }

    private void startServerConnection() {
        netServer = vertx.createNetServer();
        netServer.connectHandler(handler -> {

        }).listen(port, res -> {
            if (res.succeeded()) {
                LOGGER.info("Server using PORT={} has been started", port);
            } else {
                LOGGER.error("Server using PORT={} has Failed to start, cause={}", port, res.cause());
            }

        });

    }
}