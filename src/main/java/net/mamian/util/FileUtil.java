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
     * 从文件读取数据到InputStream中
     *
     * 只要操作is.read即可读取到数据
     * 注意最后is.close()
     *
     * @param pathName
     * @return
     * @throws java.io.IOException
     * */
    public static InputStream readToFileInputStream(String pathName) throws IOException {
        InputStream is = new FileInputStream(pathName);
        return is;
    }

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
