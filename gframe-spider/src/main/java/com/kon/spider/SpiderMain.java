package com.kon.spider;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.kon.spider.model.HexunModel;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;

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

        OOSpider.create(
                Site.me().setCharset("gb2312"),
                HexunModel.class)
                .addUrl("http://money.hexun.com/")
                .addPipeline(new ConsolePipeline())
                .run();
    }
}
