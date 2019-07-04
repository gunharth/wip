package anotherwarc;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import info.debatty.java.datasets.reuters.*;

import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main
{

    public static void main(String[] args) throws Exception {
        // We will use reuters news dataset
        Dataset reuters_dataset = new Dataset("reuters21578");

        String indexPath = "index";

        Directory dir = FSDirectory.open(Paths.get(indexPath));

        Analyzer analyzer = new StandardAnalyzer();

        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        //iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
        iwc.setOpenMode(OpenMode.CREATE);

        IndexWriter writer = new IndexWriter(dir, iwc);

        Integer i = 1;
        // Iterate over news
        for (News news : reuters_dataset) {
            System.out.println(i + " " + news.date);
            String date1 = news.date.substring(0,20);//he
            System.out.println(date1);
            SimpleDateFormat formatter6=new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
            Date date6=formatter6.parse(date1);
            System.out.println(date6);
            SimpleDateFormat formatter7=new SimpleDateFormat("yyyyMMdd");
            String date7 = formatter7.format(date6);
            System.out.println(date7);
            /*SimpleDateFormat formatter7=new SimpleDateFormat("dd-MMM-yyyy");
            Date date7=formatter7.parse(String.valueOf(date6));
            System.out.println(date7);*/
            //System.out.println(news.body);

            Document doc = new Document();
            doc.add(new StringField("date", date7, Field.Store.YES));
            doc.add(new StringField("title", news.title, Field.Store.YES));
            doc.add(new TextField("body", news.body, Field.Store.YES));

            writer.addDocument(doc);
        i++;

        }

        writer.close();

    }
}