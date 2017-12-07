package com.beyondhost.exam.service;

import java.text.MessageFormat;
import java.util.regex.Pattern;

import com.beyondhost.exam.entity.ConfigKeys;
import com.beyondhost.exam.util.ConfigHelper;
import com.beyondhost.exam.util.HttpHelper;
import org.springframework.stereotype.Component;

@Component
public class CrawlerService  {

    public String getWebPageContent() {
        String urlFomat = ConfigHelper.getValue(ConfigKeys.ORG_INFO_URL_FORMAT);
        String fullUrl = MessageFormat.format(urlFomat,158385112,"2017-12-07","2017-12-07");
        return HttpHelper.GetContent(fullUrl);

    }
}
