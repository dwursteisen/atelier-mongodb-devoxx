package com.github.mongo.labs.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.MongoException;


public class GeoServiceTest extends AtelierTest {

	GeoService service = new GeoService();
	
	@Before
	public void before(){
		service.speakers = db.getCollection("speakers");
	}	
	

	
	
	/**
	 * Testez sur mongo 
	 * db.speakers.find({"geo" : {$near: 
	 * 									{$maxDistance : 1500, 
	 * 									 $geometry : {"type": "Point", 
	 * 												  "coordinates" : [48.879099, 2.48169098]}}}})
	 * */
	@Test
	public void can_search_by_geo(){
		
		try{
			
			JSONArray speakers = new JSONArray(service.near(48.879099, 2.48169098));
			
			assertThat(speakers.length()).isEqualTo(21);
			assertThat(speakers.getJSONObject(0).toString()).contains("Arnaud", "Bétrémieux");
			
			
		}catch(MongoException ex){
			System.out.print(" Did you forget you need the Geo Index ? db.speakers.ensureIndex({geo: \"2dsphere\"}");
			System.out.println(ex);
			fail("Ooooops Regarde la sortie de la console");
		}
		
	}
}

