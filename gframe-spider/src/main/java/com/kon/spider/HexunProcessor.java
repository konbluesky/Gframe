package com.kon.spider;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created by konbluesky on 14-6-19.
 */
public class HexunProcessor implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setCharset("gb2312");

    @Override
    public void process(Page page) {
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

        //http://money\.hexun\.com/\w+/\w+.html
        page.addTargetRequests(page.getHtml().links().regex("(http://money\\.hexun\\.com/\\d{4}-\\d{2}-\\d{2}/\\d+.html)").all());

    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {

        Spider.create(new HexunProcessor())
                .addUrl("http://money.hexun.com/")
                .thread(5)
                .run();
    }

}
