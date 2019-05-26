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

import javax.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 */
public class LessonsFragment extends Fragment {

    private static final String TAG = "MainActivity";
    public static List<Activities> newusersList;
    private FirebaseFirestore mFirestore;

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
        mFirestore = FirebaseFirestore.getInstance();

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerAdapter(HomeFragment.m_usersList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemCLickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                HomeFragment.m_usersList.get(position);
                HomeFragment.activity = HomeFragment.m_usersList.get(position);
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
                newusersList = new ArrayList<>();
                for (int i = 0; i < HomeFragment.m_usersList.size(); i++){
                    if (HomeFragment.m_usersList.get(i).getName().toLowerCase().contains(searchText.getText().toString().toLowerCase())){
                        newusersList.add(HomeFragment.m_usersList.get(i));
                    }
                }
                if(newusersList.size()>0){
                    adapter = new RecyclerAdapter(newusersList);
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

