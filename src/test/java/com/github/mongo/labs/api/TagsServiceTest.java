package com.github.mongo.labs.api;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TagsServiceTest {


    private TagsService service;

    @Before
    public void setUp() throws Exception {
        service = new TagsService();
        service.init();
    }

    @Test
    public void should_return_something() {
        assertThat(service.all()).isNotEmpty();
    }
}
