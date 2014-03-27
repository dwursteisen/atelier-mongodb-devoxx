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

package com.github.mongo.labs.helper;

import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.RawResultHandler;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.net.UnknownHostException;
import java.util.Iterator;

@Path("/query")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class MongoQueryService {

    private MongoCollection collection;

    @PostConstruct
    public void init() throws UnknownHostException {
        DB db = new MongoClient("localhost").getDB("devoxx");

        Jongo jongo = new Jongo(db);
        jongo.runCommand("{profile: 1, slowms: 1}"); // log event fast query

        collection = jongo.getCollection("system.profile");
    }

    @GET
    @Path("/lastQuery")
    public String lastQuery() {
        // catch only last query (and ignore update, insert, ...)
        Iterator<DBObject> iterable = collection.find("{op: 'query'}")
                .limit(1) // get first result
                .sort("{ts: -1}") // get last query
                .projection("{ns: 1, query: 1}") // get only query field and ns field (ns => collection)
                .map(new RawResultHandler<DBObject>()).iterator(); // map it as DBObject

        if (iterable.hasNext()) {
            DBObject dbObject = iterable.next();
            StringBuilder builder = new StringBuilder()
                    .append("db.").append(dbObject.get("ns")).append(".")
                    .append("find(").append(dbObject.get("query")).append(")");
            return builder.toString();
        } else {
            return "{}";
        }
    }

}
