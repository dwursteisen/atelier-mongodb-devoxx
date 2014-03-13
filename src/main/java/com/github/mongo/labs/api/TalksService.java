package com.github.mongo.labs.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.Arrays;
import java.util.Collection;

import com.github.mongo.labs.model.Talk;

/**
 * Created with IntelliJ IDEA.
 * User: david.wursteisen
 * Date: 13/03/14
 * Time: 13:48
 * To change this template use File | Settings | File Templates.
 */
@Path("/talks")
public class TalksService {

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Talk> all() {
        return Arrays.asList(new Talk("Hello people !"));
    }

    @GET
    @Path("/hello")
    @Produces(MediaType.APPLICATION_JSON)
    public Talk get() {
        return new Talk("Hello world");
    }
}
