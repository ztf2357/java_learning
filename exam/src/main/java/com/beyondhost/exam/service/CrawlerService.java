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

    private static Map<String,String> getCookies() {
        Map<String,String> result = new HashMap<>();
        result.put("IJSESSIONID","lfx31zhoz4pl1ef1ahcfs2tvy");
        result.put("iuuid","58041698C0D5DD2D72F152B3E335416E4AD943311069BA2FD6EA87E91F3EFD86");
        result.put("latlng","31.127014,121.360474,1512731376105");
        result.put("ci","10");
        result.put("cityname","%E4%B8%8A%E6%B5%B7");
        result.put("_lxsdk_cuid","16035d0fb6dc8-05e87db276b425-5a442916-1fa400-16035d0fb6dc8");
        result.put("uuid","3edfba9b0cd54d688b8e.1512731376.1.0.0");
        result.put("_lxsdk_s","1603fc10093-60c-7c1-55c%7C%7C3");
        return result;
    }
    public static String getRoomTypeInfoPageContent(long poiId) {
        String urlFormat = ConfigHelper.getValue(ConfigKeys.ROOM_TYPE_URL_FORMAT);
        Date todayZeroTime = SysConst.TODAY_ZERO_TIME();
        Date tomorrowZeroTime = DateTimeHelper.addDays(todayZeroTime,1);
        String fullUrl = MessageFormat.format(urlFormat,String.valueOf(poiId),String.valueOf(todayZeroTime.getTime()),String.valueOf(tomorrowZeroTime.getTime()));

        fullUrl+="&uuid=58041698C0D5DD2D72F152B3E335416E4AD943311069BA2FD6EA87E91F3EFD86";
        fullUrl+= "&_token=eJxVis1ugkAURt9lthCZyzDCsEOYKtr6A7agjQurVEAcqYBWm757h9QumtzkOzn3fKGTv0U2YIwpqKiuJFMgmFpUJzoxVbT55wxguoreTi8esl8lY5UZbNWaQIpfA9jCK/WPDcm6Ia+tfBmhtK5LW9PSY50UnUOS1c1adDbHgwbUIhYF0DWkIiT7w7ztAbpMtSiTEoBRlRHcPvftU+76vvV9q2wnkI2S4aXI8mqSus6zM2HZLBKJofF69DDb6RE9L/h1f+Tu+JItTQVIXnjTeRkBjIYDx91xYo7zW5es+zmf783R6TFaWgPP+5xqgTn0owZublQ3QUxEkZ6SOA53vFfcZkGSVAqjKS8G1/3sGlv+4tyLuNt3q2bqHGGbPnnlIu+F2dgoGRaieF82pAxDkSgfqXLhVRWeBfr+AT1HdYw=";

        try {
            Connection con = Jsoup.connect(fullUrl);
            con.header("Accept", "application/json, */*");
            con.header("Accept-Encoding", "gzip, deflate, br");
            con.header("Accept-Language", "zh-CN,zh;q=0.9");
            con.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36");
            con.header("Host","ihotel.meituan.com");
            con.header("Origin","http://hotel.meituan.com");
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
            Map<String,String> cookies = res.cookies();
            return document.html();
        }
        catch (Exception ex)
        {
            logger.error("读取网页内容时出错",ex);
            return null;
        }
    }

    private static OrgInfo parseOrgInfoContent(String htmlString) {
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
            return null;
        }
    }

    public static List<RoomTypeInfo> getRoomTypeInfo(long poiId) {
       String jsonString = getRoomTypeInfoPageContent(poiId);
       return parseRoomTypeInfoContent(jsonString);
    }

    private static List<RoomTypeInfo> parseRoomTypeInfoContent(String jsonString) {
        List<RoomTypeInfo> resultList = new ArrayList<RoomTypeInfo>();
        RoomTypeInfo info = new RoomTypeInfo();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode node = mapper.readTree(jsonString);
            Iterator<JsonNode> poiData = node.get("mergeList").get("data").elements();
            while(poiData.hasNext())
            {
                JsonNode extAttr = poiData.next();
                Iterator<JsonNode> prepayGoods = extAttr.get("aggregateGoods").elements();
                while(prepayGoods.hasNext()) {
                    JsonNode goodsNode = prepayGoods.next();
                    JsonNode prepayGoodNode = goodsNode.get("prepayGood");
                    JsonNode goodsRoomModelNode =  prepayGoodNode.get("goodsRoomModel");

                    info.setPoiId(goodsRoomModelNode.get("poiId").asLong());
                    info.setRoomId(goodsRoomModelNode.get("roomId").asLong());
                    info.setPartnerId(goodsRoomModelNode.get("partnerId").asLong());
                    info.setRoomName(goodsRoomModelNode.get("roomName").asText());
                    info.setOriginalPrice(prepayGoodNode.get("originalPrice").asInt());

                    resultList.add(info);
                }
            }
            return resultList;
        }catch(IOException ex)
        {
            logger.error("解析 json出错",ex);
            return  null;
        }
    }
}
