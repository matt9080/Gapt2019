package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2/12/2018.
 */

public class HorizontalViewAdapter extends RecyclerView.Adapter<HorizontalViewAdapter.ViewHolder> {

    private static final String TAG = "HorizontalViewAdapter";

    private Activities activity;
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

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on an image: " + mActivityList.get(position).getName());

                //adapter.setOnItemCLickListener(new HorizontalViewAdapter.OnItemClickListener() {
                   // @Override
                    //public void onItemClick(int position) {mActivityList.get(position);
               activity = mActivityList.get(position);
               Intent myIntent = new Intent(mContext, DetailedLessonActivity.class);
              mContext.startActivity(myIntent);
                notifyItemChanged(position);
                    //}
               // });


                Toast.makeText(mContext, mActivityList.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
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
        }
    }
}