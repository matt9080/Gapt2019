package com.example.myapplication;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    @NonNull
    public List<Activities> usersList;
    public RecyclerAdapter(List<Activities> usersList){
        this.usersList = usersList;
    }
    private String[] titles = {"Chapter One",
            "Chapter Two",
            "Chapter Three",
            "Chapter Four"};

    private String[] details = {"Item one details",
            "Item two details", "Item three details",
            "Item four details"};

    private int[] images = { R.drawable.logo,
            R.drawable.logo,
            R.drawable.logo,
            R.drawable.logo,};

    class ViewHolder extends RecyclerView.ViewHolder{

        public int currentItem;
        //public ImageView itemImage;
        public TextView itemName;
        public TextView itemAge;

        public ViewHolder(View itemView) {
            super(itemView);
            //itemImage = (ImageView)itemView.findViewById(R.id.item_image);
            itemName = (TextView)itemView.findViewById(R.id.item_title);
            itemAge =
                    (TextView)itemView.findViewById(R.id.item_detail);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();

                    Snackbar.make(v, "Click detected on item " + position,
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
            });*/
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