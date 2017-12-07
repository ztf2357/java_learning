package com.beyondhost.exam.util;
import java.io.*;
import java.util.*;
import org.slf4j.*;

public final class ConfigHelper {
        private static final Logger logger = LoggerFactory.getLogger(ConfigHelper.class);
        private static Properties config = null;
        private static String configFileFullPath = "application.properties";
        private static void Init(String filePath) {
            config = new Properties();
            try {
                ClassLoader classLoader =ConfigHelper.class.getClassLoader();
                InputStream in;
                if (classLoader != null) {
                    in = classLoader.getResourceAsStream(filePath);
                }else {
                    in = ClassLoader.getSystemResourceAsStream(filePath);
                }
                config.load(in);
                in.close();
            } catch (FileNotFoundException e) {
                logger.error("服务器配置文件没有找到",e);
            } catch (Exception e) {
                logger.error("服务器配置信息读取错误",e);
            }
        }

        public static String getValue(String key) {
            if(config==null) Init(configFileFullPath);

            if (config.containsKey(key)) {
                String value = config.getProperty(key);
                return value;
            }else {
                return "";
            }
        }

        public static int getValueInt(String key) {
            if(config==null) Init(configFileFullPath);
            String value = getValue(key);
            int valueInt = 0;
            try {
                valueInt = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                logger.error("类型转换出错",e);
                return valueInt;
            }
            return valueInt;
        }
    }