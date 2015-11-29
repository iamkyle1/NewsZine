package com.dylee.newszine.parser;


import android.util.Log;
import android.util.Xml;

import com.dylee.newszine.object.NaverNews;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;


public class XMLPullNewsFeedParser extends BaseNewsFeedParser {
    public XMLPullNewsFeedParser(String feedUrl) {
        super(feedUrl);
//        Log.i("intext initialize", feedUrl);
    }

    public ArrayList<NaverNews> fetchItems() {
        ArrayList<NaverNews> NewsList = null;
        XmlPullParser parser = Xml.newPullParser();
//        Log.i("intext", "fetch items");

        try {
            // auto-detect the encoding from the stream
            parser.setInput(this.getInputStream(), null);
            int eventType = parser.getEventType();
            NaverNews currentNews = null;

            boolean done = false;

//            Log.i("intext", "fetch itmes try");

            while (eventType != XmlPullParser.END_DOCUMENT && !done) {
                String name = null;
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        NewsList = new ArrayList<>();
//                        Log.i("intext", "fetch itmes start document");
                        break;
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
//                        Log.i("intext", "fetch itmes start tag        " + name);
                        if (name.equalsIgnoreCase(NaverNews.ITEM)) {
                            currentNews = new NaverNews();
//                            Log.i("intext", "fetch itmes start tag/item---------" + name);
                        } else if (currentNews != null) {
                            if (name.equalsIgnoreCase(NaverNews.TITLE)) {
                                currentNews.setTitle(parser.nextText());
//                                currentNews.setTitle(" test");
                                Log.i("intext", "fetch itmes title");
                            } else if (name.equalsIgnoreCase(NaverNews.ORIGINAL_LINK)) {
                                currentNews.setOriginalLink(parser.nextText());
//                                currentNews.setOriginalLink(" test");
//                                Log.i("intext", "fetch itmes start tag/Ori-Link "+parser.nextText());
                            } else if (name.equalsIgnoreCase(NaverNews.LINK)) {
                                currentNews.setLink(parser.nextText());
//                                currentNews.setLink(" test");
//                                Log.i("intext", "fetch itmes start tag/Link "+parser.nextText());
                            } else if (name.equalsIgnoreCase(NaverNews.DESCRIPTION)) {
                                currentNews.setDescription(parser.nextText());
//                                currentNews.setDescription(" test");
//                                Log.i("intext", "fetch itmes start tag/DESC"+parser.nextText());
                            } else if (name.equalsIgnoreCase(NaverNews.PUB_DATE)) {
                                currentNews.setDate(parser.nextText());
//                                currentNews.setDate(" test");
//                                Log.i("intext", "fetch itmes pub_date"+parser.nextText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = parser.getName();
//                        Log.i("intext", "fetch itmes end_tag");
                        if (name.equalsIgnoreCase(NaverNews.ITEM) && currentNews != null) {
                            NewsList.add(currentNews);
//                            Log.i("intext", "print " + currentNews.getTitle());
                        } else if (name.equalsIgnoreCase(NaverNews.CHANNEL)) {
//                            Log.i("intext", "fetch itmes end_tag new end_channle");
                            done = true;
                        }
                        break;
                }
                eventType = parser.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return NewsList;
    }
}