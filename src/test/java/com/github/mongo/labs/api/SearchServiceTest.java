package com.github.mongo.labs.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.MongoException;

public class SearchServiceTest extends AtelierTest{

	private SearchService service;

	@Before
	public void setUp() throws Exception {
		service = new SearchService();

		service.dbCollection = db.getCollection("talks");

	}
	
	@Test
	public void can_search_full_text() {

		try{
			String searchJSON = service.search("future");
			JSONArray searchResult = new JSONArray(searchJSON);
			
			assertThat(searchResult.length()).isEqualTo(14);
			assertThat(searchJSON).containsIgnoringCase("Java 8 Lambdas in the Stream");
			
		}catch(MongoException ex){
			
			System.out.print(" Did you forget you need the Geo Index ? db.talks.ensureIndex({ summary : \"text\", title : \"text\"  }, { default_language: \"french\" })");
			System.out.println(ex);
			fail("Ooooops Regarde la sortie de la console");
			
		}
		
	}
}
