package com.beyondhost.exam.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class CrawlWebPageJob {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(cron = "${jobs.cron}")
    public void crawMeituanWebPage() {

        //System.out.println("任务2,从配置文件加载任务信息，当前时间：" + dateFormat.format(new Date()));
    }
}
