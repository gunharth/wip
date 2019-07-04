package warctest;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class Main {

    public static void main(String[] args) throws IOException {
        String inputWarcFile="input/ClueWeb09_English_Sample.warc";
        // open our gzip input stream
        //GZIPInputStream inputStream =new GZIPInputStream(new FileInputStream(inputWarcFile));

        InputStream inputStream = new FileInputStream(inputWarcFile);

        // cast to a data input stream
        DataInputStream inStream=new DataInputStream(inputStream);

        // iterate through our stream
        WarcRecord thisWarcRecord;
        while ((thisWarcRecord=WarcRecord.readNextWarcRecord(inStream))!=null) {
            // see if it's a response record
            if (thisWarcRecord.getHeaderRecordType().equals("response")) {
                // it is - create a WarcHTML record
                WarcHTMLResponseRecord htmlRecord=new WarcHTMLResponseRecord(thisWarcRecord);
                // get our TREC ID and target URI
                String thisTRECID=htmlRecord.getTargetTrecID();
                String thisTargetURI=htmlRecord.getTargetURI();
                WarcRecord thisRawRecord=htmlRecord.getRawRecord();
                // print our data
                System.out.println(thisTRECID + " : " + thisTargetURI  + " : " + htmlRecord);
            }
        }

        inStream.close();
    }
}
