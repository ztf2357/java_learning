package com.beyondhost.exam.job;

import com.beyondhost.exam.dao.OrgInfoDao;
import com.beyondhost.exam.dao.RoomTypeInfoDao;
import com.beyondhost.exam.entity.SysConst;
import com.beyondhost.exam.service.CrawlerService;
import com.beyondhost.exam.util.DateTimeHelper;
import com.beyondhost.exam.util.EhcacheHelper;
import com.beyondhost.exam.util.RandomHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

@Component
public class CrawlerJob {

    private static final Logger logger = LoggerFactory.getLogger(CrawlerService.class);
    private static final EhcacheHelper cache = EhcacheHelper.getInstance();
    @Autowired
    private  OrgInfoDao _orgInfoDao;
    @Autowired
    private  RoomTypeInfoDao _roomTypeInfoDao;

    //@Scheduled(cron = "${jobs.cron}")
    public void crawlMeituanWebPage() {
        CrawlerJobInfo jobInfo = new CrawlerJobInfo();
        jobInfo.setJobStartTime(new Date());
        updateJobProcess(jobInfo);

        String[] poiList =  new String[]{"158385112"};//CrawlerService.getPoiIdList();
        List<CompletableFuture<Void>> futureList = new ArrayList<CompletableFuture<Void>>();
        for (String poiString:poiList)
        {
            long poiId = Long.valueOf(poiString);
            CompletableFuture<Void> orgInfoFuture =   CompletableFuture.supplyAsync(()-> CrawlerService.getOrgInfo(poiId))
                    .thenAcceptAsync(info -> {
                            if(info!=null)
                            {
                                _orgInfoDao.addOrgInfo(info);
                                jobInfo.increaseOrgInfoNum();
                                updateJobProcess(jobInfo);
                            }});

            CompletableFuture<Void> roomTypeFuture = CompletableFuture.supplyAsync(()-> CrawlerService.getRoomTypeInfo(poiId))
                    .thenAcceptAsync(info ->{
                                if(info!=null)
                                {
                                    _roomTypeInfoDao.addRoomTypeInfo(info);
                                    jobInfo.increaseRoomTypeInfoNum();
                                    updateJobProcess(jobInfo);
                                }});
            futureList.add(orgInfoFuture);
            futureList.add(roomTypeFuture);

            long sleepSpan = RandomHelper.getRandom(2000,8000);
            try {
                Thread.sleep(sleepSpan);
            } catch (InterruptedException e) {
                logger.error("线程Sleep时发生错误", e);
            }
        }

        CompletableFuture<Void> allFuturesResult =
                CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()]))
                        .thenRun(()->{
                                jobInfo.setJobEndTime(new Date());
                                String dateFormat = "yyyy-MM-dd HH:mm:ss";
                                String formartString = "作业已完成，开始时间：{0},结束时间：{1},已抓取{2}家酒店,{3}个房型信息";
                                String jobResultLog = MessageFormat.format(formartString, DateTimeHelper.format(jobInfo.getJobStartTime(),dateFormat),
                                    DateTimeHelper.format(jobInfo.getJobEndTime(),dateFormat),
                                    String.valueOf(jobInfo.getOrgInfoNum()),String.valueOf(jobInfo.getRoomTypInfoNum()));
                                logger.info(jobResultLog);
                                cache.remove(SysConst.CRAWL_JOB_CACHE_BLOCK_NAME,SysConst.CRAWL_JOB_CACHE_KEY);
                             });
    }


    private static  void updateJobProcess(CrawlerJobInfo jobInfo)
    {
        cache.put(SysConst.CRAWL_JOB_CACHE_BLOCK_NAME,SysConst.CRAWL_JOB_CACHE_KEY,jobInfo);
    }

}
