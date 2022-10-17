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




public class Crawling implements Job {


    @Override
    public void execute(JobExecutionContext ctx) throws JobExecutionException {

        /**
         * JobData에 접근
         */
        Date date= new Date();
        String year=new SimpleDateFormat("yyyy").format(date);
        String month=new SimpleDateFormat("MM").format(date);
        String day= new SimpleDateFormat("dd").format(date);
        String hour= new SimpleDateFormat("HH").format(date);
        String minute= new SimpleDateFormat("mm").format(date);
        System.out.println("돌아가는중");
try {
    String url = "https://home.kepco.co.kr/kepco/getPowerGraphPop.do";
    Document doc = Jsoup.connect(url).get();
    String supplydemand= doc.select("#supply_demand > .graph_value").text().replace(",","");
    String nowValue=doc.select("#nowValue > .graph_value").text().replace(",","");
    String reservePercent=doc.select("#reservePercent > .graph_value").text().replace(",","");

    String nowStr="{"+"supplydemand:"+supplydemand+",nowValue:"+nowValue+",reservePercent:"+reservePercent+",date:\""+year+"/"+month+"/"+day+"\",time:\""+hour+":"+minute+"\",}";


    System.out.println(org.bson.Document.parse(nowStr));

    MongoClient mongoClient = MongoClients.create(System.getenv("mongodb.uri"));
        MongoDatabase database = mongoClient.getDatabase("ElecData");
    MongoCollection<org.bson.Document>  DayCollection = database.getCollection("DayStatis");
    DayCollection.insertOne(org.bson.Document.parse(nowStr));

        //todo 이슈처리 mongoClient와 연결종료가 안되는디... 왜?
        mongoClient.close();
    } catch (IOException ex) {
    throw new RuntimeException(ex);
}
    }
    }

