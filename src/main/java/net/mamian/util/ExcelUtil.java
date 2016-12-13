package net.mamian.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import net.mamian.util.enums.ExcelType;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

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
    
    /** 数字格式化 */
    private static NumberFormat format = NumberFormat.getInstance();
    
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
    
    /**
     * 读取excel文件
     * 
     * @param excelPath
     * @param skipRows 跳过几行
     * @param columnCount 共多少列
     * @return 
     * @throws java.io.FileNotFoundException 
     */
    public static List<String[]> readExcel(String excelPath, int skipRows, int columnCount) throws FileNotFoundException, IOException{
        log.info("【读取Excel】excelPath = {},skipRows = {},columnCount = {}", excelPath, skipRows, columnCount);

        if (StringUtils.isBlank(excelPath)) {
            log.warn("【参数excelPath为空】");
            return new ArrayList<>();
        }

        FileInputStream is = new FileInputStream(new File(excelPath));
        POIFSFileSystem fs = new POIFSFileSystem(is);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        List<String[]> list = new ArrayList<>();
        HSSFSheet sheet = wb.getSheetAt(0);
        // 得到总共的行数
        int rowNum = sheet.getPhysicalNumberOfRows();
        try {
            for (int i = skipRows; i < rowNum; i++) {
                String[] vals = new String[columnCount];
                HSSFRow row = sheet.getRow(i);
                if (null == row) {
                    continue;
                }
                for (int j = 0; j < columnCount; j++) {
                    HSSFCell cell = row.getCell(j);
                    String val = getStringCellValue(cell);
                    vals[j] = val;
                }
                list.add(vals);
            }
        } catch (Exception e) {
            log.error("【Excel解析失败】", e);
            throw new RuntimeException("Excel解析失败");
        } finally {
            wb.close();
        }
        return list;
    }
    
    /**
     * 获取单元格数据内容为字符串类型的数据
     * @param cell Excel单元格{@link HSSFCell}
     * @return 单元格数据内容（可能是布尔类型等，强制转换成String）
     */
    private static String getStringCellValue(HSSFCell cell) {
        if (cell == null) {
            return "";
        }
        String strCell = "";
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:
                strCell = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                strCell = String.valueOf(format.format(cell.getNumericCellValue()))
                    .replace(",", "");
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                strCell = String.valueOf(cell.getBooleanCellValue());
                break;
            case HSSFCell.CELL_TYPE_BLANK:
                strCell = "";
                break;
            default:
                strCell = "";
                break;
        }
        if (StringUtils.isBlank(strCell)) {
            return "";
        }

        return strCell;
    }
    
}
