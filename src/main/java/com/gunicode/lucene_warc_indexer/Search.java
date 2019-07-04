package com.gunicode.lucene_warc_indexer;

import java.nio.file.Paths;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

public class Search
{
    public static void main(String[] args) throws Exception {

        StandardAnalyzer standardAnalyzer = new StandardAnalyzer();
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("outputIndex")));
        IndexSearcher searcher = new IndexSearcher(reader);

        QueryParser parser = new QueryParser("body", standardAnalyzer);

        Query query = parser.parse("Template");

        // shows the amount of documents
        TopDocs results = searcher.search(query, 100);
        System.out.println("Hits for Templates -->" + results.totalHits);

    }
}