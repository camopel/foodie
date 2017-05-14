package edu.sjsu.yduan.foodie;

import java.io.Serializable;

/**
 * Created by yduan on 5/10/2017.
 */

public class FacebookPage implements Serializable {
    public long getLikes() {
        return this.Likes;
    }
    public void setLikes(long i) {
        this.Likes = i;
    }
    long Likes;

    public String getPageID() {
        return this.PageID;
    }
    public void setPageID(String s) {
        this.PageID = s;
    }
    String PageID;

//    public String getImgUrl() {
//        return this.ImgUrl;
//    }
//    public void setImgUrl(String s) {
//        this.ImgUrl = s;
//    }
//    String ImgUrl;
//    public double getRating() {
//        return this.Rating;
//    }
//    public void setRating(double d) {
//        this.Rating = d;
//    }
//    double Rating;


}
