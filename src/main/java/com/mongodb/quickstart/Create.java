package com.mongodb.quickstart;

import com.mongodb.client.*;
import com.mongodb.client.model.InsertManyOptions;
import org.bson.Document;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Create {


    public static void Create() throws IOException {

        try (MongoClient mongoClient = MongoClients.create(System.getenv("mongodb.uri"))) {
            MongoDatabase database = mongoClient.getDatabase("ElecData");
            MongoCollection<Document> elecCollection = database.getCollection("ElecData");

            insertManyDocuments(elecCollection);
        }
    }

    private static void insertManyDocuments(MongoCollection<Document> elecCollection) throws IOException {
        List<Document> elec = new ArrayList<>();
        Date date = new Date();
        String yy = new SimpleDateFormat("yyyy").format(date);
        String mm = new SimpleDateFormat("MM").format(date);

        int year = Integer.parseInt(yy) - 3;
        boolean flag = true;
        int i = 1;
        while (flag) {
//todo url에 넣기 cond%5BuseYy%3A%3ALIKE%5D=year

            StringBuilder urlBuilder = new StringBuilder("https://api.odcloud.kr/api/elecPowerContractUseService/v1/getUseQtyRegionalList?page=" + i + "&perPage=10000&cond%5BuseYy%3A%3ALIKE%5D=" + year + "&serviceKey=" + System.getenv("serviceKey"));
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

            elecCollection.insertMany(elec, new InsertManyOptions().ordered(false));
        }
    }
}