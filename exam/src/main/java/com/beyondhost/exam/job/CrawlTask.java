package com.beyondhost.exam.job;

import com.beyondhost.exam.entity.OrgInfo;
import com.beyondhost.exam.service.CrawlerService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Callable;

public class CrawlTask implements Callable<OrgInfo> {

    private final long _poiId;

    public CrawlTask(long poiId)
    {
        this._poiId = poiId;
    }

    @Override
    public OrgInfo call() throws Exception {
            Thread.sleep(1000);
            return CrawlerService.getOrgInfo(_poiId);
    }
}
