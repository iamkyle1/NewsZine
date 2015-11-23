package com.dylee.newszine.parser;
/**
 * Created by 1000128 on 15. 9. 8..
 */
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
public abstract class BaseNewsFeedParser {

    final URL feedUrl;
    protected BaseNewsFeedParser(String feedUrl){
        try {
            this.feedUrl = new URL(feedUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    protected InputStream getInputStream() {
        try {
//                Log.i("intext", "fetch itmes inpurstream");
            return feedUrl.openConnection().getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


