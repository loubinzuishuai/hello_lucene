package org.itac.test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;



public class HelloLucene {
	
	//建立索引
	public void index() throws CorruptIndexException, LockObtainFailedException, IOException{
		
		//创建Directory
		Directory directory = FSDirectory.open(new File("F:/lucene/index01"));

	    //创建IndexWriter
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35));
		IndexWriter writer = null;
		try{
			
			 
			writer = new IndexWriter(directory, iwc);
			
			//创建Document对象
			Document doc = null;
			File f = new File("F:/lucene/example");
			for(File file:f.listFiles())
			{
				//位Document对象添加Field
				doc = new Document();
				doc.add(new Field("content", new FileReader(file)));
				doc.add(new Field("filename", file.getName(), Field.Store.YES, Field.Index.NOT_ANALYZED));
				doc.add(new Field("path", file.getAbsolutePath(), Field.Store.YES, Field.Index.NOT_ANALYZED));
				//通过IndexWriter添加对象到索引中
				writer.addDocument(doc);
			}
			
			
			
		}
		catch (Exception e){
			e.printStackTrace();
		}
		finally{
			if(writer != null)
				writer.close();
		}
		
	}
	
	//搜索
	public void searcher() throws IOException, ParseException{
		//1.创建Directory
		Directory directory = FSDirectory.open(new File("F:/lucene/index01"));
		//2.创建IndexReader
		IndexReader reader = IndexReader.open(directory);
		//3.根据IndexReader创建IndexSeacher
		IndexSearcher searcher = new IndexSearcher(reader);
		//4.创建搜索的Query
		QueryParser parser = new QueryParser(Version.LUCENE_35, "content", new StandardAnalyzer(Version.LUCENE_35));
		Query query = parser.parse("AA");
		//5.根据searcher搜索并且返回TopDocs
		TopDocs tds = searcher.search(query, 10);
		//6.根据TopDocs获取ScoreDoc对象
		ScoreDoc sds[] = tds.scoreDocs;
		for(ScoreDoc sd:sds)
		{
			//7.根据searcher和ScoreDoc对象获取Document对象
			Document dc = searcher.doc(sd.doc);
			//8.根据Document对象获取需要的值
			System.out.println(dc.get("filename") + "[" + dc.get("path") + "]");
			
		}
		
		
		reader.close();
	}

}
