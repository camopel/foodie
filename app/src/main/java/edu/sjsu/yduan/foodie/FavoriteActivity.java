package edu.sjsu.yduan.foodie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yelp.fusion.client.models.Business;

import java.util.ArrayList;

public class FavoriteActivity extends ProgressActivity {
    private FlowLayout preference_layout;
    private LinearLayout friend_layout;
    private LinearLayout restaurant_layout;
    private LayoutInflater inflater;
    private ImageView fav_pref_add;
    private int req=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        setTitle("Favorite");
        preference_layout = (FlowLayout)findViewById(R.id.preference_layout);
        friend_layout = (LinearLayout)findViewById(R.id.friends_layout);
        restaurant_layout = (LinearLayout)findViewById(R.id.restaurant_layout);
        fav_pref_add = (ImageView)findViewById(R.id.fav_pref_add);
        fav_pref_add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(v.getContext(),NewPreferenceActivity.class));
            }
        });
        inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        addPreferenceItem("Chinese Spicy",7);
        addPreferenceItem("Ramen",8);
        addPreferenceItem("BBQ",9);
        addPreferenceItem("Hotpot",10);
        addFollowingFriends();
        addFollowingRestaurants();
    }
    private void addPreferenceItem(String content,int value){
        TextView v = (TextView)inflater.inflate(R.layout.preference_item, null);
        v.setText(content);
        GradientDrawable bgShape = (GradientDrawable)v.getBackground();
        int color = ContextCompat.getColor(this, Util.getGradeColor(value));
        bgShape.setColor(color);
        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                preference_layout.removeView(v);
                return true;
            }
        });
        preference_layout.addView(v,preference_layout.getChildCount()-1);
    }
    private void addNewPreferenceItem(){
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.share),MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        float r = sharedPref.getFloat("rating",-1);
        if(r!=-1){
            String p = sharedPref.getString("pref",null);
            addPreferenceItem(p,(int)(r*2));
            editor.remove("rating").remove("pref").commit();
        }
    }

    private void addFollowingFriends(){
        final int size=100;
        for(FacebookUser fu:Util.FriendsAndMe){
            final TextView v = (TextView)inflater.inflate(R.layout.favorite_item, null);
            final String fpage = getString(R.string.facebook)+fu.getID();
            v.setText(fu.getName());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0,0,0,4);
            v.setLayoutParams(lp);
            Picasso.with(getBaseContext()).load(fu.getImgUrl()).resize(size, size).into(new Target() {
                @Override
                public void onPrepareLoad(Drawable arg0) {}
                @Override
                public void onBitmapFailed(Drawable arg0) {
                    Drawable default_image = ContextCompat.getDrawable(v.getContext(), R.drawable.ic_person_white);
                    default_image.setBounds(0,0,size,size);
                    v.setCompoundDrawables(default_image,null,null,null);
                }
                @Override
                public void onBitmapLoaded(Bitmap bitmapPic, Picasso.LoadedFrom from){
                    Drawable d = new BitmapDrawable(getResources(), bitmapPic);
                    d.setBounds(0,0,size,size);
                    v.setCompoundDrawablesWithIntrinsicBounds(d,null,null,null);
                }
            });
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    friend_layout.removeView(v);
                    return true;
                }
            });
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(fpage)));
                }
            });
//            v.setGravity(Gravity.LEFT);
            friend_layout.addView(v);
        }
    }

    private void addFollowingRestaurants(){
        ArrayList<Business> alb = new ArrayList<>();
        Business b = new Business();
        b.setName("The Boiling Crab");
        b.setImageUrl("https://s3-media1.fl.yelpcdn.com/bphoto/GgRpcCbCz8Ckuc5Yn9QTuA/o.jpg");
        b.setId("the-boiling-crab-san-jose");
        b.setUrl("https://www.yelp.com/biz/the-boiling-crab-san-jose");
        alb.add(b);

        b = new Business();
        b.setName("Din Tai Fung");
        b.setImageUrl("https://s3-media3.fl.yelpcdn.com/bphoto/JvCA_Z04lZUKiwNTFhqDzg/o.jpg");
        b.setId("din-tai-fung-santa-clara");
        b.setUrl("https://www.yelp.com/biz/din-tai-fung-santa-clara");
        alb.add(b);

        b = new Business();
        b.setName("Gombei Japanese Restaurant");
        b.setImageUrl("https://s3-media2.fl.yelpcdn.com/bphoto/ZJuS_EIsKheNBjX_ndrXAw/o.jpg");
        b.setId("gombei-japanese-restaurant-san-jose");
        b.setUrl("https://www.yelp.com/biz/gombei-japanese-restaurant-san-jose");
        alb.add(b);

        final int size=100;
        for(Business biz:alb){
            final TextView v = (TextView)inflater.inflate(R.layout.favorite_item, null);
            final String ylep_page = biz.getUrl();
            v.setText(biz.getName());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0,0,0,4);
            v.setLayoutParams(lp);
            Picasso.with(getBaseContext()).load(biz.getImageUrl()).resize(size, size).into(new Target() {
                @Override
                public void onPrepareLoad(Drawable arg0) {}
                @Override
                public void onBitmapFailed(Drawable arg0) {
                    Drawable default_image = ContextCompat.getDrawable(v.getContext(), R.mipmap.ic_launcher);
                    default_image.setBounds(0,0,size,size);
                    v.setCompoundDrawables(default_image,null,null,null);
                }
                @Override
                public void onBitmapLoaded(Bitmap bitmapPic, Picasso.LoadedFrom from){
                    Drawable d = new BitmapDrawable(getResources(), bitmapPic);
                    d.setBounds(0,0,size,size);
                    v.setCompoundDrawablesWithIntrinsicBounds(d,null,null,null);
                }
            });
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    friend_layout.removeView(v);
                    return true;
                }
            });
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(ylep_page)));
                }
            });
            restaurant_layout.addView(v);
        }
    }


    @Override
    public void onResume(){
        super.onResume();
        addNewPreferenceItem();

    }
}
