package com.example.myapplication;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    @NonNull
    public List<Activities> usersList;

    public RecyclerAdapter(List<Activities> usersList){
        this.usersList = usersList;
    }

    private int[] images = { R.drawable.logo,
            R.drawable.logo,
            R.drawable.logo,
            R.drawable.logo,};

    class ViewHolder extends RecyclerView.ViewHolder{

        //public ImageView itemImage;
        public TextView itemName;
        public TextView itemAge;

        public ViewHolder(View itemView) {
            super(itemView);
            //itemImage = (ImageView)itemView.findViewById(R.id.item_image);
            itemName = (TextView)itemView.findViewById(R.id.item_title);
            itemAge = (TextView)itemView.findViewById(R.id.item_detail);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        Activities activity;
        activity = usersList.get(i);
        viewHolder.itemName.setText(activity.getName());
        viewHolder.itemAge.setText(activity.getAge());
        //viewHolder.itemImage.setImageResource(images[i]);
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }
}