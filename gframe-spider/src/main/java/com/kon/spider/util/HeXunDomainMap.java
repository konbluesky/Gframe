package com.kon.spider.util;

import java.util.HashMap;

/**
 * Created by konbluesky on 14-6-23.
 */
public class HeXunDomainMap {
    private static HashMap<String, String> textmap = new HashMap<String, String>();
    private static HashMap<String, String> codemap = new HashMap<String, String>();
    static{
        textmap.put("stock","股票");
        textmap.put("forex","外汇");
        textmap.put("funds","基金");
        textmap.put("bond","债劵");
        textmap.put("futures","期货");
        textmap.put("qizhi","期指");
        textmap.put("gold","黄金");
        textmap.put("bank","银行");
        textmap.put("money","理财");


        codemap.put("stock","gp");
        codemap.put("forex","wh");
        codemap.put("funds","jj");
        codemap.put("bond","zj");
        codemap.put("futures","qh");
        codemap.put("qizhi","qz");
        codemap.put("gold","hj");
        codemap.put("bank","yh");
        codemap.put("money","lcgs");
    }
    public static HashMap<String, String> getTextmap(){
        return textmap;
    }

    public static HashMap<String, String> getCodemap(){
        return codemap;
    }

}
