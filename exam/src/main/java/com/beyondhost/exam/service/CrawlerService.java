package com.beyondhost.exam.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.beyondhost.exam.entity.ConfigKeys;
import com.beyondhost.exam.entity.OrgInfo;
import com.beyondhost.exam.entity.RoomTypeInfo;
import com.beyondhost.exam.entity.SysConst;
import com.beyondhost.exam.util.ConfigHelper;
import com.beyondhost.exam.util.DateTimeHelper;
import com.beyondhost.exam.util.FileHelper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

@Component
public class CrawlerService  {

    private static final int TIMEOUT_IN_MS = 5000;
    private static final Logger logger = LoggerFactory.getLogger(CrawlerService.class);

    public static String getRoomTypeInfoPageContent(long poiId) {
        String urlFormat = ConfigHelper.getValue(ConfigKeys.ROOM_TYPE_URL_FORMAT);
        Date todayZeroTime = SysConst.TODAY_ZERO_TIME();
        Date tomorrowZeroTime = DateTimeHelper.addDays(todayZeroTime,1);
        String fullUrl = MessageFormat.format(urlFormat,String.valueOf(poiId),String.valueOf(todayZeroTime.getTime()),String.valueOf(tomorrowZeroTime.getTime()));

        fullUrl+="&uuid=58041698C0D5DD2D72F152B3E335416E4AD943311069BA2FD6EA87E91F3EFD86";
        fullUrl+= "&_token=eJxNjV1vgjAYRv9LbyXSUopA4oUCfkAUdd10WbwAxMIAgYqCLvvvq5lLlrzJeXJykvcL8PkBmAhCSJAEmrPYBCkG0TA2VKRIIPrvVAQHhgRC/mYD8wMZCpQM1dg/zEaIX4OgDvfS31bFVlRxj2ouIpA0TWXKclI2cd4v4rS5BKd+VBYyIjrWCUKKDCQARF9Q0QtmTwZPNk+eU3YCJojdNk+547Pb6HW0kifsMNW7bqnbo7bOZpTPOkanm2RdGlZ2NOQAnxJMD902bUuX+dOZvwrhZQypB9cV515EdlqWBZ8yxdkRBl7pRjrhdV70SOfwbYHSemKHO2ZV5EZ5FY3zzd11tPyqLzGjTuxYy3rB2s7vqZNBeLfWtqqheLG7v1zPivjkN0WvSuJ328MLTtrhEHz/AO1Mc2g=";

        try {
            Connection con = Jsoup.connect(fullUrl);
            con.header("Accept", "application/json, text/plain, */*");
            con.header("Accept-Encoding", "gzip, deflate, br");
            con.header("Accept-Language", "zh-CN,zh;q=0.9");
            con.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36");
            con.header("Host","ihotel.meituan.com");
            Connection.Response res = con.method(Connection.Method.GET).ignoreContentType(true).timeout(TIMEOUT_IN_MS).execute();
            String document = res.body();
            return document;
        }
        catch (Exception ex)
        {
            logger.error("读取网页内容时出错",ex);
            return ex.toString();
        }
    }

    public static OrgInfo getOrgInfo(long poiId) {
        String htmlString = getOrgInfoPageContent(poiId);
        return parseOrgInfoContent(htmlString);
    }

