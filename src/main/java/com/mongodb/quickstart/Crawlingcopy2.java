package com.mongodb.quickstart;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;




public class Crawlingcopy2 implements Job {
    private static final SimpleDateFormat TIMESTAMP_FMT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSSS");

    SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd-hh-mm");

    @Override
    public void execute(JobExecutionContext ctx) throws JobExecutionException {

        /**
         * JobData에 접근
         */
        Date date= new Date();
        String now= format.format(date);
        String year=new SimpleDateFormat("yyyy").format(date);
        String month=new SimpleDateFormat("MM").format(date);
        String day= new SimpleDateFormat("dd").format(date);
        String hour= new SimpleDateFormat("hh").format(date);
        String minute= new SimpleDateFormat("mm").format(date);
        System.out.println("돌아가는중");
try {
    String url = "https://home.kepco.co.kr/kepco/getPowerGraphPop.do";
    Document doc = Jsoup.connect(url).get();
    String supplydemand= doc.select("#supply_demand > .graph_value").text().toString().replace(",","");
    String nowValue=doc.select("#nowValue > .graph_value").text().toString().replace(",","");
    String reservePercent=doc.select("#reservePercent > .graph_value").text().toString().replace(",","");

    String nowStr="{"+"supplydemand:"+supplydemand+",nowValue:"+nowValue+",reservePercent:"+reservePercent+",time:"+hour+minute+"}";


    MongoClient mongoClient = MongoClients.create(System.getenv("mongodb.uri"));

        MongoDatabase database = mongoClient.getDatabase("ElecData");
        if(database.getCollection(year+"-"+month+"-"+day)==null){
            database.createCollection(year+"-"+month+"-"+day);
        };
        MongoCollection<org.bson.Document> elecCollection = database.getCollection(year+"-"+month+"-"+day);
System.out.println(nowStr);
        elecCollection.insertOne(org.bson.Document.parse(nowStr));
    } catch (IOException ex) {
    throw new RuntimeException(ex);
}
    }
    }

