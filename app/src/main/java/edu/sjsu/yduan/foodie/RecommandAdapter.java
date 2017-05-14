package edu.sjsu.yduan.foodie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by xiaoyuliang on 5/12/17.
 */

class RecommandAdapter extends BaseAdapter {
    public static final String TAG = RecommandAdapter.class.getSimpleName();


    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Recommendation> mDataSource;

    public RecommandAdapter(Context context, ArrayList<Recommendation> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        // check if the view already exists if so, no need to inflate and findViewById again!
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_recommendation, parent, false);
            holder = new ViewHolder();
            holder.titleTextView = (TextView) convertView.findViewById(R.id.list_title);
            holder.thumbnailImageView = (ImageView) convertView.findViewById(R.id.list_thumbnail);
            holder.subtitleTextView = (TextView) convertView.findViewById(R.id.list_subtitle);
            holder.detailTextView = (TextView) convertView.findViewById(R.id.list_detail);
            convertView.setTag(holder);
        }
        else {

            // skip all the expensive inflation/findViewById and just get the holder you already made
            holder = (ViewHolder) convertView.getTag();
        }
        TextView titleTextView = holder.titleTextView;
        TextView subtitleTextView = holder.subtitleTextView;
        ImageView thumbnailImageView = holder.thumbnailImageView;
        TextView detailTextView = holder.detailTextView;

        Recommendation res = (Recommendation) getItem(position);

        titleTextView.setText(res.name);
        subtitleTextView.setText(res.discription);
        detailTextView.setText(res.distance);

        // Use Picasso to load the image. Temporarily have a placeholder in case it's slow to load
        Picasso.with(mContext).load(res.image).placeholder(R.mipmap
                .ic_launcher).into(thumbnailImageView);

        // Style text views
//        Typeface titleTypeFace = Typeface.createFromAsset(mContext.getAssets(),
//                "fonts/JosefinSans-Bold.ttf");
//        titleTextView.setTypeface(titleTypeFace);
//        Typeface subtitleTypeFace = Typeface.createFromAsset(mContext.getAssets(),
//                "fonts/JosefinSans-SemiBoldItalic.ttf");
//        subtitleTextView.setTypeface(subtitleTypeFace);
//        Typeface detailTypeFace = Typeface.createFromAsset(mContext.getAssets(),
//                "fonts/Quicksand-Bold.otf");


        return convertView;
    }

    private static class ViewHolder {
        public TextView titleTextView;
        public TextView subtitleTextView;
        public ImageView thumbnailImageView;
        public TextView detailTextView;
    }
}
