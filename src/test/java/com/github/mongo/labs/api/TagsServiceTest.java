package com.github.mongo.labs.api;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.jongo.Jongo;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TagsServiceTest {


    private TagsService service;

    @Before
    public void setUp() throws Exception {
        service = new TagsService();

        DB db = new MongoClient("localhost").getDB("devoxx");
        service.setCollection(new Jongo(db).getCollection("talks"));
    }

    @Test
    public void should_return_something() {
        assertThat(service.all()).isNotEmpty();
    }
}
