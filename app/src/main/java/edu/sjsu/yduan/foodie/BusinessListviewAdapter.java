package edu.sjsu.yduan.foodie;

import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class BusinessListviewAdapter extends Adapter<BusinessListViewHolder> {
    ArrayList<BusinessAIO> Businesses;
    BusinessListviewAdapter(ArrayList<BusinessAIO> bs){
        Businesses=bs;
    }
    @Override
    public BusinessListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.business_list_item, parent, false);
        return new BusinessListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BusinessListViewHolder holder, int position) {
        holder.bind(Businesses.get(position));
    }

    @Override
    public int getItemCount() {
        return Businesses.size();
    }
}
