package com.beyondhost.exam.service;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import com.beyondhost.exam.entity.ConfigKeys;
import com.beyondhost.exam.entity.OrgInfo;
import com.beyondhost.exam.util.ConfigHelper;
import com.beyondhost.exam.util.HttpHelper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CrawlerService  {

    private static final Logger logger = LoggerFactory.getLogger(CrawlerService.class);

    public String getWebPageContent() {
        String urlFomat = ConfigHelper.getValue(ConfigKeys.ORG_INFO_URL_FORMAT);
        String fullUrl = MessageFormat.format(urlFomat,158385112,"2017-12-07","2017-12-08");
        try {
            Connection con = Jsoup.connect("http://hotel.meituan.com/158385112/");
            //浏览器可接受的MIME类型。
            con.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            con.header("Accept-Encoding", "gzip, deflate");
            con.header("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");

            con.header("Connection", "keep-alive");
            con.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0");
            Document document = con.get();
            return document.html();
        }
        catch (Exception e)
        {
            return "";
        }
    }

    public OrgInfo parseWebPageContent() {
        String urlFomat = ConfigHelper.getValue(ConfigKeys.ORG_INFO_URL_FORMAT);
        String fullUrl = MessageFormat.format(urlFomat,158385112,"2017-12-07","2017-12-08");
        String htmlString =  HttpHelper.GetContent(fullUrl);
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
            info.setLatitude(poiData.get("lng").asDouble());

            info.setPoiId(poiData.get("poiid").asLong());
            info.setOrgName(poiData.get("name").asText());

            Iterator<JsonNode> poiExtendsInfos = node.get("poiExt").get("hotelIntroInfo").get("poiExtendsInfos").elements();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
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

        }catch(IOException ex)
        {
            logger.error("解析html json出错",ex);
        }
        return info;
    }
}
