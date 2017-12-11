package com.beyondhost.exam.controller;

import com.beyondhost.exam.entity.SysConst;
import com.beyondhost.exam.job.CrawlerJob;
import com.beyondhost.exam.job.CrawlerJobInfo;
import com.beyondhost.exam.service.CrawlerService;
import com.beyondhost.exam.util.DateTimeHelper;
import com.beyondhost.exam.util.EhcacheHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;

@RestController
@RequestMapping("/job/")
public class JobController {

    @Autowired
    private CrawlerJob _job;
    private static final EhcacheHelper cache = EhcacheHelper.getInstance();


    @GetMapping("index")
    public void index() {
          _job.crawlMeituanWebPage();
    }


    @GetMapping("process")
    public String getJobProcess()
    {
       Object cachedObj = cache.get(SysConst.CRAWL_JOB_CACHE_BLOCK_NAME,SysConst.CRAWL_JOB_CACHE_KEY);
       if(cachedObj!=null)
       {
           String dateFormat ="yyyy-MM-dd HH:mm:ss";
           CrawlerJobInfo jobInfo = (CrawlerJobInfo)cachedObj;
           if(jobInfo.getJobEndTime()==null)
           {
               String formartString = "作业正在进行中，开始时间：{0},已抓取{1}家酒店,{2}个房型信息";
               return MessageFormat.format(formartString, DateTimeHelper.format(jobInfo.getJobStartTime(),dateFormat),
                      String.valueOf(jobInfo.getOrgInfoNum()),String.valueOf(jobInfo.getRoomTypInfoNum()));
           }
           String formartString = "作业已完成，开始时间：{0},结束时间：{1},已抓取{2}家酒店,{3}个房型信息";
           return MessageFormat.format(formartString, DateTimeHelper.format(jobInfo.getJobStartTime(),dateFormat),
                   DateTimeHelper.format(jobInfo.getJobEndTime(),dateFormat),
                   String.valueOf(jobInfo.getOrgInfoNum()),String.valueOf(jobInfo.getRoomTypInfoNum()));
       }
        return "未查到作业信息！新的作业还未开始运行";
    }
}
