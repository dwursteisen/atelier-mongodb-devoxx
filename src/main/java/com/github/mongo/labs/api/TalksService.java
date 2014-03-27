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

import com.github.mongo.labs.model.Talk;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.net.UnknownHostException;

@Api(value = "/talks", description = "Recherche de talks")
@Path("/talks")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class TalksService {

    private MongoCollection collection;

    @PostConstruct
    public void init() throws UnknownHostException {
        DB db = new MongoClient("localhost").getDB("devoxx");
        collection = new Jongo(db).getCollection("talks");
    }

    @GET
    @Path("/")
    @ApiOperation(value = "Retourne tous les talks")
    public Iterable<Talk> all() {
        return collection.find().as(Talk.class);
    }

    @GET
    @Path("/{id}")
    @ApiOperation(
            value = "Retrouve un talk par son identifiant (ex: XWC-772)",
            notes = "le service retourne un code 404 si non trouvé",
            response = Talk.class)
    public Talk get(@PathParam("id") String id) {
        Talk talk = collection.findOne("{_id: #}", id).as(Talk.class);
        if (talk == null) {
            throw new WebApplicationException(404);
        }
        return talk;
    }

}
