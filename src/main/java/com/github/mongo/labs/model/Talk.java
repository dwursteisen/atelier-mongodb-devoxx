package com.github.mongo.labs.model;

import java.util.ArrayList;
import java.util.Collection;

import org.jongo.marshall.jackson.oid.Id;

/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
public class Talk {

    @Id
    private String id;
    private String title;
    private String summary;
    private String type;
    private Collection<String> tags = new ArrayList<String>();

    public Talk(final String title) {
        this.title = title;
    }

    public Talk() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(final String summary) {
        this.summary = summary;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public Collection<String> getTags() {
        return new ArrayList<String>(tags);
    }

    public void setTags(final Collection<String> tags) {
        this.tags = new ArrayList<String>(tags);
    }

    public String getId() {
        return id;
    }
}
