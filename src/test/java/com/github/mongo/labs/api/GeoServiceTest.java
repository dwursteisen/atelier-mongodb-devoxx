package com.github.mongo.labs.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import com.github.mongo.labs.api.com.github.mongo.labs.util.ExpectedQuery;
import com.github.mongo.labs.api.com.github.mongo.labs.util.QueryCheckRule;
import org.json.JSONArray;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mongodb.MongoException;


public class GeoServiceTest extends AtelierTest {

    @Rule
    public final QueryCheckRule rule = new QueryCheckRule();

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
	 * 												  "coordinates" : [2.48169098, 48.879099]}}}})
	 * */
    @ExpectedQuery(query = "db.speakers.find({\"geo\" : {\"$near\": {\"$maxDistance\" : 1500, \"$geometry\" : {\"type\": \"Point\", \"coordinates\" : [2.48169098, 48.879099]}}}})")
 	@Test
	public void can_search_by_geo(){
		
		try{
			
			JSONArray speakers = new JSONArray(service.near(2.48169098, 48.879099));
			
			assertThat(speakers.length()).isEqualTo(4);
			assertThat(speakers.getJSONObject(0).toString()).contains("Jean-Baptiste", "Defard");
			
			
		}catch(MongoException ex){
			System.out.print(" Did you forget you need the Geo Index ? db.speakers.ensureIndex({geo: \"2dsphere\"}");
			System.out.println(ex);
			fail("Ooooops Regarde la sortie de la console");
		}
		
	}
}

