package com.example.tcp;

import com.example.tcp.api.ITcpChannelManager;
import io.vertx.core.Vertx;
import org.osgi.service.component.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component(immediate = true, scope = ServiceScope.SINGLETON)
public class TcpChannelManager implements ITcpChannelManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(TcpChannelManager.class);

    @Reference
    private Vertx vertx;

    private List<String> deployedChannels = new CopyOnWriteArrayList<>();

    @Override
    public void create(int port) {

        TcpChannelVerticle tcpChannelVerticle = new TcpChannelVerticle(port);
        vertx.deployVerticle(tcpChannelVerticle, handler -> {
            if (handler.succeeded()) {
                String deploymentId = handler.result();
                LOGGER.info("Channel has successfully deployed={}", deploymentId);
                deployedChannels.add(deploymentId);
            }
        });

    }

    @Activate
    public void activate(){
        LOGGER.info("Service Activated");
    }

    @Deactivate
    public void deactivate(){
        LOGGER.info("Service is deactivated");
        for(String deploymentId : deployedChannels){
            vertx.undeploy(deploymentId);
        }
    }

}
