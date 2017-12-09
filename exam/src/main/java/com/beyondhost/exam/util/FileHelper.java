package com.beyondhost.exam.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public final class FileHelper {

    private static final Logger logger = LoggerFactory.getLogger(ConfigHelper.class);
    public static String readTxtFile(String path)
    {
        StringBuilder sb = new StringBuilder();
        FileInputStream inputStream = null;
        Scanner sc = null;
        try {
            inputStream = new FileInputStream(path);
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                sb.append(line);
                sb.append(" ");
            }
        }
        catch (IOException ex)
        {
            logger.error("读取文件时出现异常",ex);
        }
        finally {
            if (inputStream != null) {
                try{
                    inputStream.close();
                }catch(Exception ex){
                    logger.error("关闭文件流时出现异常",ex);
                }
            }
            if (sc != null) {
                sc.close();
            }
        }
        return  sb.toString().trim();
    }
}
