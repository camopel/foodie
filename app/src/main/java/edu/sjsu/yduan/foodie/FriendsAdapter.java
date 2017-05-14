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
import java.util.HashMap;

/**
 * Created by xiaoyuliang on 5/12/17.
 */

public class FriendsAdapter extends BaseAdapter {
    public static final String TAG = FriendsAdapter.class.getSimpleName();
    public static final HashMap<String, Integer> LABEL_COLORS = new HashMap<String, Integer>()
    {{

        put("non-vegetarian", R.color.colorLowFat);
        put("Vegetarian", R.color.colorVegetarian);
    }};

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

        // check if the view already exists if so, no need to inflate and findViewById again!
        if (convertView == null) {

            // Inflate the custom row layout from your XML.
            convertView = mInflater.inflate(R.layout.list_item_friend, parent, false);

            // create a new "Holder" with subviews
            holder = new ViewHolder();
            holder.thumbnailImageView = (ImageView) convertView.findViewById(R.id.friend_list_thumbnail);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.friend_list_title);
            holder.subtitleTextView = (TextView) convertView.findViewById(R.id.friend_list_subtitle);
            holder.detailTextView = (TextView) convertView.findViewById(R.id.friend_list_detail);

            // hang onto this holder for future recyclage
            convertView.setTag(holder);
        }
        else {

            // skip all the expensive inflation/findViewById and just get the holder you already made
            holder = (ViewHolder) convertView.getTag();
        }

        // Get relevant subviews of row view
        TextView titleTextView = holder.titleTextView;
        TextView subtitleTextView = holder.subtitleTextView;
        TextView detailTextView = holder.detailTextView;
        ImageView thumbnailImageView = holder.thumbnailImageView;

        //Get corresponding recipe for row
        Friend friend = (Friend) getItem(position);
        String pos = String.valueOf(position+1)+".";
        // Update row view's textviews to display recipe information
        titleTextView.setText(pos+friend.name);
        subtitleTextView.setText(friend.Preference);
        detailTextView.setText(friend.Distance);

        // Use Picasso to load the image. Temporarily have a placeholder in case it's slow to load
        Picasso.with(mContext).load(friend.image).placeholder(R.mipmap.ic_launcher).into(thumbnailImageView);

        // Style text views
//        Typeface titleTypeFace = Typeface.createFromAsset(mContext.getAssets(),
//                "fonts/JosefinSans-Bold.ttf");
//        titleTextView.setTypeface(titleTypeFace);
//        Typeface subtitleTypeFace = Typeface.createFromAsset(mContext.getAssets(),
//                "fonts/JosefinSans-SemiBoldItalic.ttf");
//        subtitleTextView.setTypeface(subtitleTypeFace);
//        Typeface detailTypeFace = Typeface.createFromAsset(mContext.getAssets(),
//                "fonts/Quicksand-Bold.otf");
//        detailTextView.setTypeface(detailTypeFace);
//        detailTextView.setTextColor(android.support.v4.content.ContextCompat.getColor(mContext, LABEL_COLORS
//                .get(friend.Label)));

        return convertView;
    }

    private static class ViewHolder {
        public TextView titleTextView;
        public TextView subtitleTextView;
        public TextView detailTextView;
        public ImageView thumbnailImageView;
    }
}
