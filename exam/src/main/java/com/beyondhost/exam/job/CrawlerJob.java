package com.beyondhost.exam.job;

import com.beyondhost.exam.dao.OrgInfoDao;
import com.beyondhost.exam.dao.RoomTypeInfoDao;
import com.beyondhost.exam.entity.OrgInfo;
import com.beyondhost.exam.entity.RoomTypeInfo;
import com.beyondhost.exam.service.CrawlerService;
import com.beyondhost.exam.util.CrawlThreadPool;
import com.beyondhost.exam.util.RandomHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

@Component
public class CrawlerJob {

    private static final Logger logger = LoggerFactory.getLogger(CrawlerService.class);
    private final OrgInfoDao _orgInfoDao;
    private final RoomTypeInfoDao _roomTypeInfoDao;

    public CrawlerJob(OrgInfoDao orgInfoDao, RoomTypeInfoDao roomTypeInfoDao)
    {
        this._orgInfoDao = orgInfoDao;
        this._roomTypeInfoDao = roomTypeInfoDao;
    }


    //@Scheduled(cron = "${jobs.cron}")
    public void crawlMeituanWebPage() {
        String[] poiList = CrawlerService.getPoiIdList();
        poiList = Arrays.copyOfRange(poiList, 0, 1);
        for (String poiString:poiList)
        {
            long poiId = Long.valueOf(poiString);
            long sleepSpan = RandomHelper.getRandom(2000,8000);
            try {
                Thread.sleep(sleepSpan);
            } catch (InterruptedException e) {
                logger.error("线程Sleep时发生错误", e);
            }
            CompletableFuture.supplyAsync(()-> CrawlerService.getOrgInfo(poiId))
                    .thenAcceptAsync(info ->  _orgInfoDao.addOrgInfo(info))
                    .exceptionally(throwable -> {
                            logger.error("抓取OrgInfo过程中发生错误", throwable);
                            return null; });

            CompletableFuture.supplyAsync(()-> CrawlerService.getRoomTypeInfo(poiId))
                    .thenAcceptAsync(info ->  _roomTypeInfoDao.addRoomTypeInfo(info))
                    .exceptionally(throwable -> {
                        logger.error("抓取OrgType过程中发生错误", throwable);
                        return null; });

        }
    }

}
