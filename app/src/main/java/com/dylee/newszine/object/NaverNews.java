package com.dylee.newszine.object;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;

public class NaverNews implements Comparable<NaverNews> {

    public static final String CHANNEL = "channel";
    public static final String PUB_DATE = "pubDate";
    public static final String DESCRIPTION = "description";
    public static final String LINK = "link";
    public static final String ORIGINAL_LINK = "originallink";
    public static final String TITLE = "title";
    public static final String ITEM = "item";

    static SimpleDateFormat FORMATTER =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
    static SimpleDateFormat FORMATTER_KR =
            new SimpleDateFormat("yyyy년 MM월 dd일 HH시mm분ss초, E");
    private String title;
    private URL link;
    private URL originalLink;
    private String description;
    private String date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public URL getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLink(String link) {
        try {
            if (link != null && link.trim().length() > 0) {
                this.link = new URL(link);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }


    public URL getOriginalLink() {
        return originalLink;
    }


    public void setOriginalLink(String originalLink) {
        try {
            if (originalLink != null && originalLink.trim().length() > 0) {
                this.originalLink = new URL(originalLink);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getDate() {
        return this.date;
//        FORMATTER_KR.format(this.date);
    }

    public void setDate(String date) {
        Log.i("dateweired ", date);
        this.date = date;
//        while (!date.endsWith("00")){
//            date += "0";
//        }
//        try {
//            this.date = FORMATTER.parse(date.trim());
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }
    }

    public int compareTo(NaverNews another) {
        if (another == null) return 1;
        return another.date.compareTo(date);
    }

    public NaverNews copy() {

        NaverNews news = new NaverNews();
        news.date = this.date;
        news.description = this.description;
        news.link = this.link;
        news.originalLink = this.originalLink;
        news.title = this.title;
        return news;
    }
}



/*
public class NaverNewItem {

    // 검색 결과 header
    public String sTitle;//검색 결과 제목
    public String sLink;//검색사이트 main domain
    public int nTotal;
    public int nDisplay; //검색된 결과의 수
    public java.util.List<eachItem> aEachItems;

    //검색결과의 상세 항목

    public class eachItem {
        public String sItemTitle;
        public String sItemOriginallink;
        public String sItemLink;
        public String sItemDescription;
    }

    final List<eachItem> aDataSetList = new ArrayList<eachItem>();

    public void addAll(List<eachItem> items) {
        aDataSetList.addAll(items);
    }
}
*/

    /*

<title>Cheonan food expo draws 53 foreign firms</title>
<originallink>
http://www.koreatimes.co.kr/www/news/nation/2013/06/116_138059.html
</originallink>
<link>
http://openapi.naver.com/l AAABWIyQ6DIBgGn+bnaCrU7cDBre/B8hESU0sRNbx96WQOk/meiFnSOtMoaJr/0U80LCzlALnjPtiGLE2tn49WC251AUDjuBKttaYuv3PMRzjpUwokRuKv4gEVja92dSFW5vP+AW4sCndqAAAA
</link>
<description>
“The foreign firms are expected to display their diverse food products and provide visitors with the opportunity to <b>sample</b> them, making the expo a stage to grasp the recent trend of well...
</description>
<pubDate>Mon, 24 Jun 2013 19:14:00 +0900</pubDate>

<rss version="2.0">
<channel>
<title>Naver Open API - news ::'sample'</title>
<link>http://search.naver.com</link>
<description>Naver Search Result</description>
<lastBuildDate>Tue, 25 Jun 2013 16:07:52 +0900</lastBuildDate>
<total>3033</total>
<start>1</start>
<display>10</display>
<item>
<title>Cheonan food expo draws 53 foreign firms</title>
<originallink>
http://www.koreatimes.co.kr/www/news/nation/2013/06/116_138059.html
</originallink>
<link>
http://openapi.naver.com/l AAABWIyQ6DIBgGn+bnaCrU7cDBre/B8hESU0sRNbx96WQOk/meiFnSOtMoaJr/0U80LCzlALnjPtiGLE2tn49WC251AUDjuBKttaYuv3PMRzjpUwokRuKv4gEVja92dSFW5vP+AW4sCndqAAAA
</link>
<description>
“The foreign firms are expected to display their diverse food products and provide visitors with the opportunity to <b>sample</b> them, making the expo a stage to grasp the recent trend of well...
</description>
<pubDate>Mon, 24 Jun 2013 19:14:00 +0900</pubDate>
</item>
<item>
<title>FSS to limit bank CEOs' paychecks</title>
<originallink>
http://www.koreatimes.co.kr/www/news/biz/2013/06/488_138036.html
</originallink>
<link>
http://openapi.naver.com/l?AAABWIyQ6DIBgGn+bnaCrU7cDBre/B8hESU0sRNbx96WQOk/meiFnSOtMoaJr/0U80LCzlALnjPtiGLE2tn49WC251AUDjuBKttaYuv3PMRzjpUwokRuKv4gEVja92dSFW5vP+AW4sCndqAAAA
</link>
<description>
Our <b>sample</b> survey showed that some financial firms are operating questionable salary systems for executives and directors,” an FSS official said. “We’ve decided to launch a full...
</description>
<pubDate>Mon, 24 Jun 2013 17:29:00 +0900</pubDate>
</item>
        ....
</channel>
</rss>
*/

//000	System error
// 010	Your query request count is over the limit
//011	Incorrect query request
//020	Unregistered key
//021	Your key is temporary unavailable
//100	Invalid target value
//101	Invalid display value
//102	Invalid start value
//110	Undefined sort value
//200	Reserved
//900	Undefined error occured