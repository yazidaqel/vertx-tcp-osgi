package com.example.tcp;

import io.vertx.core.Vertx;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class VertxActivator implements BundleActivator {

    private static final Logger LOGGER = LoggerFactory.getLogger("BaseVertxActivator");
    private ServiceRegistration<Vertx> vertxRegistration;

    @Override
    public void start(BundleContext context) throws Exception {
        LOGGER.info("Creating Vert.x instance");

        Vertx vertx = executeWithTCCLSwitch(Vertx::vertx);

        vertxRegistration = context.registerService(Vertx.class, vertx, null);
        LOGGER.info("Vert.x service registered");
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        if (vertxRegistration != null) {
            vertxRegistration.unregister();
            vertxRegistration = null;
        }
    }

    public <T> T executeWithTCCLSwitch(Callable<T> action) throws Exception {
        final ClassLoader original = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(VertxActivator.class.getClassLoader());
            return action.call();
        } finally {
            Thread.currentThread().setContextClassLoader(original);
        }
    }
}
