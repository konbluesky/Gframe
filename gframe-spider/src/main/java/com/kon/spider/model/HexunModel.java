package com.kon.spider.model;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;
import us.codecraft.webmagic.pipeline.ConsolePipeline;

/**
 * Created by konbluesky on 14-6-20.
 */
@TargetUrl("http://money.hexun.com/\\d{4}-\\d{2}-\\d{2}/\\d+.html")
@HelpUrl("http://money.hexun.com/")
public class HexunModel extends Model<HexunModel> implements AfterExtractor {
    @ExtractBy(value = "//*[@id=\"artibodyTitle\"]/h1/text()", type = ExtractBy.Type.XPath,notNull = true)
    public String newstitle;

    @ExtractBy(value = "//*[@id=\"artibody\"]/tidyText()", type = ExtractBy.Type.XPath,notNull = true)
    public String content;

    @ExtractByUrl(value="http://money\\.hexun\\.com/(\\d{4}-\\d{2}-\\d{2})/\\d+.html")
    public String pdate;


    @Override
    public void afterProcess(Page page) {
        this.set("newstitle",newstitle);
        this.set("pdate",pdate);
        this.set("source","和讯网");
        this.set("content",content);
        this.set("summary",page.getUrl().toString());
        this.save();
    }

    public static void main(String[] args) {
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
