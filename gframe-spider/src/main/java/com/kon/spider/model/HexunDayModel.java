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
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.kon.spider.util.HeXunDomainMap;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;
import us.codecraft.webmagic.pipeline.ConsolePipeline;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by konbluesky on 14-6-20.
 */
//@TargetUrl("http://money.hexun.com/\\d{4}-\\d{2}-\\d{2}/(\\d+)(_\\d|\\d).html")
@TargetUrl("http://\\w+.hexun.com/\\d{4}-\\d{2}-\\d{2}/(\\d+).html")
@HelpUrl("http://www.hexun.com/")
public class HexunDayModel extends Model<HexunDayModel> implements AfterExtractor {
    public static HexunDayModel dao=new HexunDayModel();
    @ExtractBy(value = "//*[@id=\"artibodyTitle\"]/h1/text()", type = ExtractBy.Type.XPath,notNull = true)
    public String newstitle;

    @ExtractBy(value = "//*[@id=\"artibody\"]/tidyText()", type = ExtractBy.Type.XPath,notNull = true)
    public String content;

    @ExtractByUrl(value="http://\\w+\\.hexun\\.com/(\\d{4}-\\d{2}-\\d{2})/(\\d+)(_\\d|\\d).html",notNull = true)
    public String pdate;

    @ExtractByUrl(value="http://\\w+\\.hexun\\.com/\\d{4}-\\d{2}-\\d{2}/((\\d+)(_\\d|\\d)).html",notNull = true)
    public String originid;

    @ExtractBy(value = "//*[@id=\"pubtime_baidu\"]/text()", type = ExtractBy.Type.XPath,notNull = true)
    public String ndate;

    @ExtractBy(value="//*[@id=\"source_baidu\"]/a/text()", type = ExtractBy.Type.XPath,notNull = true)
    public String source;

    @ExtractBy(value="name=\"keywords\" content=\"(.*?)\"",type = ExtractBy.Type.Regex,notNull = true)
    public String meta_keyword;

    @ExtractBy(value="name=\"description\" content=\"(.*?)\"",type = ExtractBy.Type.Regex,notNull = true)
    public String meta_description;
    /**
     * 过滤方法
     */
    public void filter(){
        //过滤内容中的<http://*****>
        content=content.replaceAll("<.*?>", "");
    }
    @Override
    public void afterProcess(Page page) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String nowdate=sdf.format(new Date());
        if(!StringUtils.equals(nowdate,pdate)) return ;

        String stype=page.getUrl().regex("http://(\\w+).hexun.com").toString();
        if(stype==null || stype.length()==0) return;

        String code=HeXunDomainMap.getCodemap().get(stype);
        if(code ==null || code.length()==0) return;
        //过滤内容中的url
        filter();
        this.set("newid",originid);
        this.set("ntype",code);
        this.set("newstitle",newstitle);
        this.set("pdate",pdate);
        this.set("createdate",ndate);
        this.set("source",source);
        this.set("content",content);
        this.set("summary",content.length()>40?content.substring(0,50):content);
        this.set("editor","peopleim");
        this.set("liveflag",1);
        this.set("isspider",1);
        this.save();

        //保存到爬取记录中
        Record spider_recoder=new Record();
        spider_recoder.set("localid",this.get("id"));
        spider_recoder.set("ntype",code);
        spider_recoder.set("title",newstitle);
        spider_recoder.set("url",page.getUrl().toString());
        spider_recoder.set("originid",originid);
        spider_recoder.set("catchdate",new Date());
        Db.save("spider_recoder",spider_recoder);

    }

    public static void main(String[] args) {
//        C3p0Plugin c3p0Plugin = new C3p0Plugin("jdbc:mysql://127.0.0.1/jiren?characterEncoding=utf-8", "root", "root");
        C3p0Plugin c3p0Plugin = new C3p0Plugin("jdbc:mysql://210.209.73.47/jiren?characterEncoding=utf-8", "root", "gongwei911");
        c3p0Plugin.start();
        ActiveRecordPlugin activeRecordPlugin = new ActiveRecordPlugin(c3p0Plugin);
        activeRecordPlugin.addMapping("news_normal", HexunDayModel.class);
        activeRecordPlugin.start();
        //启动webmagic

        OOSpider.create(
                    Site.me().setCharset("gb2312"),
                    HexunDayModel.class)
                .addUrl("http://www.hexun.com/")
                .addPipeline(new ConsolePipeline())
                .run();
    }


}
