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
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.jongo.MongoCollection;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Api(value = "/geo", description = "Recherche géolocalisé")
@Path("/geo")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class GeoService {

    @Named("jongo/speakers")
    @Inject
    private MongoCollection collection;

    @Named("mongo/speakers")
    @Inject
    private DBCollection speakers;

    @GET
    @Path("/{longitude}/{latitude}")
    @ApiOperation(
            value = "Retrouve les speakers proche du point [longitude, latitude] (ex: 48.8670, 2.3521)",
            notes = "Un <b>index géolocalisé</b> doit être présent sur la collection des speakers"
    )
    public Iterable<Speaker> near(@PathParam("longitude") double longitude, @PathParam("latitude") double latitude) {
        return collection.find("{geo: {$near: " +
                "{$geometry: {type:\"Point\", coordinates: [#, #]}, " +
                "$maxDistance: 1500}}}", longitude, latitude).as(Speaker.class);
    }

    @GET
    @Path("/")
    @ApiOperation(
            value = "Retourne tous les speakers avec leurs données de géolocalisation, nom et id",
            notes = "C'est très proche du service /speakers/ : les données sont les mêmes, mais la structure est différente."
    )
    public String all() {
        DBObject projection = new BasicDBObject();
        projection.put("name", 1);
        projection.put("geo", 1);
        return JSON.serialize(speakers.find(new BasicDBObject(), projection));
    }
}
