package com.dylee.newszine.object;

/**
 * Created by 1000128 on 15. 11. 9..
 */

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HuffNews implements Comparable<HuffNews> {


    static SimpleDateFormat FORMATTER =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
    static SimpleDateFormat FORMATTER_KR =
            new SimpleDateFormat("yyyy년 MM월 dd일 HH시mm분ss초, E");

    private String Caption;
    private String Message;
    private String source;
    private String Description;
    private String Picture;
    private URL DestinationURL;
    private URL IconURL;
    private Date mCDate;
    private Date mUDate;


    public String getMessage() {
        if (Message != null)
            return Message;
        else return "Click Here";
    }

    public String getCaption() {
        return Caption;
    }

    public String getDescription() {
        if (Description != null)
            return Description;
        else return "No Desc";
    }

    public String getPictureURL() {
        return Picture;
    }


    public URL getDestinationURL() {
        return DestinationURL;
    }

    public String getDestinationURLASString() {
        return DestinationURL.toString();
    }

    public URL getIconURL() {
        return IconURL;
    }

    public void setMessage(String msg) {
        this.Message = msg;
    }

    public void setCaption(String Caption) {
        this.Caption = Caption;
    }

    public void setDescription(String Description) {
        if (Description != null)
            this.Description = Description;
        else this.Description = "No Desc";
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setPictureURL(String url) {
        try {
            if (url != null && url.trim().length() > 0) {
                this.Picture = url;
            } else {
                url = null;
            }
        } catch (Exception e) {
            url = null;
        }
    }

    ;

    public void setDestinationURL(String url) {
        try {
            if (url != null && url.trim().length() > 0) {
                this.DestinationURL = new URL(url);
            }
        } catch (MalformedURLException e) {
            this.DestinationURL = null;
//            throw new RuntimeException(e);
        }
    }

    ;

    public void setIconURL(String url) {
        try {
            if (url != null && url.trim().length() > 0) {
                this.IconURL = new URL(url);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    ;

    public void setCDate(String date) {

        while (!date.endsWith("00")) {
            date += "0";
        }
        try {
            this.mCDate = FORMATTER.parse(date.trim());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public void setUDate(String date) {

        while (!date.endsWith("00")) {
            date += "0";
        }
        try {
            this.mUDate = FORMATTER.parse(date.trim());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    ;

    public String getSource(String source) {
        if (this.source != null) return this.source;
        else return " ";

    }

    public Date getCDate() {
        if (this.mCDate != null)
            return this.mCDate;
        else {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());
            String strDate = "2014-01-29 13:30";
            try {
                return dateFormat.parse(strDate);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public Date getUDate() {
        if (this.mUDate != null)
            return this.mUDate;
        else {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());
            String strDate = "2014-01-29 13:30";
            try {
                return dateFormat.parse(strDate);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public int compareTo(HuffNews another) {
        if (another == null) return 1;
        return another.Message.compareTo(this.Message);
    }

    public HuffNews copy() {

        HuffNews Hnews = new HuffNews();
        Hnews.Message = this.Message;
        Hnews.Caption = this.Caption;
        Hnews.Description = this.Description;
        Hnews.Picture = this.Picture;
        Hnews.DestinationURL = this.DestinationURL;
        Hnews.IconURL = this.IconURL;

        return Hnews;
    }
}