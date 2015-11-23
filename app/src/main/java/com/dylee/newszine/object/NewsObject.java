package com.dylee.newszine.object;

/**
 * Created by 1000128 on 15. 10. 4..
 */
public class NewsObject {

    int imgResource;
    String landingURL;
    String title;

    public NewsObject setValue(String li, int ir, String tittle) {
        this.imgResource = ir;
        this.landingURL = li;
        this.title = tittle;
        return this;
    }

    public int getImgResource() {
        return imgResource;
    }

    public String getURL() {
        return landingURL;
    }

    public String getTitle() {
        return title;
    }

    ;

}
