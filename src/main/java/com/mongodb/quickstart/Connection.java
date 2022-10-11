package com.mongodb.quickstart;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class Connection {
    public static void main(String[] args) throws IOException {
        String connectionString =System.getenv("mongodb.uri");
        try (MongoClient mongoClient = MongoClients.create(System.getenv("mongodb.uri"))) {

            MongoDatabase database = mongoClient.getDatabase("ElecData");
            MongoCollection<Document> elecCollection = database.getCollection("ElecData");
// elecCollection.find().forEach(db->{
//            System.out.println(db);
//
//        });;
            System.out.println(1);
            Crawling.Crawling();

//            elecCollection.find(eq("useYy", "2021")).forEach(db->{
//                System.out.println(1);
//                System.out.println(db.toJson());
//            });
        }
//
//        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
//            List<Document> databases = mongoClient.listDatabases().into(new ArrayList<>());
//            databases.forEach(db -> System.out.println(db.toJson()));
//        }

        //       Create.Create();
//        Update.Update();

    }
}
