package com.github.mongo.labs.api;

import com.github.mongo.labs.model.Talk;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Api(value = "/search", description = "Recherche fulltext")
@Path("/search")
@Produces(MediaType.APPLICATION_JSON)
public class SearchService {

    @GET
    @Path("/{term}")
    @ApiOperation(value = "Recherche les talks ayant le terme fourni en paramètre. " +
            "Cette recherche utilise la fonction FullText Search de MongoDB. ",
            notes = "Il ne faut pas oublier d'activer cette fonctionnalité sur mongo : " +
                    "<b>mongod --setParameter textSearchEnabled=true</b>"
    )
    public Iterable<Talk> search(@PathParam("term") String term) {
        return null;
    }
}
