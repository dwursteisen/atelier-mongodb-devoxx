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
