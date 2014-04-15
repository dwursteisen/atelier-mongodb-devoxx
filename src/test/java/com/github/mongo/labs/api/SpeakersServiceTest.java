package com.github.mongo.labs.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.mongo.labs.model.Speaker;
import com.github.mongo.labs.model.Speaker.Name;

public class SpeakersServiceTest extends AtelierTest {

	private SpeakersService service;


    @BeforeClass
    public static void backupSpeakers(){
        Iterable<Speaker> allSpeakers = jongo.getCollection("speakers").find().as(Speaker.class);
        MongoCollection speakersBackup = jongo.getCollection("speakersBackup");
        speakersBackup.remove();
        while(allSpeakers.iterator().hasNext())  speakersBackup.save(allSpeakers.iterator().next());

    }

	@Before
	public void setUp() throws Exception {
		service = new SpeakersService();
		service.dbCollection = db.getCollection("speakers");
		service.dbCollectionTalks = db.getCollection("talks");
        service.jongoCollection = jongo.getCollection("speakers");
    }

    @After
    public void tearDown() throws Exception {
        Iterable<Speaker> speakersBackup =  jongo.getCollection("speakersBackup").find().as(Speaker.class);
        MongoCollection allSpeakers = jongo.getCollection("speakers");
        allSpeakers.remove();
        while(speakersBackup.iterator().hasNext())  allSpeakers.save(speakersBackup.iterator().next());
    }

	@Test
	public void find_all_speakers_sorted_by_lastName_and_name() {

		JSONArray speakers = new JSONArray(service.all());

		assertThat(speakers.length()).isGreaterThanOrEqualTo(208);
		assertThat(getFirstSpeakerName(speakers).optString("lastName")).isEqualToIgnoringCase("AMMAR");
		assertThat(getFirstSpeakerName(speakers).optString("firstName")).isEqualToIgnoringCase("Sofiane");
	}

	@Test
	public void find_speaker_by_id() {
		JSONObject speaker = new JSONObject(service.get("534444a344ae6328612a69fb"));

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

    private static final JSONObject getFirstSpeakerName(JSONArray speakers) {
		return speakers.getJSONObject(0).getJSONObject("name");
	}
}
