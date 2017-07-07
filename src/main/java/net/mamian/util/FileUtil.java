package net.mamian.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @author mamian
 * @mail mamianskyma@aliyun.com
 * @date 2016-12-7 19:32:28
 * @copyright ©2016 马面 All Rights Reserved
 */
public class FileUtil {

    private static final Logger log = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 获取文件的扩展名
     * 
     * @param pathName
     * @return 
     */
    public static String getExtensionName(String pathName) {
        int index = pathName.lastIndexOf("\\.");
        if (index > 0) {
            return pathName.substring(index);
        }
        return null;
    }

}
