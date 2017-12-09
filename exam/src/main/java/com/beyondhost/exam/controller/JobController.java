package com.beyondhost.exam.controller;

import com.beyondhost.exam.dao.OrgInfoDao;
import com.beyondhost.exam.entity.OrgInfo;
import com.beyondhost.exam.job.CrawlWebPageJob;
import com.beyondhost.exam.service.CrawlerService;
import javafx.beans.binding.ObjectExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/job/")
public class JobController {

    private final CrawlWebPageJob _service;

    @Autowired
    public JobController(CrawlWebPageJob service) {
        this._service = service;
    }


    @GetMapping("index")
    public Object index() {
         return _service.crawMeituanWebPage();
    }
}
