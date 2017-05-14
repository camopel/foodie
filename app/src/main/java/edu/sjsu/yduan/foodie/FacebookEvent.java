package edu.sjsu.yduan.foodie;

import java.util.ArrayList;

/**
 * Created by yduan on 5/10/2017.
 */

public interface FacebookEvent {
    void onSearchPageComplete(ArrayList<BusinessAIO> alba);
    void onMyFriendsComplate();
    void onPageLikedFriendsComplate(ArrayList<FacebookUser> alfu);
}
