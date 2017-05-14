package edu.sjsu.yduan.foodie;

import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;
import com.yelp.fusion.client.models.AccessToken;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.Reviews;
import com.yelp.fusion.client.models.SearchResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yduan on 5/9/2017.
 */

public class BusinessProxy {
    private YelpFusionApiFactory apiFactory;
    private YelpFusionApi yelpFusionApi;
    private BusinessEvent event;
    private final int SearchBusinessError=10001;
    private final int APICreateError=10000;
    private final int BusinessError=10002;
    private final int ReviewsError=10003;
    private static String yelp_id;
    private static String yelp_secret;
    private static BusinessProxy instance=null;
    private BusinessProxy(){}
    public static BusinessProxy getInstance() {
        if(instance == null)  instance = new BusinessProxy();
        return instance;
    }
    public static void Initial(String id, String secret){
        yelp_id=id;
        yelp_secret=secret;
    }
    public void SetEvent(BusinessEvent e){
        event = e;
    }
    public void SetupProxy(){
        try{
            apiFactory = new YelpFusionApiFactory();
            Call<AccessToken> call = apiFactory.getAccessToken(yelp_id,yelp_secret);
            Callback<AccessToken> cb = new Callback<AccessToken>() {
                @Override
                public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                    AccessToken token = response.body();
                    apiFactory.setToken(token);
                    yelpFusionApi=apiFactory.createAPI();
                    event.onYelpProxyConnected();
                }
                @Override
                public void onFailure(Call<AccessToken> call, Throwable t) {
                    event.onYelpProxyError(APICreateError);
                }
            };
            call.enqueue(cb);
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void SearchBusiness(double latitude, double longtitude){
        SearchBusiness(latitude, longtitude,"",0,"distance",true);
    }
    public void SearchBusiness(double latitude, double longtitude,String term){
        SearchBusiness(latitude, longtitude,term,0,"best_match",false);
    }
    public void SearchBusiness(double latitude, double longtitude,String term,int offset,String sortby,boolean openNow){
        Map<String, String> params = new HashMap<>();
        params.put("categories", "restaurants");
        params.put("latitude", String.valueOf(latitude));//"40.581140"
        params.put("longitude", String.valueOf(longtitude));//"-111.914184"
        params.put("limit", "10");
        params.put("sort_by", sortby);
        if(openNow) params.put("open_now", "true");
        params.put("term",term);
//        params.put("radius","40000");
        params.put("offset",String.valueOf(offset));
        Call<SearchResponse> call = yelpFusionApi.getBusinessSearch(params);
        Callback<SearchResponse> cb = new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                SearchResponse searchResponse = response.body();
//                int totalNumberOfResult = searchResponse.getTotal();
                event.onYelpSearchComplete(searchResponse.getBusinesses());
            }
            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                event.onYelpProxyError(SearchBusinessError);
            }
        };
        call.enqueue(cb);
    }
    static public String parseError(int err){
        if(err==10000) return "API Connection Error";
        else if(err==10001) return "Business Search Error";
        else if(err==10002) return "Business Error";
        else if(err==10003) return "Reviews Error";
        return "Unknown error";
    }

    public void BusinessDetail(String id){
        Call<Business> call = yelpFusionApi.getBusiness(id);
        Callback<Business> cb = new Callback<Business>() {
            @Override
            public void onResponse(Call<Business> call, Response<Business> response) {
                Business res = response.body();
                event.onYelpBusinessComplete(res);
            }
            @Override
            public void onFailure(Call<Business> call, Throwable t) {
                event.onYelpProxyError(BusinessError);
            }
        };
        call.enqueue(cb);
    }
    public void Reviews(String id){
        Call<Reviews> call = yelpFusionApi.getBusinessReviews(id,"en_US");
        Callback<Reviews> cb = new Callback<Reviews>() {
            @Override
            public void onResponse(Call<Reviews> call, Response<Reviews> response) {
                Reviews res = response.body();
                event.onYelpReviewsComplete(res);
            }
            @Override
            public void onFailure(Call<Reviews> call, Throwable t) {
                event.onYelpProxyError(ReviewsError);
            }
        };
        call.enqueue(cb);
    }
}
