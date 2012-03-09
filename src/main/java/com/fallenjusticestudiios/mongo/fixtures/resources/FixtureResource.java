package com.fallenjusticestudiios.mongo.fixtures.resources;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;



public interface FixtureResource  {
	public Reader getReader() throws IOException;
	public Writer getWriter() throws IOException;
	public void close() throws IOException;
}
