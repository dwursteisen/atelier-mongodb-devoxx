package com.github.mongo.labs.api;

import java.net.UnknownHostException;

import org.junit.BeforeClass;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class AtelierTest {

	static DB db;

	@BeforeClass
	public static void getDB() {

		try {
			db = new MongoClient("localhost").getDB("devoxx");
		} catch (UnknownHostException e) {
			System.out.println("La base de données devoxx n'existe pas. Mongo est up ? ");
		}
	}
}
