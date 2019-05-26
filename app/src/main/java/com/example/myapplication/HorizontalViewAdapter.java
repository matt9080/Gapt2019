package com.example.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2/12/2018.
 */

public class HorizontalViewAdapter extends RecyclerView.Adapter<HorizontalViewAdapter.ViewHolder> {

    private static final String TAG = "HorizontalViewAdapter";

    private List<Activities> mActivityList;
    private Context mContext;
    private OnItemClickListener mListener;

    public HorizontalViewAdapter(Context context, List<Activities> activityList, ArrayList<String> imageUrls) {
        mActivityList = activityList;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horlayout_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(mContext)
                .asBitmap()
                .load(mActivityList.get(position).getImage())
                .into(holder.image);

        holder.name.setText(mActivityList.get(position).getName());
    }
    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemCLickListener(OnItemClickListener listener){
        mListener = listener;
    }
    @Override
    public int getItemCount() {
        return mActivityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);

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
}