package com.mongodb.quickstart;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;


public class Crawlingcopy{

    public static void Crawling() throws IOException {

        SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd-hh-mm");
        Date date= new Date();
        String now= format.format(date);



        String url ="https://home.kepco.co.kr/kepco/getPowerGraphPop.do";
        Document doc = Jsoup.connect(url).get();
        String supplydemand= doc.select("#supply_demand > .graph_value").text().toString().replace(",","");
        String nowValue=doc.select("#nowValue > .graph_value").text().toString().replace(",","");
        String reservePercent=doc.select("#reservePercent > .graph_value").text().toString().replace(",","");


        System.out.println(now+"    "+supplydemand+"     "+nowValue+"       "+reservePercent);


        URL url1= new URL("https://epsis.kpx.or.kr/epsisnew/selectMain.do?locale=");
        BufferedReader br = new BufferedReader(new InputStreamReader(url1.openStream()));
        StringBuffer sourceCode = new StringBuffer();
        String sourceLine = "";
        while((sourceLine=br.readLine())!=null){
            sourceCode.append(sourceLine+"\n");
        }

    }


}
