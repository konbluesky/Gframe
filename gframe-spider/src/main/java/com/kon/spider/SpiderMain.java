package com.kon.spider;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.kon.spider.model.HexunModel;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;

import java.util.List;

/**
 * Created by konbluesky on 14-6-23.
 */
public class SpiderMain {
    public static void main(String[] args){
        C3p0Plugin c3p0Plugin = new C3p0Plugin("jdbc:mysql://127.0.0.1/jiren?characterEncoding=utf-8", "root", "root");
        c3p0Plugin.start();
        ActiveRecordPlugin activeRecordPlugin = new ActiveRecordPlugin(c3p0Plugin);
        activeRecordPlugin.addMapping("news_normal", HexunModel.class);
        activeRecordPlugin.start();
        //启动webmagic

//        OOSpider.create(
//                Site.me().setCharset("gb2312"),
//                HexunModel.class)
//                .addUrl("http://money.hexun.com/")
//                .addPipeline(new ConsolePipeline())
//                .run();

//        List<HexunModel> hms=HexunModel.dao.find("select * from news_normal");
//        for(HexunModel h:hms){
//            String content=h.get("content");
//            System.out.println(content);
//            content=content.replaceAll("<.*?>","").replaceAll("href=\"(.*?)\"","").replaceAll("(http://.*?\\.html)","");
//            System.out.println(content);
//            if(content.length()>40){
//                String ss=h.get("summary");
//                ss.replaceAll("href=\"(.*?)\"","").replaceAll("(http.*?\\.html)","");
//                h.set("summary",ss);
////                h.set("summary",content.substring(0,40));
//            }
//            h.set("content",content);
//            h.update();
//        }
        String a="asdfasdf http://forex.hexun.com/2014-06-17/165767614.htmlssss";
       a= a.replaceAll("(http.*?\\.html)","");
        a= a.replaceAll("(\\s)","");
        System.out.println(a);
    }
}
