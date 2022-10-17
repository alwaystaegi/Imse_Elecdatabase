package com.mongodb.quickstart;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.InsertManyOptions;
import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateFirst {


    public static void Create() throws IOException{

        try (MongoClient mongoClient = MongoClients.create(System.getenv("mongodb.uri"))) {
            MongoDatabase database = mongoClient.getDatabase("ElecData");
            MongoCollection<Document> elecCollection = database.getCollection("ElecData");
            MongoCollection<Document>  listCollection = database.getCollection("ListData");
            MongoCollection<Document>  SidoCollection = database.getCollection("SidoData");
//            insertOneDocument(gradesCollection);
            insertManyDocuments(elecCollection,listCollection,SidoCollection);
        }
    }

    private static void insertManyDocuments(MongoCollection<Document> elecCollection,MongoCollection<Document> listCollection,MongoCollection<Document> SidoCollection) throws IOException {
        List<Document> elec= new ArrayList<>();
        Date date= new Date();
        String yy= new SimpleDateFormat("yyyy").format(date);
        String mm= new SimpleDateFormat("MM").format(date);

        int year=Integer.parseInt(yy)-3;
        boolean flag=true;
        int i=1;
        int[] sidocount ={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
       ArrayList<String> cntrKndNmList= new ArrayList<>();
        ArrayList<String> sidoNmList= new ArrayList<>();
        ArrayList<String> sigunguNmList= new ArrayList<>();
        ArrayList<String> dateList= new ArrayList<>();
        while(flag){
//todo url에 넣기 cond%5BuseYy%3A%3ALIKE%5D=year

            StringBuilder urlBuilder= new StringBuilder("https://api.odcloud.kr/api/elecPowerContractUseService/v1/getUseQtyRegionalList?page="+i+"&perPage=10000&cond%5BuseYy%3A%3ALIKE%5D="+year+"&serviceKey="+System.getenv("serviceKey"));
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

                JSONArray datas=(JSONArray) parser.parse(obj.get("data").toString());


                for (Object datum : datas) {
                    JSONObject jsondatum=(JSONObject)datum;



                    if(i!=1)break;



                    if(sigunguNmList.size()>1&&sigunguNmList.get(0).equals(jsondatum.get("sigunguNm").toString())){
                        break;
                    }

                    if(sidoNmList.size()==0||sidoNmList.get(sidoNmList.size()-1).equals(jsondatum.get("sidoNm").toString())!=true){
                        sidoNmList.add(jsondatum.get("sidoNm").toString());}
                    if(sigunguNmList.size()==0||sigunguNmList.get(sigunguNmList.size()-1).equals(jsondatum.get("sigunguNm").toString())!=true){

                        sidocount[sidoNmList.size()-1]++;
                        sigunguNmList.add(jsondatum.get("sigunguNm").toString());

                    }
                    if(sigunguNmList.size()==1){
                        cntrKndNmList.add(jsondatum.get("cntrKndNm").toString());
                    }


                }

//System.out.println(dateList);

                if(datas.size()==0){
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
                    for(int j=0;j<datas.size();j++){
                        elec.add(Document.parse(datas.get(j).toString()));
                    }

                }
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
        int sigungucount=0;
        for(int j=0;j<sidoNmList.size()-1;j++){
            StringBuilder sidojsonstr= new StringBuilder("{");

            sidojsonstr.append("\"sidoNm\":\""+sidoNmList.get(j)+"\"}");

            StringBuilder sigungujsonstr= new StringBuilder("{");
            sigungujsonstr.append("\"sigunguNmList\":[");
            for(int k=0;k<sidocount[j];k++){

                sigungujsonstr.append('"'+sigunguNmList.get(sigungucount+k)+'"');
                if(k!=sidocount[j]-1)sigungujsonstr.append(",");
            }

            sigungucount+=sidocount[j];
            sigungujsonstr.append("]}");

           SidoCollection.insertOne(Document.parse(sidojsonstr.toString()));
           SidoCollection.insertOne(Document.parse(sigungujsonstr.toString()));
        }
        StringBuilder jsonstr= new StringBuilder("{");

        jsonstr.append("\"cntrKndNmList\":[");

        for(int j=0;j<cntrKndNmList.size();j++){
            jsonstr.append('"'+cntrKndNmList.get(j)+'"');

            if(j!=cntrKndNmList.size()-1)jsonstr.append(",");
        }
        jsonstr.append("],}");

       // System.out.println(jsonstr.toString());
     //   System.out.println(Document.parse(jsonstr.toString()));

       listCollection.insertOne(Document.parse(jsonstr.toString()));



       elecCollection.insertMany(elec,new InsertManyOptions().ordered(false));
    }

}
