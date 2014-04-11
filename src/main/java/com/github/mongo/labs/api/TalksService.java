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
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.util.JSON;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Api(value = "/talks", description = "Recherche de talks")
@Path("/talks")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class TalksService {

    @Named("mongo/talks")
    @Inject
    private DBCollection dbCollection;

    @GET
    @Path("/")
    @ApiOperation(value = "Retourne tous les talks")
    /**
     * La requête à constuire :
     *
     * <pre>
     *     db.talks.find({}, {title: 1, summary: 1, speakers: 1})
     * </pre>
     *
     * La requête retourne tous les résultats, mais on n'expose dans le résultat que les champs
     * title/summary/speakers/_id (le champ _id est ajouté par défaut)
     *
     * Il faut bien utiliser les projections
     */
    public String all() {

        BasicDBObject query = new BasicDBObject();

        BasicDBObject projection = new BasicDBObject();

        return JSON.serialize(dbCollection.find( query, projection));
    }


    @GET
    @Path("/{id}")
    @ApiOperation(
            value = "Retrouve un talk par son identifiant (ex: XWC-772)",
            notes = "le service retourne un code 404 si non trouvé",
            response = Talk.class)
    /**
     * La requête à constuire :
     *
     * <pre>
     *      db.talks.find({ "_id" : "AFY-069"})
     * </pre>
     */
    public String get(@PathParam("id") String id) {
        BasicDBObject query = new BasicDBObject();
        return JSON.serialize(dbCollection.findOne(query));
    }

}
