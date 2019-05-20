package com.example.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class InfoListAdapter extends RecyclerView.Adapter<InfoListAdapter.ViewHolder>{
    @NonNull
    public List<Activities> usersList;
    public InfoListAdapter(List<Activities> usersList){
        this.usersList = usersList;
    }
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameText.setText(usersList.get(position).getName());
        holder.descText.setText(usersList.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View  mView;

        public TextView nameText;
        public TextView descText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            nameText = (TextView)mView.findViewById(R.id.name_text);
            descText = (TextView)mView.findViewById(R.id.desc_text);

        }
    }
}

