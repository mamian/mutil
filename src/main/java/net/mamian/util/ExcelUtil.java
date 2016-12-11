package net.mamian.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.mamian.util.enums.ExcelType;

/**
 * Excel文件的读写
 *
 * @author mamian
 * @mail mamianskyma@aliyun.com
 * @date 2016-12-7 18:33:30
 * @copyright ©2016 马面 All Rights Reserved
 */
public class ExcelUtil {
    private static final Logger log = LoggerFactory.getLogger(ExcelUtil.class);
    
    /**
     * excel类型
     * 
     * @param fileName
     * @return 
     */
    private static ExcelType excelType(String fileName) {
        if(fileName.toLowerCase().endsWith(".xlsx")){
            return ExcelType.XLSX;
        } else if (fileName.toLowerCase().endsWith(".xls")){
            return ExcelType.XLS;
        }
        return null;
    }
}
