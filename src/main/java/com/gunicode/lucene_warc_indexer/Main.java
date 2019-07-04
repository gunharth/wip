package com.gunicode.lucene_warc_indexer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.*;

public class Main
{

    public static void main(String[] args) throws Exception {

        String warcPath = "input/ClueWeb09_English_Sample.warc.gz";
        String indexPath = "index";
        Indexer indexer = new Indexer();




        //Input Path Variable
        final Path docDir = Paths.get(warcPath);

        try {
            //org.apache.lucene.store.Directory instance
            Directory dir = FSDirectory.open(Paths.get(indexPath));

            //Standard-Analyzer with predefined stop words
            Analyzer analyzer = new StandardAnalyzer();

            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            //iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
            iwc.setOpenMode(OpenMode.CREATE);
            IndexWriter writer = new IndexWriter(dir, iwc);
            indexer.indexDocs(writer, docDir);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}