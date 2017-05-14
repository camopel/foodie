package edu.sjsu.yduan.foodie;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yelp.fusion.client.models.Reviews;

/**
 * Created by yduan on 5/12/2017.
 */

public class ReviewListviewAdapter extends RecyclerView.Adapter<ReviewListviewHolder> {
    Reviews reviews;
    ReviewListviewAdapter(Reviews rvs){
        reviews=rvs;
    }
    @Override
    public ReviewListviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ReviewListviewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReviewListviewHolder holder, int position) {
        holder.bind(reviews.getReviews().get(position));
    }

    @Override
    public int getItemCount() {
        return reviews.getReviews().size();
    }
}
