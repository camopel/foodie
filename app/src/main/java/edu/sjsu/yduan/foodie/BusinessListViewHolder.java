package edu.sjsu.yduan.foodie;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yelp.fusion.client.models.Business;

import java.util.Locale;

class BusinessListViewHolder extends ViewHolder implements View.OnClickListener{//FacebookEvent
    private Business business;
    private Context context;
    private ImageView item_logo;
    private TextView biz_name;
    private TextView biz_score;
    private  TextView biz_price;
    private TextView biz_category;
    private TextView biz_distance;
    private TextView biz_like;
    private TextView biz_reviewcount;
    private String fbPageID;

    BusinessListViewHolder(View view){
        super(view);
        context = view.getContext();
        item_logo = (ImageView)view.findViewById(R.id.biz_img);
        biz_name = (TextView) view.findViewById(R.id.biz_name);
        biz_score = (TextView) view.findViewById(R.id.biz_score);
        biz_price = (TextView) view.findViewById(R.id.biz_price);
        biz_category = (TextView) view.findViewById(R.id.biz_category);
        biz_distance = (TextView) view.findViewById(R.id.biz_distance);
        biz_like = (TextView) view.findViewById(R.id.biz_like);
        biz_reviewcount = (TextView) view.findViewById(R.id.biz_reviewcount);
        view.setOnClickListener(this);
        view.setTag(this);
    }
    void bind(BusinessAIO ba){
        business=ba.business;
        fbPageID=ba.facebookPage.PageID;
        biz_name.setText(business.getName());
        biz_price.setText(business.getPrice());
        biz_category.setText(Util.JoinCategories(business.getCategories()));
        biz_distance.setText(String.format(Locale.ENGLISH,"%.1f",(business.getDistance()/1609.34)));
        int grade = ((int)business.getRating())*2;
        biz_score.setText(String.valueOf(grade));
        biz_reviewcount.setText(String.valueOf(business.getReviewCount()));
        GradientDrawable bgShape = (GradientDrawable)biz_score.getBackground();
        int color = ContextCompat.getColor(context, Util.getGradeColor(grade));
        bgShape.setColor(color);
        biz_like.setText(Util.formatInteger(ba.facebookPage.getLikes()));
        Picasso.with(item_logo.getContext()).load(business.getImageUrl()).resize(100, 100).into(item_logo);
    }

    @Override
    public void onClick(View v){
        Intent intent= new Intent(v.getContext(),RestaurantActivity.class);
        intent.putExtra("biz",business);
        intent.putExtra("fpageid",fbPageID);
        context.startActivity(intent);
    }
}
