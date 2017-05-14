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

public class FriendsAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Friend> mDataSource;


    public FriendsAdapter(Context context, ArrayList<Friend> items) {
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
            convertView = mInflater.inflate(R.layout.list_item_friend, parent, false);
            holder = new ViewHolder();
            holder.thumbnailImageView = (ImageView) convertView.findViewById(R.id.friend_list_thumbnail);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.friend_list_title);
            holder.subtitleTextView = (TextView) convertView.findViewById(R.id.friend_list_subtitle);
            holder.detailTextView = (TextView) convertView.findViewById(R.id.friend_list_detail);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        TextView titleTextView = holder.titleTextView;
        TextView subtitleTextView = holder.subtitleTextView;
        TextView detailTextView = holder.detailTextView;
        ImageView thumbnailImageView = holder.thumbnailImageView;

        Friend friend = (Friend) getItem(position);
        String pos = String.valueOf(position+1)+".";
        titleTextView.setText(pos+friend.name);
        subtitleTextView.setText(friend.Preference);
        detailTextView.setText(friend.Distance);
        Picasso.with(mContext).load(friend.image).placeholder(R.mipmap.ic_launcher).into(thumbnailImageView);
        return convertView;
    }

    private static class ViewHolder {
        public TextView titleTextView;
        public TextView subtitleTextView;
        public TextView detailTextView;
        public ImageView thumbnailImageView;
    }
}
