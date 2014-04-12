package com.github.mongo.labs.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;

public class TagsServiceTest extends AtelierTest{

	private TagsService service;

	@Before
	public void setUp() throws Exception {
		service = new TagsService();
		
		service.dbCollection = db.getCollection("talks");
	}

	@Test
	public void can_count_most_used_tags() {
		JSONArray tags = new JSONArray(service.countByTag());
		assertThat(tags.length()).isEqualTo(16);
		assertThat(tags.getJSONObject(0).toString()).isEqualTo("{\"tags\":\"java\",\"count\":50}");
	}
}
