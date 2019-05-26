package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LessonsFragment extends Fragment {



    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerAdapter adapter;

    public LessonsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_lesson, container, false);

        //Initialize and set lessons recycler view
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerAdapter(HomeFragment.m_activitiesList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemCLickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                HomeFragment.m_activitiesList.get(position);
                HomeFragment.curr_activity = HomeFragment.m_activitiesList.get(position);
                Intent myIntent = new Intent(getActivity(), DetailedLessonActivity.class);
                startActivity(myIntent);
                adapter.notifyItemChanged(position);
            }
        });

        Button searchIcon = (Button)v.findViewById(R.id.searchIcon);
        final EditText searchText = (EditText)v.findViewById(R.id.txt_searchhere2);

        searchIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                List<Activities> search_activityList;
                search_activityList = new ArrayList<>();

                //iterate through main activities list, a get searched items
                for (int i = 0; i < HomeFragment.m_activitiesList.size(); i++){
                    if (HomeFragment.m_activitiesList.get(i).getName().toLowerCase().contains(searchText.getText().toString().toLowerCase())){
                        search_activityList.add(HomeFragment.m_activitiesList.get(i));
                    }
                }
                if(search_activityList.size()>0){   //create a recyclerview with results
                    adapter = new RecyclerAdapter(search_activityList);
                    recyclerView.setAdapter(adapter);
                }else{
                    Toast.makeText(getActivity(), "No results found", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }

    protected void OnStart(){
        super.onStart();
    }
}

