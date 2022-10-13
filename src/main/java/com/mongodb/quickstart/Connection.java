package com.mongodb.quickstart;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import java.io.IOException;


public class Connection {
    public static void main(String[] args) throws IOException {
        try (MongoClient mongoClient = MongoClients.create(System.getenv("mongodb.uri"))) {
            MongoDatabase database = mongoClient.getDatabase("ElecData");
            if(database.getCollection("ElecData")==null){
                Create.Create();
            }
        }
        Interval.Interval();
    }
}


