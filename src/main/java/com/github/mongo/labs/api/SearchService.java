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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Api(value = "/search", description = "Recherche fulltext")
@Path("/search")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class SearchService {

    private MongoCollection collection;

    @PostConstruct
    public void init() throws UnknownHostException {
        DB db = new MongoClient("localhost").getDB("devoxx");
        collection = new Jongo(db).getCollection("$cmd");
    }

    @GET
    @Path("/{term}")
    @ApiOperation(value = "Recherche les talks ayant le terme fourni en paramètre (ex: java). " +
            "Cette recherche utilise la fonction FullText Search de MongoDB. ",
            notes = "Il ne faut pas oublier d'activer cette fonctionnalité sur mongo : " +
                    "<b>mongod --setParameter textSearchEnabled=true</b>"
    )
    public Iterable<Talk> search(@PathParam("term") String term) {
        Output output = collection.findOne("{text: 'talks', search: #, limit: 5}", term).as(Output.class);
        List<Talk> result = new ArrayList<Talk>();

        if (output == null) {
            return result;
        }

        for (Result r : output.results) {
            result.add(r.obj);
        }
        return result;
    }


    public static class Output {
        private Iterable<Result> results;

    }

    public static class Result {
        public double score;
        public Talk obj;
    }
}
