package com.github.mongo.labs.helper;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

import java.net.UnknownHostException;

public class MongoBinder extends ResourceConfig {

    public MongoBinder() {
        super();
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                try {
                    DB db = new MongoClient("localhost").getDB("devoxx");

                    bind(db.getCollection("speakers")).to(DBCollection.class).named("mongo/speakers");
                    bind(db.getCollection("talks")).to(DBCollection.class).named("mongo/talks");

                    bind(new Jongo(db).getCollection("speakers")).to(MongoCollection.class).named("jongo/speakers");
                    bind(new Jongo(db).getCollection("talks")).to(MongoCollection.class).named("jongo/talks");

                } catch (UnknownHostException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
