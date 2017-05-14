package edu.sjsu.yduan.foodie;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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
            holder = (ViewHolder) convertView.getTag();
        }
        TextView titleTextView = holder.titleTextView;
        TextView subtitleTextView = holder.subtitleTextView;
        ImageView thumbnailImageView = holder.thumbnailImageView;
        TextView detailTextView = holder.detailTextView;
        Recommendation res = (Recommendation) getItem(position);
        Picasso.with(mContext).load(res.image).placeholder(R.mipmap.ic_launcher).into(thumbnailImageView);
        titleTextView.setText(res.name);
        subtitleTextView.setText(res.discription);

        GradientDrawable bgShape = (GradientDrawable)detailTextView.getBackground();
        int grade = (int)(res.grade*2);
        int color = ContextCompat.getColor(mContext, Util.getGradeColor(grade));
        bgShape.setColor(color);
        detailTextView.setText(String.valueOf(grade));
        return convertView;
    }

    private static class ViewHolder {
        public TextView titleTextView;
        public TextView subtitleTextView;
        public ImageView thumbnailImageView;
        public TextView detailTextView;
    }
}
