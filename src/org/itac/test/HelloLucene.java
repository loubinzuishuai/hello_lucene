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
	
	//��������
	public void index() throws CorruptIndexException, LockObtainFailedException, IOException{
		
		//����Directory
		Directory directory = FSDirectory.open(new File("F:/lucene/index01"));

	    //����IndexWriter
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35));
		IndexWriter writer = null;
		try{
			
			 
			writer = new IndexWriter(directory, iwc);
			
			//����Document����
			Document doc = null;
			File f = new File("F:/lucene/example");
			for(File file:f.listFiles())
			{
				//λDocument�������Field
				doc = new Document();
				doc.add(new Field("content", new FileReader(file)));
				doc.add(new Field("filename", file.getName(), Field.Store.YES, Field.Index.NOT_ANALYZED));
				doc.add(new Field("path", file.getAbsolutePath(), Field.Store.YES, Field.Index.NOT_ANALYZED));
				//ͨ��IndexWriter��Ӷ���������
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
	
	//����
	public void searcher() throws IOException, ParseException{
		//1.����Directory
		Directory directory = FSDirectory.open(new File("F:/lucene/index01"));
		//2.����IndexReader
		IndexReader reader = IndexReader.open(directory);
		//3.����IndexReader����IndexSeacher
		IndexSearcher searcher = new IndexSearcher(reader);
		//4.����������Query
		QueryParser parser = new QueryParser(Version.LUCENE_35, "content", new StandardAnalyzer(Version.LUCENE_35));
		Query query = parser.parse("AA");
		//5.����searcher�������ҷ���TopDocs
		TopDocs tds = searcher.search(query, 10);
		//6.����TopDocs��ȡScoreDoc����
		ScoreDoc sds[] = tds.scoreDocs;
		for(ScoreDoc sd:sds)
		{
			//7.����searcher��ScoreDoc�����ȡDocument����
			Document dc = searcher.doc(sd.doc);
			//8.����Document�����ȡ��Ҫ��ֵ
			System.out.println(dc.get("filename") + "[" + dc.get("path") + "]");
			
		}
		
		
		reader.close();
	}

}
