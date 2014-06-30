/*******************************************************************************
 * Copyright (c) 2014. konbluesky  (blackjackhoho@gmail.com)
 * Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 ******************************************************************************/

package com.kon.spider.model;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;
import us.codecraft.webmagic.pipeline.ConsolePipeline;

import javax.sound.midi.SysexMessage;

/**
 * Created by konbluesky
 * Date : 14-6-29 下午4:11
 * Project : simple.jiren
 */
@TargetUrl("http://\\w+.hexun.com/\\d{4}-\\d{2}-\\d{2}/(\\d+).html")
@HelpUrl("http://www.hexun.com/")
public class TestModel implements AfterExtractor {
    @ExtractBy(value="name=\"keywords\" content=\"(.*?)\"",type = ExtractBy.Type.Regex,notNull = true)
    public String meta_keyword;
    @ExtractBy(value="name=\"description\" content=\"(.*?)\"",type = ExtractBy.Type.Regex,notNull = true)
    public String meta_description;
    @Override
    public void afterProcess(Page page) {
        System.out.println(meta_keyword);
    }
    public static void main(String[] args){
//        C3p0Plugin c3p0Plugin = new C3p0Plugin("jdbc:mysql://127.0.0.1/jiren?characterEncoding=utf-8", "root", "root");
//        c3p0Plugin.start();
//        ActiveRecordPlugin activeRecordPlugin = new ActiveRecordPlugin(c3p0Plugin);
//        activeRecordPlugin.addMapping("news_normal", HexunDayModel.class);
//        activeRecordPlugin.start();
        //启动webmagic

        OOSpider.create(
                Site.me().setCharset("gb2312"),
                TestModel.class)
                .addUrl("http://www.hexun.com/")
                .addPipeline(new ConsolePipeline())
                .run();

    }
}
