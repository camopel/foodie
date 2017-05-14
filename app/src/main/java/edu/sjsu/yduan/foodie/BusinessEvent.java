package edu.sjsu.yduan.foodie;

import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.Reviews;

import java.util.ArrayList;

/**
 * Created by yduan on 5/9/2017.
 */

public interface BusinessEvent {
    void onYelpProxyConnected();
    void onYelpSearchComplete(ArrayList<Business> businesses);
    void onYelpProxyError(int err);
    void onYelpBusinessComplete(Business business);
    void onYelpReviewsComplete(Reviews rvs);
}
