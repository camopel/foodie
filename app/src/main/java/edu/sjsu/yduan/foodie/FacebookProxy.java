package edu.sjsu.yduan.foodie;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.yelp.fusion.client.models.Business;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by yduan on 5/10/2017.
 */
public class FacebookProxy {
    private ArrayList<FacebookUser> PageLikedFBUser;
    private ArrayList<BusinessAIO> ALBA;
    private FacebookEvent fbEvent;
    public FacebookUser user;
    private FacebookProxy(){
        ALBA=new ArrayList<>();
        PageLikedFBUser=new ArrayList<>();
    }
    private static FacebookProxy instance = null;
    public void GetUserProfile(){
        if(fbEvent==null) return;
        if(user!=null || AccessToken.getCurrentAccessToken()==null) {
            fbEvent.onProfileComplete(user);
            return;
        }
        user=new FacebookUser();
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,id,picture.type(large){url},email");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        if(response.getError()==null) {
                            try{
                                JSONObject resp = response.getJSONObject();
                                user.setName(resp.getString("name"));
                                user.setID(resp.getString("id"));
                                user.setEmail(resp.getString("email"));
                                user.setImgUrl(resp.getJSONObject("picture").getJSONObject("data").getString("url"));
                                fbEvent.onProfileComplete(user);
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                        }
                    }
                }
        ).executeAsync();
    }
    public static FacebookProxy getInstance() {
        if(instance == null)  instance = new FacebookProxy();
        return instance;
    }
    public static void close() {
        if(instance!=null) {
            instance.ALBA.clear();
            instance.ALBA=null;
            instance.user=null;
            instance.PageLikedFBUser.clear();
            instance.PageLikedFBUser=null;
            instance=null;
        }

    }
    public void SetEvent(FacebookEvent e){
        this.fbEvent = e;
    }
    public void SearchPage(ArrayList<Business> businesses){
        ALBA.clear();
        ArrayList<GraphRequest> algr = new ArrayList<>();
        for(int i=0;i<businesses.size();i++){
            Business b = businesses.get(i);
            String q = b.getName()+","+b.getLocation().getZipCode();
            Bundle parameters = new Bundle();
            parameters.putString("q", q);
            parameters.putString("type", "page");
            parameters.putString("fields", "name,picture.type(large){url},fan_count,overall_star_rating,location{street},phone");
            GraphRequest gr = new GraphRequest(AccessToken.getCurrentAccessToken(),"/search",parameters,HttpMethod.GET);
            gr.setCallback(new GraphRequest.Callback() {
                public void onCompleted(GraphResponse response) {
                    onPageResponseHandler(response);
                }
            });
            BusinessAIO ba = new BusinessAIO();
            ba.business=b;
            ba.facebookPage=new FacebookPage();
            ALBA.add(ba);
            gr.setTag(i);
            algr.add(gr);
        }
        GraphRequestBatch grb = new GraphRequestBatch(algr);
        grb.addCallback(new GraphRequestBatch.Callback(){
            public void onBatchCompleted(GraphRequestBatch batchReq){
                fbEvent.onSearchPageComplete(ALBA);
            }
        });
        grb.executeAsync();
    }
    public void GetMyFriends(@Nullable GraphRequest req){
        if(req==null){
            Util.FriendsAndMe.clear();
            Bundle parameters = new Bundle();
            parameters.putString("fields", "friends{id,name,picture.type(large){url}},name,id,picture{url}");
            req=new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me",
                parameters,
                HttpMethod.GET
            );
        }
        req.setCallback(new GraphRequest.Callback() {
            public void onCompleted(GraphResponse response) {
                onMyFriendResponseHandler(response);
            }
        });
        req.executeAsync();
    }
    public void GetPageLikedFriends(String pageID){
        PageLikedFBUser.clear();
        ArrayList<GraphRequest> algr = new ArrayList<>();
        for(FacebookUser fu : Util.FriendsAndMe){
            String page = "/"+fu.getID()+"/likes/"+pageID;
            GraphRequest gr = new GraphRequest(AccessToken.getCurrentAccessToken(),page,null,HttpMethod.GET);
            gr.setCallback(new GraphRequest.Callback() {
                public void onCompleted(GraphResponse response) {
                    onPageLikeResponseHandler(response);
                }
            });
            gr.setTag(fu);
            algr.add(gr);
        }
        GraphRequestBatch grb = new GraphRequestBatch(algr);
        grb.addCallback(new GraphRequestBatch.Callback(){
            public void onBatchCompleted(GraphRequestBatch batchReq){
                fbEvent.onPageLikedFriendsComplate(PageLikedFBUser);
            }
        });
        grb.executeAsync();
    }
    private void onMyFriendResponseHandler(GraphResponse response) {
        try{
            if(response.getError()==null) {
                JSONObject resp = response.getJSONObject();
                if(resp.has("id")){
                    FacebookUser fu = new FacebookUser();
                    fu.setID(resp.getString("id"));
                    fu.setName(resp.getString("name"));
                    fu.setImgUrl(resp.getJSONObject("picture").getJSONObject("data").getString("url"));
                    Util.FriendsAndMe.add(fu);
                }
                if(resp.has("friends")) resp = resp.getJSONObject("friends");
                if(resp.has("data")){
                    JSONArray jsa = resp.getJSONArray("data");
                    for(int i=0;i<jsa.length();i++) {
                        JSONObject jo = jsa.getJSONObject(i);
                        if(jo.has("id")){
                            FacebookUser fu = new FacebookUser();
                            fu.setID(jo.getString("id"));
                            fu.setName(jo.getString("name"));
                            fu.setImgUrl(jo.getJSONObject("picture").getJSONObject("data").getString("url"));
                            Util.FriendsAndMe.add(fu);
                        }
                    }
                }
                GraphRequest requestNext = response.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);
                if(requestNext!=null)
                    GetMyFriends(requestNext);
                else
                    fbEvent.onMyFriendsComplate();
            }
        }catch (JSONException ex){
            ex.printStackTrace();
        }
    }
    private void onPageResponseHandler(GraphResponse response){
        try {
            if (response.getError() == null) {
                int idx = (int)response.getRequest().getTag();
                Business biz = ALBA.get(idx).business;
                FacebookPage fp = ALBA.get(idx).facebookPage;
                JSONObject resp = response.getJSONObject();
                JSONArray jsa = resp.getJSONArray("data");
                String street = biz.getLocation().getAddress1().trim().toLowerCase();
                String phone = biz.getPhone();
                for (int i = 0; i < jsa.length(); i++) {
                    JSONObject jo = jsa.getJSONObject(i);
                    String fbphone = jo.has("phone") ? jo.getString("phone").replaceAll("[^0-9]", "") : "N/A";
                    String fbstreet = jo.has("location") ? jo.getJSONObject("location").getString("street").trim().toLowerCase() : "N/A";
                    if (jsa.length() == 1 || phone.contains(fbphone) || fbstreet.contains(street)) {
                        if(jo.has("id")) fp.setPageID(jo.getString("id"));
                        if(jo.has("fan_count"))fp.setLikes(jo.getLong("fan_count"));
//                        if(jo.has("overall_star_rating"))fp.setRating(jo.getDouble("overall_star_rating"));
//                        if (jo.has("picture")) {
//                            jo = jo.getJSONObject("picture");
//                            if (jo.has("data")) {
//                                jo = jo.getJSONObject("data");
//                                if (jo.has("url")) fp.setImgUrl(jo.getString("url"));
//                            }
//                        }
                        break;
                    }
                }
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }
    private void onPageLikeResponseHandler(GraphResponse response){
        try {
            if (response.getError() == null) {
                JSONObject resp = response.getJSONObject();
                JSONArray jsa = resp.getJSONArray("data");
                if(jsa.length()>0) PageLikedFBUser.add((FacebookUser) response.getRequest().getTag());
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }
}
