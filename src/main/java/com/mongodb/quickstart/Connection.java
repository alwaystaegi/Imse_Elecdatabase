package com.mongodb.quickstart;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;

import static com.mongodb.client.model.Filters.eq;

public class Connection {
    public static void main(String[] args) throws IOException {
        try (MongoClient mongoClient = MongoClients.create(System.getenv("mongodb.uri"))) {
            MongoDatabase database = mongoClient.getDatabase("ElecData");
            if(database.getCollection("ElecData")==null){
                Create.Create();
            }
            else {
                MongoCollection<Document> elecCollection = database.getCollection("updateAt");
            }

        mongoClient.close();
        }
        Interval.Interval();


    }
}


