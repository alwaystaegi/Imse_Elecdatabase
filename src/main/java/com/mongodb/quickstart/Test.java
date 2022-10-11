package com.mongodb.quickstart;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Test
{
    public static void Test(){
    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");

    Calendar calendar =Calendar.getInstance();
    calendar.set(Calendar.SECOND,0);
    if(calendar.get(Calendar.MINUTE)<15){
        calendar.set(Calendar.MINUTE,15);
    }
    else if(calendar.get(Calendar.MINUTE)<30){
        calendar.set(Calendar.MINUTE,30);
    }
    else if(calendar.get(Calendar.MINUTE)<45){
        calendar.set(Calendar.MINUTE,45);
    }
    else if(calendar.get(Calendar.MINUTE)<60){
        calendar.set(Calendar.MINUTE,0);
    }
    Date date=calendar.getTime();

    System.out.println(date);
    System.out.println(new Date());
}
}
