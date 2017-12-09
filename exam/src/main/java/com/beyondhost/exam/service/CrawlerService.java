package com.beyondhost.exam.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.beyondhost.exam.entity.ConfigKeys;
import com.beyondhost.exam.entity.OrgInfo;
import com.beyondhost.exam.entity.SysConst;
import com.beyondhost.exam.util.ConfigHelper;
import com.beyondhost.exam.util.FileHelper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CrawlerService  {

    private static final Logger logger = LoggerFactory.getLogger(CrawlerService.class);

    public static OrgInfo getOrgInfo(long poiId) {
        String htmlString =getWebPageContent(poiId);
        return parseWebContent(htmlString);
    }

    private static String getWebPageContent(long poiId) {
        String urlFomat = ConfigHelper.getValue(ConfigKeys.ORG_INFO_URL_FORMAT);
        String fullUrl = MessageFormat.format(urlFomat,String.valueOf(poiId));
        try {
            Connection con = Jsoup.connect(fullUrl);
            con.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            con.header("Accept-Encoding", "gzip, deflate");
            con.header("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");

            con.header("Connection", "keep-alive");
            con.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0");
            Connection.Response res = con.method(Connection.Method.GET).execute();
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

    private static OrgInfo parseWebContent(String htmlString) {
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
}
