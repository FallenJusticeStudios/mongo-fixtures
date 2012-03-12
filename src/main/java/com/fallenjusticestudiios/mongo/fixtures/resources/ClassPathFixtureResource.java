package com.fallenjusticestudiios.mongo.fixtures.resources;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;


import org.apache.log4j.Logger;

public class ClassPathFixtureResource implements FixtureResource {
	private String path;
	private Reader reader;
	public ClassPathFixtureResource(String path) {
		this.path = path;
	}
	
	public synchronized Reader getReader() throws IOException {
		if (reader != null) {
			log.warn("getReader() called on FileFixutreResoruce  more that once. A resource cannot be user for more that one open operation.");
			throw new IllegalStateException("getReader() called on FileFixutreResoruce  more that once. A resource cannot be user for more that one open operation.");
		}
		
		reader = new InputStreamReader(FixtureResource.class.getResourceAsStream(path));
		return reader;
	}
	
	public Writer getWriter() throws IOException {
		throw new IllegalStateException("can not get a writer from a ClassPathResource");
	}
	
	public void close() throws IOException {
		// TODO Auto-generated method stub
		if (reader != null) {
			reader.close();
			reader = null;
		}
	}
	
	private static Logger log = Logger.getLogger(FileFixtureResource.class);
}
