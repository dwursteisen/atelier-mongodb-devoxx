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

import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.bson.types.ObjectId;
import org.jongo.MongoCollection;

import com.github.mongo.labs.model.Speaker;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

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
    /**
     * La requête à construire pour rechercher tous les speakers, par ordre de nom/prénom :
     * <pre>
     *     db.speakers.find({}).sort({ "name.lastName" : 1 , "name.firstName" : 1})
     * </pre>
     *
     * Le driver Java mongo propose lui aussi une méthode <code>sort</code>.
     */
    public String all() {
        DBObject sort = new BasicDBObject();
        return JSON.serialize(dbCollection.find().sort(sort));
    }

    @GET
    @Path("/{id}")
    @ApiOperation(value = "Retrouve un speaker par son identifiant (ex: '5325b07a84ae2fdd99aa3ddb')",
            notes = "le service retourne un code 404 si non trouvé"
    )
    /**
     * La requête à construire pour récuperer un speaker :
     *
     * <pre>
     *     db.speakers.find({_id: #})
     * </pre>
     */
    public String get(@PathParam("id") String id) {
        BasicDBObject query = new BasicDBObject();
        return JSON.serialize(dbCollection.findOne(query));
    }

    @GET
    @Path("/byname/{lastName}")
    @ApiOperation(value = "Retrouve une liste de speakers par leur nom (ex: 'Aresti')",
            notes = "le service retourne un code 404 si non trouvé"
    )
    /**
     * La requête à construire :
     *
     * <pre>
     *     db.speakers.find({ "name.lastName" : { "$regex" : "Ares"}})
     * </pre>
     *
     * Le Driver mongo peut directement utiliser une regex, et générera
     * <code>{ "$regex" : "Ares"}</code>.
     */
    public String getByName(@PathParam("lastName") String lastName) {
        Pattern regex = Pattern.compile(lastName, Pattern.CASE_INSENSITIVE);

        BasicDBObject query = new BasicDBObject();
        return JSON.serialize(dbCollection.find(query));
    }

    @PUT
    @Path("/{id}")
    @ApiOperation(value = "Mise à jour d'un speaker",
            notes = "Le service retourne un code 404 si non trouvé, 500 si une erreur a été rencontrée"
    )
    /**
     * La requête à construire via le driver Mongo :
     * <pre>
     *     db.speakers.update({_id: #}, {$set: {
     *                                      "name" : { "lastName" : "Grall", "firstName" : "Tugdual" },
     *                                      "bio" : "<p>Technical Evangelist</p>\n",
     *                                      "geo" : { "longitude" : 2.3521, "latitude" : 48.8091 }
     *                                      }
     *                                   });
     * </pre>
     *
     * Jongo s'affranchi très bien de ce genre de problème en prenant un POJO et
     * en générant le json correspondant.
     *
     * Pour utiliser jongo, utilisez simplement jongoCollection.
     *
     */
    public String update(@PathParam("id") String id, Speaker speaker) {
        // dbCollection.update()
        return id;
    }

    @POST
    @Path("/")
    @ApiOperation(value = "Ajout d'un speaker",
            notes = "Le service retourne un code 500 si une erreur a été rencontrée"
    )
    /**
     * La requête à constuire :
     * <pre>
     *     db.speakers.insert({
     *                             "name" : { "lastName" : "Grall", "firstName" : "Tugdual" },
     *                             "bio" : "<p>Technical Evangelist</p>\n",
     *                             "geo" : { "longitude" : 2.3521, "latitude" : 48.8091 }
     *                        })
     * </pre>
     *
     * Ici encore, Jongo peut faire la même chose.
     */
    public String add(Speaker speaker) {
        // dbCollection.insert()
        return speaker.getId();
    }

    @DELETE
    @Path("/{id}")
    @ApiOperation(value = "Suppression d'un speaker",
            notes = "Le service retourne un code 404 si non trouvé, 500 si une erreur a été rencontrée"
    )
    /**
     * Pour supprimer un speaker, il faut le supprimer de tous les endroits où il est référencé.
     * c-a-d la collection "talks" et "speakers"
     *
     * les requêtes à générer sont dans ce cas :
     *
     *
     * <pre>
     *     // supprime le speaker de tous les talks où il est mentionné
     *     db.talks.update({}, {$pull: {speakers: {ref: #}})
     * </pre>
     *
     * <pre>
     *     db.speakers.remove({_id: #})
     * </pre>
     *
     */
    public void delete(@PathParam("id") String id) {

        // Suppression des speakers dans les talks
        DBObject query = new BasicDBObject();
        // dbCollectionTalks.update(query, modifier)

        // suppression du speaker
        // dbCollection.remove(query);

    }



}
