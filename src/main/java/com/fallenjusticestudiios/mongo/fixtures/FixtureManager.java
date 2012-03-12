package com.fallenjusticestudiios.mongo.fixtures;

import java.io.IOException;
import java.net.UnknownHostException;

import com.fallenjusticestudiios.mongo.fixtures.resources.FileFixtureResource;
import com.fallenjusticestudiios.mongo.fixtures.resources.FixtureResource;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class FixtureManager {
	
	
	public FixtureManager(String host, int port, String databaseName) throws UnknownHostException, MongoException {
		Mongo mongo = new Mongo(host, port);
		db = mongo.getDB(databaseName);
	}
	
	private DB db;
	public FixtureManager(DB db) {
		this.db = db;
	}
	
	public void save(String collectionName, String filename ) throws IOException {
		FixtureResource resource = new FileFixtureResource(filename);
		Fixture.saveFixture(db, collectionName, resource);
		
	}
	
	public void restore(String collectionName, String filename ) throws IOException {
		FixtureResource resource = new FileFixtureResource(filename);
		Fixture.restoreFixture(db, collectionName, resource);	
	}
	
	public void save(String collectionName, FixtureResource resource) throws IOException {
		Fixture.saveFixture(db, collectionName, resource);
	}
	
	public void restore(String collectionName, FixtureResource resource) throws IOException {
		Fixture.restoreFixture(db, collectionName, resource);
	}
	
	
	public static void main(String [] args) throws MongoException, IOException {
		Mongo mongo = new Mongo("localhost", 27017);
		DB db = mongo.getDB("bardwalk-test");

		FixtureResource resource = new FileFixtureResource("c:/dev/db/comment.fixture");
		Fixture.restoreFixture(db, "comment", resource);
	//	Fixture.saveFixture(db, "comment", resource);
		
	}
	
}
