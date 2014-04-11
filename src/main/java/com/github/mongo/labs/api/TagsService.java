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

import com.github.mongo.labs.model.Tag;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.assertj.core.util.VisibleForTesting;
import org.jongo.MongoCollection;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Api(value = "/tags", description = "Consultation de tags")
@Path("/tags")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class TagsService {

    @Named("jongo/talks")
    @Inject
    private MongoCollection collection;


    @Named("mongo/talks")
    @Inject
    private DBCollection dbCollection;


    @GET
    @Path("/")
    @ApiOperation(value = "Retourne les tags les plus utilisés avec leurs statistiques associées. Ce endpoint utilise Jongo",
            notes = "Le framework d'aggrégation doit être utilisé pour remonter les bonnes données"
    )
    public Iterable<Tag> all() {
        return collection.aggregate("{$project: {tags: 1}}")
                .and("{$unwind: '$tags'}")
                .and("{$project: {tags: {$toLower: '$tags'}}}")
                .and("{$group: {_id: '$tags', count: {$sum:  1}}}")
                .and("{ '$project' : { '_id' : 0, 'tag' : '$_id' , 'count' : 1 } }")
                .and("{$sort: {count: -1}}")
                .as(Tag.class);
    }


    @GET
    @Path("/native/")
    @ApiOperation(value = "Retourne les tags les plus utilisés avec leurs statistiques associées. Ce endpoint utilise le driver en native",
            notes = "Le framework d'aggrégation doit être utilisé pour remonter les bonnes données"
    )
    public String countByTag() {

        DBObject project0 = new BasicDBObject("$project", new BasicDBObject("tags", 1));

        DBObject unwind = new BasicDBObject("$unwind", "$tags");
        DBObject project1 = new BasicDBObject("$project", new BasicDBObject("tags", new BasicDBObject("$toLower", "$tags")  ));

        DBObject groupFields = new BasicDBObject();
        groupFields.put("_id", "$tags");
        groupFields.put("count", new BasicDBObject("$sum", 1));
        DBObject group = new BasicDBObject("$group", groupFields);

        DBObject projectFields = new BasicDBObject();
        projectFields.put("_id", 0);
        projectFields.put("tags", "$_id");
        projectFields.put("count", 1);

        DBObject project2 = new BasicDBObject("$project", projectFields);

        DBObject sort = new BasicDBObject("$sort", new BasicDBObject("count", -1));

        return JSON.serialize(dbCollection.aggregate(unwind, project1, group, project2, sort).results());
    }


    @VisibleForTesting
    public void setCollection(MongoCollection collection) {
        this.collection = collection;
    }
}
