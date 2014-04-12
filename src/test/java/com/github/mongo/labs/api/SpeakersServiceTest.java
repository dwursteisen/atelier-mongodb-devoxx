package com.github.mongo.labs.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.UnknownHostException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.mongo.labs.model.Speaker;
import com.github.mongo.labs.model.Speaker.Name;
import com.mongodb.DB;
import com.mongodb.MongoClient;

public class SpeakersServiceTest {

	private SpeakersService service;
	private static DB db;

	@BeforeClass
	public static void getDB() {

		try {
			db = new MongoClient("localhost").getDB("devoxx");
		} catch (UnknownHostException e) {
			System.out.println("La base de données devoxx n'existe pas. Mongo est up ? ");
		}
	}

	@Before
	public void setUp() throws Exception {
		service = new SpeakersService();

		service.dbCollection = db.getCollection("speakers");

		service.dbCollectionTalks = db.getCollection("talks");

	}

	@After
	public void tearDown() throws Exception {

		service.dbCollection = db.getCollection("speakers");
		service.dbCollectionTalks = db.getCollection("talks");

	}

	@Test
	public void find_all_speakers_sorted_by_lastName_and_name() {

		JSONArray speakers = new JSONArray(service.all());

		assertThat(speakers.length()).isGreaterThanOrEqualTo(195);
		assertThat(getFirstSpeakerName(speakers).optString("lastName")).isEqualToIgnoringCase("ANCELIN");
		assertThat(getFirstSpeakerName(speakers).optString("firstName")).isEqualToIgnoringCase("Mathieu");
	}

	@Test
	public void find_speaker_by_id() {
		JSONObject speaker = new JSONObject(service.get("5325b07a84ae2fdd99aa3df6"));

		assertThat(speaker.getJSONObject("name").optString("lastName")).isEqualTo("Aresti");
	}

	@Test
	public void find_speaker_by_lastName_case_insensitive() {
		JSONArray speakers = new JSONArray(service.getByName("aresti"));

		assertThat(getFirstSpeakerName(speakers).optString("lastName")).isEqualTo("Aresti");
	}

	@Test
	public void crud_speaker() {

		Speaker speakerToInsert = new Speaker();
		speakerToInsert.setName(new Name("Foo", "Bar"));

		// Add speaker
		String id = service.add(speakerToInsert);
		assertThat(id).isNotEmpty();

		// Read created speaker
		Speaker speakerToRead = new Speaker(service.get(id));
		assertThat(speakerToRead.getName().lastName).isEqualTo("Foo");
		assertThat(speakerToRead.getName().firstName).isEqualTo("Bar");
		assertThat(speakerToRead.getBio()).isEmpty();

		// Update speaker
		Speaker speakerToupdate = new Speaker();
		speakerToupdate.setName(new Name("Dupon", "Albert"));
		speakerToupdate.setBio("Je suis génial");
		service.update(id, speakerToupdate);

		speakerToRead = new Speaker(service.get(id));
		assertThat(speakerToRead.getName().lastName).isEqualTo("Dupon");
		assertThat(speakerToRead.getName().firstName).isEqualTo("Albert");
		assertThat(speakerToRead.getBio()).isEqualTo("Je suis génial");

		// Delete speaker
		service.delete(id);
	}

	@Test
	public void can_delete_speaker_and_his_talks() {
		// TODO
	}

	private static final JSONObject getFirstSpeakerName(JSONArray speakers) {
		return speakers.getJSONObject(0).getJSONObject("name");
	}
}
