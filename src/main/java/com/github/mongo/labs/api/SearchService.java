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

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.util.JSON;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Api(value = "/search", description = "Recherche fulltext")
@Path("/search")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class SearchService {

    @Named("mongo/talks")
    @Inject
    DBCollection dbCollection;

    @GET
    @Path("/{term}")
    @ApiOperation(value = "Recherche les talks ayant le terme fourni en paramètre (ex: java). " +
            "Cette recherche utilise la fonction FullText Search de MongoDB. ",
            notes = "La création d'un index Full Text est necessaire : " +
                    "<b>db.talks.ensureIndex({ summary : \"text\", title : \"text\"  }, { default_language: \"french\" })</b>"
    )
    public String search(@PathParam("term") String term) {

        // Termes de la recheche
        // il est ici possible d'utiliser les differentes options fournies par le Search de Mongo:
        // - terme unique:   future
        // - plusieurs terme: future mobile
        // - negations : future mobile -bof
        BasicDBObject query = new BasicDBObject();
        BasicDBObject search = new BasicDBObject();
        search.put("$search", term);
        query.put("$text", search);

        // projection pour limiter les champs
        BasicDBObject projection = new BasicDBObject();
        projection.put("_id", 1);
        projection.put("title", 1);
        projection.put("summary", 1);
        projection.put("speakers", 1);

        return JSON.serialize(dbCollection.find(query, projection));
    }

}
