package com.beyondhost.exam.controller;

import com.beyondhost.exam.job.CrawlerJob;
import com.beyondhost.exam.service.CrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/job/")
public class JobController {

    private final CrawlerJob _service;
    private final CrawlerService _cservice;

    @Autowired
    public JobController(CrawlerJob service, CrawlerService crawlerService) {
        this._service = service;
        this._cservice = crawlerService;
    }


    @GetMapping("index")
    public void index() {
          _service.crawlMeituanWebPage();
    }

    @GetMapping("")
    public Object test() {
        return _cservice.getRoomTypeInfoPageContent(158385112);
    }
}
