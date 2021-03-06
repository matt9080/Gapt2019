package com.example.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    @NonNull
    public List<Activities> activitiesList;
    public RecyclerAdapter(List<Activities> activitiesList){
        this.activitiesList = activitiesList;
    }
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemCLickListener(OnItemClickListener listener){
        mListener = listener;
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView itemImage;
        public TextView itemName;
        public TextView itemAge;
        public TextView itemshortDesc;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView)itemView.findViewById(R.id.item_image);
            itemName = (TextView) itemView.findViewById(R.id.item_title);
            itemAge = (TextView) itemView.findViewById(R.id.item_age);
            itemshortDesc = (TextView) itemView.findViewById(R.id.item_desc);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
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
        activity = activitiesList.get(i);
        viewHolder.itemName.setText(activity.getName());
        viewHolder.itemAge.setText("Age: "+activity.getAge());
        viewHolder.itemshortDesc.setText(activity.getShortDescription());
        Picasso.get().load(activity.getImage()).into(viewHolder.itemImage);
    }

    @Override
    public int getItemCount() {
        return activitiesList.size();
    }
}