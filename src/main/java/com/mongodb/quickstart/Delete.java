package com.mongodb.quickstart;

import com.mongodb.client.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.mongodb.client.model.Filters.*;

public class Delete implements Job {
    public void execute(JobExecutionContext ctx) throws JobExecutionException {
            SimpleDateFormat Format=new SimpleDateFormat("yyyy/MM/dd");
            Date date= new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            String Today;
            String Yesterday;
            Today= Format.format(cal);
            cal.add(Calendar.DATE, -1);
            Yesterday=Format.format(cal);



            try (MongoClient mongoClient = MongoClients.create(System.getenv("mongodb.uri"))) {
                System.out.println("오류포인트2");
                MongoDatabase sampleTrainingDB = mongoClient.getDatabase("Elecdata");
                System.out.println("오류포인트3");
                MongoCollection<Document> DayCollection = sampleTrainingDB.getCollection("DayStatic");
                MongoCollection<Document> ElecCollection = sampleTrainingDB.getCollection("ElecData");
                MongoCollection<Document> ListCollection = sampleTrainingDB.getCollection("ListData");

                Bson elecfilter=not(in("sidoNm","  "));
                Bson dayfilter= nor(in("Date",Today),in("Date",Yesterday));


                ElecCollection.deleteMany(elecfilter);
                DayCollection.deleteMany(dayfilter);
                ListCollection.deleteMany(elecfilter);

            }

        try {
            Create.Create();
        } catch (IOException e) {
            e.printStackTrace();
        }
        }
    }
