package com.github.mongo.labs.api;

import com.github.mongo.labs.model.Tag;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Api(value = "/tags", description = "Consultation de tags")
@Path("/tags")
@Produces(MediaType.APPLICATION_JSON)
public class TagsService {

    @GET
    @Path("/")
    @ApiOperation(value = "Retourne les tags les plus utilisés avec leurs statistiques associés",
            notes = "Le framework d'aggrégation doit être utilisé pour remonter les bonnes données"
    )
    public Iterable<Tag> all() {
        return null;
    }
}
