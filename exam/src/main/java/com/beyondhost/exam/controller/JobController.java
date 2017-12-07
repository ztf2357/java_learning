package com.beyondhost.exam.controller;

import com.beyondhost.exam.dao.OrgInfoDao;
import com.beyondhost.exam.entity.OrgInfo;
import com.beyondhost.exam.service.CrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/job/")
public class JobController {

    private final CrawlerService _service;
    @Autowired
    public JobController(CrawlerService service) {
        this._service = service;
    }


    @GetMapping("index")
    public String index() {
        return _service.getWebPageContent();
    }
}
