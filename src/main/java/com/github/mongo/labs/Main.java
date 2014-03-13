package com.github.mongo.labs;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;


/**
 * Created with IntelliJ IDEA. User: david.wursteisen Date: 13/03/14 Time: 13:39 To change this template use File | Settings | File
 * Templates.
 */
public class Main {

    public static void main(String[] args) throws Exception {

        ServletHolder sh = new ServletHolder(ServletContainer.class);
        sh.setInitParameter(ServerProperties.PROVIDER_PACKAGES, "com.github.mongo.labs.api");// Set the package where the services reside

        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
        context.addServlet(sh, "/*");
        server.start();
        server.join();
    }

}
