package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "MainActivity";

    private List<Activities> usersList;
    private FirebaseFirestore mFirestore;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerAdapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        mFirestore = FirebaseFirestore.getInstance();
        usersList = new ArrayList<>();

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        mFirestore.collection("activities").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Log.d(TAG, "Error:" + e.getMessage());
                }
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {

                        Activities users = doc.getDocument().toObject(Activities.class);
                        usersList.add(users);
                    }
                }
                adapter = new RecyclerAdapter(usersList);
                recyclerView.setAdapter(adapter);

                adapter.setOnItemCLickListener(new RecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        usersList.get(position);
                        Intent myIntent = new Intent(getActivity(), DetailedLessonActivity.class);
                        startActivity(myIntent);
                        adapter.notifyItemChanged(position);
                    }
                });
            }
        });
        return v;
    }

    protected void OnStart(){
        super.onStart();
    }
}

