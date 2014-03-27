/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  <p/>
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  <p/>
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.github.mongo.labs;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import com.wordnik.swagger.config.ScannerFactory;
import com.wordnik.swagger.jaxrs.config.BeanConfig;
import com.wordnik.swagger.servlet.config.ServletScanner;
import com.wordnik.swagger.servlet.listing.ApiDeclarationServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;
import org.jongo.Jongo;

import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ServletHolder sh = new ServletHolder(ServletContainer.class);
        // Set the package where the services reside
        sh.setInitParameter(ServerProperties.PROVIDER_PACKAGES, "com.github.mongo.labs.api, com.github.mongo.labs.helper");
        sh.setInitOrder(1); // force loading at startup

        initSwagger();
        initMongoProfiling();

        ServletContextHandler context = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
        context.setResourceBase(new File(Main.class.getResource("/static").toURI()).getAbsolutePath());
        context.addServlet(sh, "/api/*");
        context.addServlet(ApiDeclarationServlet.class, "/api-docs/*");
        context.addServlet(DefaultServlet.class, "/*");
        server.start();
        server.join();
    }

    private static void initMongoProfiling() throws Exception {
        DB db = new MongoClient("localhost").getDB("devoxx");
        Jongo jongo = new Jongo(db);

        ResultCmd r = jongo.getCollection("$cmd")
                .withWriteConcern(WriteConcern.SAFE)
                .findOne("{profile: 1, slowms: 1}")
                .as(ResultCmd.class);// log event fast query


        System.err.println("profiling =" + r);
    }

    private static void initSwagger() {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.0");
        beanConfig.setBasePath("http://localhost:8080/api");

        ServletScanner scanner = new ServletScanner();
        scanner.setResourcePackage("com.github.mongo.labs.api");
        ScannerFactory.setScanner(scanner);
    }

    public static class ResultCmd {
        public String was;
        public int slowms;
        public int ok;

        @Override
        public String toString() {
            return "ResultCmd{" +
                    "was='" + was + '\'' +
                    ", slowms=" + slowms +
                    ", ok=" + ok +
                    '}';
        }
    }

}
