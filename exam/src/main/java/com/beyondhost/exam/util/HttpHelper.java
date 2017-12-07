package com.beyondhost.exam.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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
            HttpGet httpget = new HttpGet(url);
            ResponseHandler<String> responseHandler = (response) -> {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= HttpStatus.SC_OK && status < HttpStatus.SC_MULTIPLE_CHOICES) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity, Charset.forName("utf-8")) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                };
            String responseBody = httpclient.execute(httpget, responseHandler);
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
