package com.github.mongo.labs.api;

import com.github.mongo.labs.model.Speaker;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


@Api(value = "/speakers", description = "Gestion des speakers (recherche, m-a-j, etc)")
@Path("/speakers")
@Produces(MediaType.APPLICATION_JSON)
public class SpeakersService {

    @GET
    @Path("/")
    @ApiOperation(value = "Retourne tous les speakers présent à Devoxx")
    public Iterable<Speaker> all() {
        return null;
    }

    @GET
    @Path("/{id}")
    @ApiOperation(value = "Retrouve un speaker par son identifiant (ex: '5325b07a84ae2fdd99aa3ddb')",
            notes = "le service retourne un code 404 si non trouvé"
    )
    public Speaker get(@PathParam("id") String id) {
        return null;
    }

    @PUT
    @Path("/{id}")
    @ApiOperation(value = "Mise à jour d'un speaker",
            notes = "Le service retourne un code 404 si non trouvé, 500 si une erreur a été rencontré"
    )
    public void update(@PathParam("id") String id, Speaker speaker) {

    }

    @POST
    @Path("/")
    @ApiOperation(value = "Ajout d'un speaker",
            notes = "Le service retourne un code 500 si une erreur a été rencontré"
    )
    public void add(Speaker speaker) {

    }
}
