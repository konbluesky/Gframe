package com.kon.spider;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created by konbluesky on 14-6-19.
 */
public class HexunProcessor implements PageProcessor {
    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setCharset("gb2312");

    @Override
    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {
        // 部分二：定义如何抽取页面信息，并保存下来
//        page.putField("author", page.getUrl().regex("https://github\\.com/(\\w+)/.*").toString());
//        page.putField("name", page.getHtml().xpath("//h1[@class='entry-title public']/strong/a/text()").toString());
//        if (page.getResultItems().get("name") == null) {
//            //skip this page
//            page.setSkip(true);
//        }

        page.putField("title", page.getHtml().xpath("//*[@id=\"artibodyTitle\"]/h1/text()"));
        if(page.getResultItems().get("title")==null){
            page.setSkip(true);
        }
        page.putField("publishtime",page.getHtml().xpath("//*[@id=\"artibody\"]/tidyText()"));

        // 部分三：从页面发现后续的url地址来≤抓取
        //http://money\.hexun\.com/\w+/\w+.html
        page.addTargetRequests(page.getHtml().links().regex("(http://money\\.hexun\\.com/\\d{4}-\\d{2}-\\d{2}/\\d+.html)").all());

    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {

        Spider.create(new HexunProcessor())
                //从"https://github.com/code4craft"开始抓
                .addUrl("http://money.hexun.com/")
                        //开启5个线程抓取
                .thread(5)
                        //启动爬虫
                .run();
    }

}
