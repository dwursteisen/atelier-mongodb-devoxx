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
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.regex.Pattern;

@Api(value = "/speakers", description = "Gestion des speakers (recherche, m-a-j, etc)")
@Path("/speakers")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class SpeakersService {

	@Named("mongo/speakers")
	@Inject
	DBCollection dbCollection;

	@Named("mongo/talks")
	@Inject
	DBCollection dbCollectionTalks;

	@Named("jongo/speakers")
	@Inject
	MongoCollection jongoCollection;

	@GET
	@Path("/")
	@ApiOperation(value = "Retourne tous les speakers présent à Devoxx")
	public String all() {
		DBObject sort = new BasicDBObject();
		sort.put("name.lastName", 1);
		sort.put("name.firstName", 1);
		return JSON.serialize(dbCollection.find().sort(sort));
	}

	@GET
	@Path("/{id}")
	@ApiOperation(value = "Retrouve un speaker par son identifiant (ex: '5325b07a84ae2fdd99aa3ddb')", notes = "le service retourne un code 404 si non trouvé")
	public String get(@PathParam("id") String id) {
		ObjectId objId = new ObjectId(id);
		BasicDBObject query = new BasicDBObject("_id", objId);
		return JSON.serialize(dbCollection.findOne(query));
	}

	@GET
	@Path("/byname/{lastName}")
	@ApiOperation(value = "Retrouve une liste de speakers par leur nom (ex: 'Aresti')", notes = "le service retourne un code 404 si non trouvé")
	public String getByName(@PathParam("lastName") String lastName) {
		BasicDBObject query = new BasicDBObject();
		query.put("name.lastName", java.util.regex.Pattern.compile(lastName, Pattern.CASE_INSENSITIVE));
		return JSON.serialize(dbCollection.find(query));
	}

	@PUT
	@Path("/{id}")
	@ApiOperation(value = "Mise à jour d'un speaker", notes = "Le service retourne un code 404 si non trouvé, 500 si une erreur a été rencontrée")
	public String update(@PathParam("id") String id, Speaker speaker) {

		DBObject selector = new BasicDBObject("_id", new ObjectId(id));
		BasicDBObject nameModifier = new BasicDBObject("lastName", speaker.getName().lastName).append("firstName", speaker.getName().firstName);
		DBObject modifier = new BasicDBObject("$set", new BasicDBObject("name", nameModifier).append("bio", speaker.getBio()));

		dbCollection.update(selector, modifier);

		return id;
	}

	@POST
	@Path("/")
	@ApiOperation(value = "Ajout d'un speaker", notes = "Le service retourne un code 500 si une erreur a été rencontrée")
	public String add(Speaker speaker) {

		DBObject bsonObj = speaker.toBsonObject();
		dbCollection.insert(bsonObj);

		return bsonObj.get("_id").toString();
	}

	@DELETE
	@Path("/{id}")
	@ApiOperation(value = "Suppression d'un speaker", notes = "Le service retourne un code 404 si non trouvé, 500 si une erreur a été rencontrée")
	public void delete(@PathParam("id") String id) {

		// Suppression des speakers dans les talks
		DBObject query = new BasicDBObject();
		DBObject selector = new BasicDBObject("ref", id);
		DBObject speakers = new BasicDBObject("speakers", selector);
		DBObject operation = new BasicDBObject("$pull", speakers);
		dbCollectionTalks.update(query, operation, false, true);

		// suppression du speaker
		ObjectId objId = new ObjectId(id);
		BasicDBObject removeQuery = new BasicDBObject("_id", objId);
		dbCollection.remove(removeQuery);

	}

}
