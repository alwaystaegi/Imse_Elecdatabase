package com.mongodb.quickstart;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.InsertManyOptions;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static java.util.Arrays.asList;

public class Create{


    public static void Create() throws IOException{

        try (MongoClient mongoClient = MongoClients.create(System.getenv("mongodb.uri"))) {

            MongoDatabase database = mongoClient.getDatabase("ElecData");
            MongoCollection<Document> elecCollection = database.getCollection("ElecData");

//            insertOneDocument(gradesCollection);
            insertManyDocuments(elecCollection);
        }
    }

    private static void insertOneDocument(MongoCollection<Document> gradesCollection) {
//        gradesCollection.insertOne(generateNewGrade(10000d, 1d));
//        System.out.println("One grade inserted for studentId 10000.");
    }

    private static void insertManyDocuments(MongoCollection<Document> elecCollection) throws IOException {
        List<Document> elec = new ArrayList<>();
        Date date= new Date();
        String yy= new SimpleDateFormat("yyyy").format(date);
        String mm= new SimpleDateFormat("MM").format(date);
        int year=Integer.parseInt(yy)-5;
        boolean flag=true;
        int i=1;
        while(flag){
//todo url에 넣기 cond%5BuseYy%3A%3ALIKE%5D=year

            StringBuilder urlBuilder= new StringBuilder("https://api.odcloud.kr/api/elecPowerContractUseService/v1/getUseQtyRegionalList?page="+i+"&perPage=10000&cond%5BuseYy%3A%3ALIKE%5D="+year+"&serviceKey=lAPHBd6NvtCWtopc61ow6f4RcbrM6tlHKc4s5Q3qJqPE6M3RdTnDdxhl3ayxRSYy3%2FEcoEZ13qVIrMzNjOyZxQ%3D%3D");
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
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


            try {
                JSONParser parser = new JSONParser();
                JSONObject obj = (JSONObject) parser.parse(sb.toString());

                JSONArray data=(JSONArray) parser.parse(obj.get("data").toString());



                if(data.size()==0){
                    if(year==Integer.parseInt(yy)) {
                        flag=false;


                    }
                    else {
                        year++;
                        i=1;
                        continue;
                    }

                   }
                else{
                    for(int j=0;j<data.size();j++){
                        elec.add(Document.parse(data.get(j).toString()));
                    }
                    System.out.println("됐냐");


                }
//                System.out.println(data.get(0));
            }
            catch(Exception e){
                if(year==Integer.parseInt(yy)) {
                    flag=false;

                }
                else {
                    year++;}

            };
    i++;



        }
        elecCollection.insertMany(elec,new InsertManyOptions().ordered(false));
//        for (double classId = 1d; classId <= 10d; classId++) {
//            grades.add(generateNewGrade(10001d, classId));
//        }
//
//        gradesCollection.insertMany(grades, new InsertManyOptions().ordered(false));
//        System.out.println("Ten grades inserted for studentId 10001.");



    }

//    private static Document generateNewGrade(double studentId, double classId) {
//        List<Document> scores = asList(new Document("type", "exam").append("score", rand.nextDouble() * 100),
//                                       new Document("type", "quiz").append("score", rand.nextDouble() * 100),
//                                       new Document("type", "homework").append("score", rand.nextDouble() * 100),
//                                       new Document("type", "homework").append("score", rand.nextDouble() * 100));
//        return new Document("_id", new ObjectId()).append("student_id", studentId)
//                                                  .append("class_id", classId)
//                                                  .append("scores", scores);
//    }
}
