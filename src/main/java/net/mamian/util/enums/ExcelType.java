/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.mamian.util.enums;

/**
 * 
 *
 * @author mamian
 * @mail mamianskyma@aliyun.com
 * @date 2016-12-9 0:09:17
 * @copyright ©2016 马面 All Rights Reserved
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */
public enum ExcelType implements BaseEnum {
    
    XLSX("xlsx"),
    XLS("xls");
    
    
    
    private final String msg;
    
    ExcelType(String msg){
        this.msg = msg;
    }

    @Override
    public String getMsg() {
        return msg;
    }
    
}
