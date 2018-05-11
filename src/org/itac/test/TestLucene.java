package org.itac.test;

import java.io.IOException;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.store.LockObtainFailedException;
import org.junit.Test;

public class TestLucene {
	
	@Test
	public void testIndex() throws CorruptIndexException, LockObtainFailedException, IOException{
		HelloLucene hl = new HelloLucene();
		hl.index();
	}
	
	@Test
	public void testSearcher() throws IOException, ParseException{
		HelloLucene hl = new HelloLucene();
		hl.searcher();
	}

}
