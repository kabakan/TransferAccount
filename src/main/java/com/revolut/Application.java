package com.revolut;

import com.revolut.exception.RevolutException;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.URI;

/**
* Revolut Transfer
* @date 14.12.2018
* @author Kanat K.B.
*/

public class Application {
    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static final String BASE_URI = "http://localhost:8587/";

    public static void main(String[] args) throws RevolutException, IOException {
        final HttpServer server = startHttpServer();
        LOG.info("RevolutTransferAccount start",BASE_URI);
        System.in.read();
        server.shutdownNow();
    }

    public static HttpServer startHttpServer() {
        final ResourceConfig resourceConfig = new ResourceConfig().packages("com.revolut.controller");
        resourceConfig.property(ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR, "true");
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), resourceConfig);
    }
}
