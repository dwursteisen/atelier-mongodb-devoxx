package com.github.mongo.labs.api;

import java.net.UnknownHostException;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jongo.Jongo;
import org.jongo.MongoCollection;

import com.github.mongo.labs.model.Talk;
import com.mongodb.DB;
import com.mongodb.MongoClient;

/**
 * Created with IntelliJ IDEA. User: david.wursteisen Date: 13/03/14 Time: 13:48 To change this template use File | Settings | File
 * Templates.
 */
@Path("/talks")
public class TalksService {

    private MongoCollection collection;

    @PostConstruct
    public void init() throws UnknownHostException {
        DB db = new MongoClient("localhost").getDB("devoxx");
        collection = new Jongo(db).getCollection("talks");
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Iterable<Talk> all() {
        return collection.find().as(Talk.class);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") String id) {
        Talk talk = collection.findOne("{_id: #}", id).as(Talk.class);
        if (talk == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(talk).build();
    }

}
