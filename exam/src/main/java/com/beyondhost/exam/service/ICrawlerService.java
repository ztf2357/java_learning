package com.beyondhost.exam.service;

import java.util.regex.Pattern;

public interface ICrawlerService {
    String getWebPageContent(String url, Pattern pattern);
}
