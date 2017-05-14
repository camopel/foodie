package edu.sjsu.yduan.foodie;

import java.io.Serializable;

/**
 * Created by yduan on 5/12/2017.
 */

public class FacebookUser implements Serializable {
    public String getImgUrl() {
        return this.ImgUrl;
    }
    public void setImgUrl(String s) {
        this.ImgUrl = s;
    }
    String ImgUrl;

    public String getID() {
        return this.ID;
    }
    public void setID(String s) {
        this.ID = s;
    }
    String ID;

    public String getName() {
        return this.Name;
    }
    public void setName(String s) {
        this.Name = s;
    }
    String Name;
}
