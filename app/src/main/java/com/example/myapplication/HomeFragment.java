package com.example.myapplication;

import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
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

    //private RecyclerView mMainList;
    //private InfoListAdapter infoListAdapter;

    private List<Activities> usersList;
    private FirebaseFirestore mFirestore;
    //private TextView title;
    //======
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        mFirestore = FirebaseFirestore.getInstance();
        usersList = new ArrayList<>();

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        //mMainList.setHasFixedSize(true);
        //mMainList.setLayoutManager(new LinearLayoutManager(this));
        //mMainList.setAdapter(infoListAdapter);

        //usersList = new ArrayList<>();
       // infoListAdapter = new InfoListAdapter(usersList);

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
            }
        });


       // mMainList = (RecyclerView) v.findViewById(R.id.main_list);
        //title = (TextView) v.findViewById(R.id.txt_materials);



        // Inflate the layout for this fragment
        return v;
    }

    protected void OnStart(){
        super.onStart();


    }
}

