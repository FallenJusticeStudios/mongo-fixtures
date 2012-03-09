package com.fallenjusticestudiios.mongo.fixtures;

import java.io.IOException;
import java.net.UnknownHostException;

import com.fallenjusticestudiios.mongo.fixtures.resources.FileFixtureResource;
import com.fallenjusticestudiios.mongo.fixtures.resources.FixtureResource;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class FixtureManager {

	
	public static void main(String [] args) throws MongoException, IOException {
		Mongo mongo = new Mongo("localhost", 27017);
		DB db = mongo.getDB("bardwalk-test");

		FixtureResource resource = new FileFixtureResource("c:/dev/db/comment.fixture");
		Fixture.restoreFixture(db, "comment", resource);
	//	Fixture.saveFixture(db, "comment", resource);
		
	}
	
}
