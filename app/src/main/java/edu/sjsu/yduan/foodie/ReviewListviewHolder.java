package edu.sjsu.yduan.foodie;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.yelp.fusion.client.models.Review;


class ReviewListviewHolder extends RecyclerView.ViewHolder {
    private ImageView biz_detail_review_img;
    private TextView biz_detail_review_name;
    private TextView biz_detail_review_time;
    private TextView biz_detail_review_comment;
    private TextView biz_detail_review_rating;
    private Context context;
    ReviewListviewHolder(View view){
        super(view);
        context=view.getContext();
        biz_detail_review_img=(ImageView)view.findViewById(R.id.biz_detail_review_img);
        biz_detail_review_name=(TextView)view.findViewById(R.id.biz_detail_review_name);
        biz_detail_review_time=(TextView)view.findViewById(R.id.biz_detail_review_time);
        biz_detail_review_comment=(TextView)view.findViewById(R.id.biz_detail_review_comment);
        biz_detail_review_rating=(TextView)view.findViewById(R.id.biz_detail_review_rating);
        view.setTag(this);
    }
    void bind(Review rv){
        Picasso.with(biz_detail_review_img.getContext()).load(rv.getUser().getImageUrl()).resize(50, 50).into(biz_detail_review_img);
        biz_detail_review_name.setText(rv.getUser().getName());
        biz_detail_review_time.setText(rv.getTimeCreated());
        biz_detail_review_comment.setText(rv.getText().trim().replace("\n"," "));
        biz_detail_review_rating.setText(String.valueOf(rv.getRating()*2));
        GradientDrawable bgShape = (GradientDrawable)biz_detail_review_rating.getBackground();
        int color = ContextCompat.getColor(context, Util.getGradeColor(rv.getRating()*2));
        bgShape.setColor(color);
    }
}
