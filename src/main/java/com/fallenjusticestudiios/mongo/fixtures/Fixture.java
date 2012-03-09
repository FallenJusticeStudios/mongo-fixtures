package com.fallenjusticestudiios.mongo.fixtures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.fallenjusticestudiios.mongo.fixtures.resources.FixtureResource;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class Fixture {

	public static void saveFixture(DB db, String name, FixtureResource resource) throws IOException {
		log.info("Saving [" + db.getName() + "." + db.getCollection(name) + "] to " + resource);
		int savedIndexes = 0;
		int savedDocuments = 0;
		PrintWriter output = new PrintWriter(resource.getWriter());
		output.println(name);
		output.println("");
		DBCollection collection = db.getCollection(name);
		List<DBObject> indexes = collection.getIndexInfo();
		for (DBObject index : indexes) {
			log.debug("Found index with keys: " + index.get("key"));
			output.println(INDEX_PREFIX + index.get("key"));
			savedIndexes++;
		}
		if (indexes.size() > 0)
			output.println("");

		DBCursor cursor = collection.find();
		while (cursor.hasNext()) {
			output.println(cursor.next());
			savedDocuments++;
		}
		log.info("Saved [" + savedDocuments + "] documents with [" + savedIndexes + "] indexes in " + resource);
		resource.close();
		
	}

	public static void restoreFixture(DB db, String name, FixtureResource resource) throws IOException {
		log.info("Restoring [" + db.getName() + "." + db.getCollection(name) + "] from " + resource);
		
		LineNumberReader lnr = new LineNumberReader(resource.getReader());
		String storeName = lnr.readLine();
		
		if (!storeName.equals(name)) {
			String message = "Stored fixture name [" + storeName + "] doesn't match collection name [" + name + "] for restore.";
			log.error(message);
			throw new RuntimeException(message);
		}

		DBCollection collection = db.getCollection(name);
		if (collection != null) {
			log.info("Found collection [" + name + "] dropping collection before applying fixture.");
			collection.drop();
		}

		collection = db.getCollection(name);
		String line = null;
		int restoredDocuments = 0;
		int blankLines = 0;
		//indexes are saved and proccessed after all the data  has been restored.
		List<DBObject> indexes = new ArrayList<DBObject>(); 
		try {
			while ((line = lnr.readLine()) != null) {
				if (!line.trim().equals("")) {
					if (line.startsWith(INDEX_PREFIX)) {
						indexes.add((DBObject) JSON.parse(line.substring(INDEX_OFFSET)));
						
					} else {
						DBObject dbObject = (DBObject) JSON.parse(line);
						collection.insert(dbObject);
						restoredDocuments++;
					}
				} else
					blankLines++;
			} 
			
			for (DBObject index : indexes) {
				collection.createIndex(index);
			}
			
			
			log.info("Restored [" + restoredDocuments + "] with [" + indexes.size() + "]");
		} catch (Throwable t) {
			log.error("Handling line " + lnr.getLineNumber() + " of " + resource + "\nline was[" + line + "]", t);
		}
		
		resource.close();

	}

	private static final String INDEX_PREFIX = "index: ";
	private static final int INDEX_OFFSET = 7;
	private static Logger log = Logger.getLogger(Fixture.class);
}