    private static String getOrgInfoPageContent(long poiId) {
        String urlFomat = ConfigHelper.getValue(ConfigKeys.ORG_INFO_URL_FORMAT);
        String fullUrl = MessageFormat.format(urlFomat,String.valueOf(poiId));
        try {
            Connection con = Jsoup.connect(fullUrl);
            con.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            con.header("Accept-Encoding", "gzip, deflate");
            con.header("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
            con.header("Connection", "keep-alive");
            con.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0");

            Connection.Response res = con.method(Connection.Method.GET).timeout(TIMEOUT_IN_MS).execute();
            Document document = res.parse();
            return document.html();
        }
        catch (Exception ex)
        {
            logger.error("读取网页内容时出错",ex);
            return null;
        }
    }

    private static OrgInfo parseOrgInfoContent(String htmlString) {
        if(htmlString==null) return null;
        Document document = Jsoup.parse(htmlString);
        String  scriptText =  document.getElementsByTag("script").first().html();
        String tempString =  scriptText.replace("window.__INITIAL_STATE__=","");
        String jsonString = tempString.substring(0,tempString.length()-1);
        ObjectMapper mapper = new ObjectMapper();
        OrgInfo info = new OrgInfo();
        try {
            JsonNode node = mapper.readTree(jsonString);
            JsonNode poiData = node.get("poiData");
            info.setPhone(poiData.get("phone").asText());
            info.setAddress(poiData.get("addr").asText());
            info.setCityId(poiData.get("cityId").asInt());
            info.setLatitude(poiData.get("lat").asDouble());
            info.setLongitude(poiData.get("lng").asDouble());

            info.setPoiId(poiData.get("poiid").asLong());
            info.setOrgName(poiData.get("name").asText());

            Iterator<JsonNode> poiExtendsInfos = node.get("poiExt").get("hotelIntroInfo").get("poiExtendsInfos").elements();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            while(poiExtendsInfos.hasNext())
            {
                JsonNode extAttr = poiExtendsInfos.next();
                int attrId = extAttr.get("attrId").asInt();
                if(attrId==101120)
                {
                    String yearString = extAttr.get("attrValue").asText().replace("年","");
                    try {
                        Date openDate = sdf.parse(yearString + "-01-01");
                        info.setOpenTime(openDate);
                    }
                    catch (java.text.ParseException ex)
                    {
                        logger.error("解析html格式化日期出错",ex);
                    }
                }
                if(attrId==101122)
                {
                   String  roomCountString =  extAttr.get("attrValue").asText().replace("间","");
                   info.setRoomCount(Integer.parseInt(roomCountString));
                }
            }
            if(info.getOpenTime()==null)
            {
                info.setOpenTime(SysConst.DEFAULT_DATE());
            }
            return info;
        }catch(IOException ex)
        {
            logger.error("解析html json出错",ex);
            return  null;
        }
    }

    public static String[] getPoiIdList() {
        try {
            URL uri = org.springframework.util.ResourceUtils.getURL("classpath:poifile/poi.txt");
            String content = FileHelper.readTxtFile(uri.getPath());
            return content.split(" ");
        }catch (FileNotFoundException ex)
        {
            logger.error("poi文件未能读取",ex);
            return new String[]{};
        }
    }

    public static RoomTypeInfo getRoomTypeInfo(long poiId) {
       String jsonString = getRoomTypeInfoPageContent(poiId);
       return parseRoomTypeInfoContent(jsonString);
    }

    private static RoomTypeInfo parseRoomTypeInfoContent(String jsonString) {
        if(jsonString==null) return null;
        RoomTypeInfo info = new RoomTypeInfo();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode node = mapper.readTree(jsonString);
            Iterator<JsonNode> poiData = node.get("mergeList").get("data").elements();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            while(poiData.hasNext())
            {
                JsonNode extAttr = poiData.next();
                JsonNode goodsNode = extAttr.get("aggregateGoods").get("prepayGood");
                JsonNode goodsRoomModelNode = goodsNode.get("goodsRoomModel");
                info.setPoiId(goodsRoomModelNode.get("poiId").asLong());
                info.setRoomId(goodsRoomModelNode.get("roomId").asLong());
                info.setPartnerId(goodsRoomModelNode.get("partnerId").asLong());
                info.setRoomName(goodsRoomModelNode.get("roomName").asText());
                info.setOriginalPrice(goodsRoomModelNode.get("originalPrice").asInt());

            }
            return info;
        }catch(IOException ex)
        {
            logger.error("解析 json出错",ex);
            return  null;
        }
    }
}
