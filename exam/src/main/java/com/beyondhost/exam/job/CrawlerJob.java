package com.beyondhost.exam.job;

import com.beyondhost.exam.dao.OrgInfoDao;
import com.beyondhost.exam.dao.RoomTypeInfoDao;
import com.beyondhost.exam.entity.SysConst;
import com.beyondhost.exam.service.CrawlerService;
import com.beyondhost.exam.util.EhcacheHelper;
import com.beyondhost.exam.util.RandomHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

@Component
public class CrawlerJob {

    private static final Logger logger = LoggerFactory.getLogger(CrawlerService.class);
    private final OrgInfoDao _orgInfoDao;
    private final RoomTypeInfoDao _roomTypeInfoDao;
    private static final EhcacheHelper cache = EhcacheHelper.getInstance();

    public CrawlerJob(OrgInfoDao orgInfoDao, RoomTypeInfoDao roomTypeInfoDao)
    {
        this._orgInfoDao = orgInfoDao;
        this._roomTypeInfoDao = roomTypeInfoDao;
    }


    //@Scheduled(cron = "${jobs.cron}")
    public void crawlMeituanWebPage() {
        CrawlerJobInfo jobInfo = new CrawlerJobInfo();
        jobInfo.setJobStartTime(new Date());
        updateJobProcess(jobInfo);

        String[] poiList = CrawlerService.getPoiIdList();
        poiList = Arrays.copyOfRange(poiList, 0, 1);
        List<CompletableFuture<Void>> futureList = new ArrayList<CompletableFuture<Void>>();
        for (String poiString:poiList)
        {
            long poiId = Long.valueOf(poiString);
            long sleepSpan = RandomHelper.getRandom(2000,8000);
            try {
                Thread.sleep(sleepSpan);
            } catch (InterruptedException e) {
                logger.error("线程Sleep时发生错误", e);
            }
            CompletableFuture<Void> orgInfoFuture =   CompletableFuture.supplyAsync(()-> CrawlerService.getOrgInfo(poiId))
                    .thenAcceptAsync(info -> { _orgInfoDao.addOrgInfo(info); })
                    .thenRunAsync(()-> { jobInfo.increaseOrgInfoNum(); updateJobProcess(jobInfo);});

            CompletableFuture<Void> roomTypeFuture = CompletableFuture.supplyAsync(()-> CrawlerService.getRoomTypeInfo(poiId))
                    .thenAcceptAsync(info ->  _roomTypeInfoDao.addRoomTypeInfo(info))
                    .thenRunAsync(()-> {jobInfo.increaseRoomTypeInfoNum();updateJobProcess(jobInfo);});
            futureList.add(orgInfoFuture);
            futureList.add(roomTypeFuture);
        }

        CompletableFuture<Void> allFuturesResult =
                CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()]))
                        .thenRun(()->{ jobInfo.setJobEndTime(new Date());//TODO 写日志，移除缓存});


    }


    private static synchronized void updateJobProcess(CrawlerJobInfo jobInfo)
    {
        cache.put(SysConst.CRAWL_JOB_CACHE_BLOCK_NAME,SysConst.CRAWL_JOB_CACHE_KEY,jobInfo);
    }

}
