package com.mongodb.quickstart;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonParseException;
import org.bson.json.JsonWriterSettings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.*;


public class Update {

    public static void Update() throws IOException {
        System.out.println("1");
        StringBuilder urlBuilder = new StringBuilder("https://api.odcloud.kr/api/elecPowerContractUseService/v1/getUseQtyRegionalList?page=1&perPage=10&serviceKey=lAPHBd6NvtCWtopc61ow6f4RcbrM6tlHKc4s5Q3qJqPE6M3RdTnDdxhl3ayxRSYy3%2FEcoEZ13qVIrMzNjOyZxQ%3D%3D");
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        // 9. 저장된 데이터를 라인별로 읽어 StringBuilder 객체로 저장.
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        // 10. 객체 해제.
        rd.close();
        conn.disconnect();
        // 11. 전달받은 데이터 확인.
//        System.out.println(sb.toString());

        try {
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(sb.toString());
//            System.out.println(parser.parse(obj.get("data").toString()));
            JSONArray data=(JSONArray) parser.parse(obj.get("data").toString());

                System.out.println(data.size());

        }
        catch(Exception e){
            System.out.println(e);
        };


//
//        JsonWriterSettings prettyPrint = JsonWriterSettings.builder().indent(true).build();
//
//        try (MongoClient mongoClient = MongoClients.create(System.getenv("mongodb.uri"))) {
//            MongoDatabase sampleTrainingDB = mongoClient.getDatabase("sample_training");
//            MongoCollection<Document> gradesCollection = sampleTrainingDB.getCollection("grades");
//
//            // update one document
//            Bson filter = eq("student_id", 10000);
//            Bson updateOperation = set("comment", "You should learn MongoDB!");
//            UpdateResult updateResult = gradesCollection.updateOne(filter, updateOperation);
//            System.out.println("=> Updating the doc with {\"student_id\":10000}. Adding comment.");
//            System.out.println(gradesCollection.find(filter).first().toJson(prettyPrint));
//            System.out.println(updateResult);
//
//            // upsert
//            filter = and(eq("student_id", 10002d), eq("class_id", 10d));
//            updateOperation = push("comments", "You will learn a lot if you read the MongoDB blog!");
//            UpdateOptions options = new UpdateOptions().upsert(true);
//            updateResult = gradesCollection.updateOne(filter, updateOperation, options);
//            System.out.println("\n=> Upsert document with {\"student_id\":10002.0, \"class_id\": 10.0} because it doesn't exist yet.");
//            System.out.println(updateResult);
//            System.out.println(gradesCollection.find(filter).first().toJson(prettyPrint));
//
//            // update many documents
//            filter = eq("student_id", 10001);
//            updateResult = gradesCollection.updateMany(filter, updateOperation);
//            System.out.println("\n=> Updating all the documents with {\"student_id\":10001}.");
//            System.out.println(updateResult);
//
//            // findOneAndUpdate
//            filter = eq("student_id", 10000);
//            Bson update1 = inc("x", 10); // increment x by 10. As x doesn't exist yet, x=10.
//            Bson update2 = rename("class_id", "new_class_id"); // rename variable "class_id" in "new_class_id".
//            Bson update3 = mul("scores.0.score", 2); // multiply the first score in the array by 2.
//            Bson update4 = addToSet("comments", "This comment is uniq"); // creating an array with a comment.
//            Bson update5 = addToSet("comments", "This comment is uniq"); // using addToSet so no effect.
//            Bson updates = combine(update1, update2, update3, update4, update5);
//            // returns the old version of the document before the update.
//            Document oldVersion = gradesCollection.findOneAndUpdate(filter, updates);
//            System.out.println("\n=> FindOneAndUpdate operation. Printing the old version by default:");
//            System.out.println(oldVersion.toJson(prettyPrint));
//
//            // but I can also request the new version
//            filter = eq("student_id", 10001);
//            FindOneAndUpdateOptions optionAfter = new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER);
//            Document newVersion = gradesCollection.findOneAndUpdate(filter, updates, optionAfter);
//            System.out.println("\n=> FindOneAndUpdate operation. But we can also ask for the new version of the doc:");
//            System.out.println(newVersion.toJson(prettyPrint));
    }
}
