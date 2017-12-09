package com.beyondhost.exam.job;

import com.beyondhost.exam.dao.OrgInfoDao;
import com.beyondhost.exam.entity.OrgInfo;
import com.beyondhost.exam.service.CrawlerService;
import com.beyondhost.exam.util.CrawlThreadPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.*;

@Component
public class CrawlWebPageJob {

    private final OrgInfoDao _orgInfoDao;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public CrawlWebPageJob(OrgInfoDao orgInfoDao)
    {
        this._orgInfoDao = orgInfoDao;
    }


    //@Scheduled(cron = "${jobs.cron}")
    public ArrayList<OrgInfo> crawMeituanWebPage() {

        String[] poiList = CrawlerService.getPoiIdList();
        poiList = Arrays.copyOfRange(poiList, 0, 2);
        int taskSize = poiList.length;
        CrawlThreadPool executor = new CrawlThreadPool();
        CompletionService<OrgInfo> completionService = new ExecutorCompletionService<OrgInfo>(executor.getThreadPoolExecutor());
        for (String poiString:poiList)
        {
            long poiId = Long.valueOf(poiString);
            completionService.submit(new CrawlTask(poiId));
        }

        ArrayList<OrgInfo> result = new ArrayList<>();
        for (int i = 0; i < taskSize; i++)
        {
            try
            {
                OrgInfo info = completionService.take().get();
                result.add(info);
                _orgInfoDao.addOrgInfo(info);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            catch (ExecutionException e)
            {
                e.printStackTrace();
            }
        }
        executor.destory();

        return result;
    }
}
