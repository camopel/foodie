package edu.sjsu.yduan.foodie;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.LikeView;
import com.facebook.share.widget.ShareButton;
import com.squareup.picasso.Picasso;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.Reviews;

import java.util.ArrayList;
import java.util.Locale;

public class RestaurantActivity extends AppCompatActivity implements BusinessEvent,FacebookEvent{
    private LikeView likeView;
    private ShareButton shareButton;
    private String fPageID;
    private Business business;
    private LinearLayout biz_detail_like_layout;
    private ImageView biz_detail_img1;
    private ImageView biz_detail_img2;
    private ImageView biz_detail_img3;
    private ImageView biz_fav;
    private RecyclerView biz_review_listview;
    private String page;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);
        setTitle("Restaurant Detail");
        Intent intent = getIntent();
        fPageID=intent.getStringExtra("fpageid");
        business=(Business) intent.getSerializableExtra("biz");

        likeView = (LikeView) findViewById(R.id.like_view);
        shareButton = (ShareButton)findViewById(R.id.share_button);
        biz_detail_like_layout = (LinearLayout)findViewById(R.id.biz_detail_like_layout);
        TextView biz_detail_name = (TextView)findViewById(R.id.biz_detail_name);
        biz_detail_name.setText(business.getName());
        TextView biz_detail_address = (TextView)findViewById(R.id.biz_detail_address);
        biz_detail_address.setText(Util.Join(business.getLocation().getDisplayAddress()));
        TextView biz_detail_phone = (TextView)findViewById(R.id.biz_detail_phone);
        biz_detail_phone.setText(business.getPhone());
        TextView biz_detail_distance = (TextView)findViewById(R.id.biz_detail_distance);
        biz_detail_distance.setText(String.format(Locale.ENGLISH,"%.1f",(business.getDistance()/1609.34)));
        TextView biz_detail_price = (TextView)findViewById(R.id.biz_detail_price);
        biz_detail_price.setText(business.getPrice());
        biz_detail_img1 = (ImageView)findViewById(R.id.biz_detail_img1);
        biz_detail_img2 = (ImageView)findViewById(R.id.biz_detail_img2);
        biz_detail_img3 = (ImageView)findViewById(R.id.biz_detail_img3);
        biz_fav = (ImageView)findViewById(R.id.biz_fav);
        biz_fav.setTag(false);
        biz_review_listview = (RecyclerView)findViewById(R.id.biz_review_listview);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        biz_review_listview.setLayoutManager(mLinearLayoutManager);
        biz_fav.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                boolean fav = (boolean)v.getTag();
                if(!fav){
                    biz_fav.setImageResource(R.drawable.ic_fav);
                    biz_fav.setTag(true);
                }else{
                    biz_fav.setImageResource(R.drawable.ic_fav_gray);
                    biz_fav.setTag(false);
                }
            }
        });
        if (fPageID!=null&&fPageID.length()>0){
            page=getString(R.string.facebook)+fPageID;
            likeView.setObjectIdAndType(page,LikeView.ObjectType.PAGE);
            likeView.setLikeViewStyle(LikeView.Style.BOX_COUNT);
            likeView.setAuxiliaryViewPosition(LikeView.AuxiliaryViewPosition.INLINE);
            likeView.setHorizontalAlignment(LikeView.HorizontalAlignment.LEFT);

            ShareLinkContent shareContent = new ShareLinkContent.Builder().setContentUrl(Uri.parse(page)).build();
            shareButton.setShareContent(shareContent);
        }
        else{
            biz_detail_like_layout.setVisibility(View.GONE);
        }

    }
    @Override
    public void onStart() {
        super.onStart();
        BusinessProxy bizproxy =BusinessProxy.getInstance();
        bizproxy.SetEvent(this);
        bizproxy.BusinessDetail(business.getId());
        bizproxy.Reviews(business.getId());
    }
    @Override
    public void onResume(){
        super.onResume();
        findFBFriendsLikePage();
    }
    private void findFBFriendsLikePage(){
        if (fPageID!=null&&fPageID.length()>0){
            int count=biz_detail_like_layout.getChildCount()-2;
            while(count-->0)biz_detail_like_layout.removeViewAt(0);

            FacebookProxy fproxy = FacebookProxy.getInstance();
            fproxy.SetEvent(this);
            fproxy.GetPageLikedFriends(fPageID);
        }
    }
    public void onYelpProxyConnected(){}
    public void onYelpProxyError(int err){}
    public void onYelpSearchComplete(ArrayList<Business> businesses){}
    public void onSearchPageComplete(ArrayList<BusinessAIO> alba){}
    public void onMyFriendsComplate(){}
    public void onPageLikedFriendsComplate(ArrayList<FacebookUser> alfu){
        final int size=58;
        for(FacebookUser fu:alfu){
            ImageView iv = new ImageView(biz_detail_like_layout.getContext());
            iv.setLayoutParams(new LinearLayout.LayoutParams(size,size));
            iv.setTag(fu.getID());
            iv.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    ImageView ivv = (ImageView)v;
                    Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.facebook)+String.valueOf(ivv.getTag())));
                    v.getContext().startActivity(intent);
//                    findFBFriendsLikePage();
                }
            });
            biz_detail_like_layout.addView(iv,0);
            Picasso.with(iv.getContext()).load(fu.getImgUrl()).resize(size, size).into(iv);
        }

    }
    public void onProfileComplete(FacebookUser fu){}
    public void onYelpBusinessComplete(Business business){
        ArrayList<String> photos = business.getPhotos();
        int size = 110;
        Picasso.with(biz_detail_img1.getContext()).load(photos.get(0)).resize(size, size).into(biz_detail_img1);
        Picasso.with(biz_detail_img2.getContext()).load(photos.get(1)).resize(size, size).into(biz_detail_img2);
        Picasso.with(biz_detail_img3.getContext()).load(photos.get(2)).resize(size, size).into(biz_detail_img3);
    }
    public void onYelpReviewsComplete(Reviews rvs){
        ReviewListviewAdapter adapter = new ReviewListviewAdapter(rvs);
        biz_review_listview.setAdapter(adapter);
    }
//    @Override ,View.OnClickListener
    public void onClick(View v) {
        if(v.getId()==R.id.biz_detail_address){
            Uri gmmIntentUri = Uri.parse("geo:0,0?q="+Util.Join(business.getLocation().getDisplayAddress()));
            Intent i = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            i.setPackage("com.google.android.apps.maps");
            startActivity(i);
        }
        else if(v.getId()==R.id.biz_detail_phone){
            String phone_number = "tel:"+business.getPhone().replaceAll("[^0-9|\\+]", "");
            Intent i = new Intent(Intent.ACTION_DIAL,Uri.parse(phone_number));
            startActivity(i);
        }
        else if(v.getId()==R.id.biz_detail_yelp_icon){
            Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse(business.getUrl()));
            startActivity(i);
        }
        else if(v.getId()==R.id.biz_detail_facebook_icon){
            Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse(page));
            startActivity(i);
        }
    }
}

