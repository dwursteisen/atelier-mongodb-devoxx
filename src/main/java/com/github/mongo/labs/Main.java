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

import com.github.mongo.labs.helper.MongoBinder;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
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
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger("fr.devoxx.mongodb");

    public static void main(String[] args) throws Exception {
        LOG.info("==========================================");
        LOG.info("Démarrage de l'application Mongodb/Devoxx");
        LOG.info("==========================================");

        initMongoProfiling();


        Server server = new Server(8080);

        ServletHolder sh = new ServletHolder(ServletContainer.class);
        // Set the package where the services reside
        sh.setInitParameter(ServerProperties.PROVIDER_PACKAGES, "com.github.mongo.labs.api, com.github.mongo.labs.helper");
        // @inject configuration
        sh.setInitParameter("javax.ws.rs.Application", MongoBinder.class.getName());
        sh.setInitOrder(1); // force loading at startup

        initSwagger();

        ServletContextHandler context = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
        context.setResourceBase(new File(Main.class.getResource("/static").toURI()).getAbsolutePath());
        context.addServlet(sh, "/api/*");
        context.addServlet(ApiDeclarationServlet.class, "/api-docs/*");
        context.addServlet(DefaultServlet.class, "/*");

        server.start();

        LOG.info("==========================================");
        LOG.info("L'application est disponible via :");
        LOG.info("==========================================");
        LOG.info(">\t\thttp://localhost:8080/ \t\t\t\t\t\t(l'application web)");
        LOG.info(">\t\thttp://localhost:8080/tutorial.html \t\t(le plan de l'atelier)");
        LOG.info(">\t\thttp://localhost:8080/swagger \t\t\t\t(pour tester l'api REST)");

        server.join();
    }

    public static void initMongoProfiling() {
        try {
            // bye bye Mongo driver logs
            Logger.getLogger("com.mongodb").setLevel(Level.OFF);

            DB db = new MongoClient("localhost").getDB("devoxx");
            Jongo jongo = new Jongo(db);

            ResultCmd r = jongo.getCollection("$cmd")
                    .withWriteConcern(WriteConcern.SAFE)
                    .findOne("{profile: 2}")
                    .as(ResultCmd.class);// log event fast query


            System.err.println("profiling =" + r);
        } catch (IOException e) {
            throwMongoNotStarted();
        } catch (MongoException e) {
            throwMongoNotStarted();
        }
    }

    private static void throwMongoNotStarted() {
        throw new RuntimeException("Oups ! mongodb n'est pas lancé ! " +
                "Vous devez démarrer mongodb d'abord : mongod --dbpath=<repertoire> --smallfiles");
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
