package com.gunicode.lucene_warc_indexer;

import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class Indexer
{
    public void indexDocs(final IndexWriter writer, Path path) throws IOException
    {
        if (Files.isDirectory(path)) {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    try {
                        indexDoc(writer, file, attrs.lastModifiedTime().toMillis());
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } else {
            indexDoc(writer, path, Files.getLastModifiedTime(path).toMillis());
        }
    }

    private void indexDoc(IndexWriter writer, Path file, long lastModified) throws IOException {

        try {

            //Lucene document
            Document doc = new Document();
            doc.add(new StringField("path", file.toString(), Field.Store.YES));
            doc.add(new LongPoint("modified", lastModified));
            doc.add(new TextField("contents", new String(Files.readAllBytes(file)), Field.Store.YES));


            // Call JSoup method & creating Index
            //JSoupRefactoring jSoupObject = new JSoupRefactoring();
           // jSoupObject.parseHtml(file, writer, doc);

            org.jsoup.nodes.Document soupDoc = Jsoup.parse(String.valueOf(doc));
            String title = soupDoc.title();
            String body = soupDoc.body().text();

            System.out.printf("Title: %s%n", title);
            //System.out.printf("Body: %s", soupBody);

            // creating Lucene-Document from JSoup Document
            Document doc1 = new Document();
            doc1.add(new StringField("title", title, Field.Store.YES));
            doc1.add(new TextField("body", body, Field.Store.YES));

            //System.out.printf("Title: %s%n", soupTitle);
            //System.out.printf("Body: %s", soupBody);

            //Writing Lucene-Index-Files
            writer.updateDocument(new Term("contents", file.toString()), doc1);

            //Index Writer is in JSoup Class
            //writer.updateDocument(new Term("contents", file.toString()), doc);
        } catch(Exception e) {
            System.out.println("Error while indexing document. " + e);
        }
    }
}
