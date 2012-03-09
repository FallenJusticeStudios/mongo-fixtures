package com.fallenjusticestudiios.mongo.fixtures.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import org.apache.log4j.Logger;

public class FileFixtureResource implements FixtureResource {
	
	
	private File file;
	private Reader reader;
	private Writer writer;
	
	public FileFixtureResource(String filename) {
		file = new File(filename);
	}
	
	public FileFixtureResource(File file) {
		this.file = file;
	}

	public synchronized Reader getReader() throws IOException {
		if (reader != null) {
			log.warn("getReader() called on FileFixutreResoruce  more that once. A resource cannot be user for more that one operation.");
			throw new IllegalStateException("getReader() called on FileFixutreResoruce  more that once. A resource cannot be user for more that one operation.");
		}
		if (writer != null) {
			log.warn("getReader() called on FileFixutreResoruce after it was used as a writer. A resource cannot be user for more that one operation.");
			throw new IllegalStateException("getReader() called on FileFixutreResoruce after it was used as a writer. A resource cannot be user for more that one operation.");
		
		}
		reader = new FileReader(file);
		return reader;
	}
	
	public synchronized Writer getWriter() throws IOException {
		if (writer != null) {
			log.warn("getWriter() called on FileFixutreResoruce more that once. A resource cannot be user for more that one operation.");
			throw new IllegalStateException("getWriter() called on FileFixutreResoruce more that once. A resource cannot be user for more that one operation.");
		}
		if (reader != null) {
			log.warn("getWriter() called on FileFixutreResoruce after it was used as a reader. A resource cannot be user for more that one operation.");
			throw new IllegalStateException("getWriter() called on FileFixutreResoruce after it was used as a reader. A resource cannot be user for more that one operation.");
		
		}
		writer = new FileWriter(file);
		return writer;
	}
	
	public void close() throws IOException {
		if (reader != null)
			reader.close();
		
		if (writer != null)
			writer.close();
	}
	
	public String toString() {
		return "FileFixtureReader[file=" + file.getAbsolutePath() + "]"; 
	}

	private static Logger log = Logger.getLogger(FileFixtureResource.class);
}
