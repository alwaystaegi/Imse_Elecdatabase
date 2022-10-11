package com.mongodb.quickstart;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Crawling {
    public static void Crawling() throws IOException {

        String url ="https://home.kepco.co.kr/kepco/getPowerGraphPop.do";
    Document doc = Jsoup.connect(url).get();
    //!한전
        System.out.println("공급능력"+doc.select("#supply_demand > .graph_value").text());//공급능력
        System.out.println("현재 부하량"+doc.select("#nowValue > .graph_value").text());//현재부하
        System.out.println("예비율"+doc.select("#reservePercent > .graph_value").text());// 예비율
        URL url1= new URL("https://epsis.kpx.or.kr/epsisnew/selectMain.do?locale=");
        BufferedReader br = new BufferedReader(new InputStreamReader(url1.openStream()));
        StringBuffer sourceCode = new StringBuffer();
        String sourceLine = "";
        while((sourceLine=br.readLine())!=null){
            sourceCode.append(sourceLine+"\n");
        }
        System.out.println(sourceCode);

    }


}