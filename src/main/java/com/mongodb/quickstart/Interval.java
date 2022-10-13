package com.mongodb.quickstart;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Interval{

    public static void Interval(){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");

        Calendar calendar =Calendar.getInstance();

        Date date=calendar.getTime();


        try {
            // Scheduler 사용을 위한 인스턴스화
            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            Scheduler scheduler = schedulerFactory.getScheduler();






            /**
             * JobDetail 은 Job이 스케줄러에 추가될 때 Quartz Client에 의해 작성 (작업 인스턴스 정의)
             *
             * 또한 Job에 대한 다양한 속성 설정과 JobDataMap을 포함할 수 있으며,
             * JobDataMap은 Job 클래스의 특정 인스턴스에 대한 상태 정보를 저장하는 데 사용
             *     - 작업 인스턴스가 실행될 때 사용하고자 하는 데이터 개체를 원하는 만큼 보유
             *     - Java Map interface를 구현한 것으로 원시 유형의 데이터를 저장하고 검색하기 위한 몇 가지 편의 방법이 추가
             */
            JobDetail jobCrawling = JobBuilder.newJob(Crawling.class)
                    .withIdentity("jobCrawling", "group1")
                    .build();

            JobDetail jobDelAndCreate=JobBuilder.newJob(Delete.class)
                    .withIdentity("delAndCreate", "group2")
                    .build();

            /**
             * Job의 실행을 trigger
             *
             * 작업을 예약하려면 트리거를 인스턴스화하고 해당 속성을 조정하여 예약 요구 사항을 구성
             *
             * - 특정시간 또는 특정 횟수 반복: SimpleTrigger
             * - 주기적 반복: CronTrigger (초 분 시 일 월 요일 연도)
             */

            // CronTrigger
            CronTrigger cronTrigger1 = (CronTrigger) TriggerBuilder.newTrigger()
                    .withIdentity("trggerName1", "cron_trigger_group")
                    .withSchedule(CronScheduleBuilder.cronSchedule("30 0/15 * * * ?")) // 매 15분30초마다 실행
                    //                    .withSchedule(CronScheduleBuilder.cronSchedule("0 0/2 8-17 * * ?")) // 매일 오전 8시에서 오후 5시 사이에 격분마다 실행
                    .build();

            CronTrigger cronTrigger2 =  TriggerBuilder.newTrigger()
                    .withIdentity("trggerName2", "cron_trigger_group")
                    .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 * * ?"))// 매일 자정 마다 실행;
                    .build();


//            Set<Trigger> triggerSet = new HashSet<Trigger>();
//            triggerSet.add(cronTrigger1);
//            triggerSet.add(cronTrigger2);


            scheduler.start();
            scheduler.scheduleJob(jobCrawling,cronTrigger1);
            scheduler.scheduleJob(jobDelAndCreate,cronTrigger2);


        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
