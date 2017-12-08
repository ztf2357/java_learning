package com.beyondhost.exam.util;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.nio.charset.Charset;

public final class HttpHelper {
    private static final Logger logger = LoggerFactory.getLogger(HttpHelper.class);
    public static String GetContent(String url)
    {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader(HttpHeaders.HOST,"hotel.meituan.com");
            httpGet.setHeader(HttpHeaders.CONNECTION,"keep-alive");
            httpGet.setHeader(HttpHeaders.ACCEPT,"*/*");
            httpGet.setHeader(HttpHeaders.USER_AGENT,"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36");
            httpGet.setHeader(HttpHeaders.ACCEPT_ENCODING,"gzip, deflate, br");
            httpGet.setHeader(HttpHeaders.ACCEPT_LANGUAGE,"zh-CN,zh;q=0.9");
            httpGet.setHeader("Cookie","IJSESSIONID=lfx31zhoz4pl1ef1ahcfs2tvy; iuuid=58041698C0D5DD2D72F152B3E335416E4AD943311069BA2FD6EA87E91F3EFD86; latlng=\"31.127014,121.360474,1512731376105\"; ci=10; cityname=%E4%B8%8A%E6%B5%B7; _lxsdk_cuid=16035d0fb6dc8-05e87db276b425-5a442916-1fa400-16035d0fb6dc8; uuid=3edfba9b0cd54d688b8e.1512731376.1.0.0; hotel_city_id=1; hotel_city_info=%7B%22id%22%3A1%2C%22name%22%3A%22%E5%8C%97%E4%BA%AC%22%2C%22pinyin%22%3A%22beijing%22%7D; __mta=141332749.1512731377911.1512737823621.1512737904579.7; _lxsdk_s=160362c58ee-de3-2d0-8b%7C%7C8");

            ResponseHandler<String> responseHandler = (response) -> {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= HttpStatus.SC_OK && status < HttpStatus.SC_MULTIPLE_CHOICES) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity, Charset.forName("utf-8")) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                };
            String responseBody = httpclient.execute(httpGet, responseHandler);
            return responseBody;
        }
        catch (IOException ex)
        {
            logger.error("网络请求出错",ex);
            return null;
        }
        finally {
            try {
                httpclient.close();
            }
            catch (Exception ignored)
            {
                logger.error("释放网络连接出错",ignored);
            }
        }
    }
}
