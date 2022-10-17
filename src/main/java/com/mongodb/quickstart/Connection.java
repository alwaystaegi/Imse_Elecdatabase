/*제작기간 : 2022/10/03~2022/10/16
* 목적: Electrickshock의 데이터베이스 관리를 위한 자바용 프로그램
* 사용 라이브러리: json-simple-1.1.1,jsoup-1.15.3 , quartz-2.3.0_SNAPSHOT
*
*
*
* */


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
               CreateFirst.Create();
            }
        }
        Interval.Interval();
    }
}


