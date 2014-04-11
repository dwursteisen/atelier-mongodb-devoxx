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

package com.github.mongo.labs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;
import org.jongo.marshall.jackson.oid.Id;

public class Speaker  {

    @Id
    private ObjectId _id;
    private Name name;
    private String bio;

    @JsonIgnore
    private Geo geo;


    public String getId() { return _id != null ? _id.toStringMongod() : null; }
    public void setId(String id) { _id = (id == null)? null : new ObjectId(id);  }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public String getBio() { return bio; }

    public void setBio(String bio) { this.bio = bio; }

    public void setGeo(Geo geo) { this.geo = geo; }

    public Geo getGeo() {
        return geo;
    }

    public static class Name {
        public String lastName;
        public String firstName;

        @Override
        public String toString() {
            return "Name{" +
                    "lastName='" + lastName + '\'' +
                    ", firstName='" + firstName + '\'' +
                    '}';
        }
    }

    public static class Geo {
        public double longitude;
        public double latitude;

        @Override
        public String toString() {
            return "Geo{" +
                    "longitude=" + longitude +
                    ", latitude=" + latitude +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Speaker{" +
                "_id=" + _id +
                ", name=" + name +
                ", bio='" + bio + '\'' +
                ", geo=" + geo +
                '}';
    }
}
