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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;


/**
 * A simple {@link Fragment} subclass.
 */
public class LessonsFragment extends Fragment {

    private static final String TAG = "MainActivity";

    //var
    public static Activities activity;
    private static List<Activities> usersList1;
    private static List<Activities> usersList2;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    HorizontalViewAdapter adapter;
    private ArrayList<String> mImageUrls = new ArrayList<>();
    public LessonsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_newlessons, container, false);
        mFirestore = FirebaseFirestore.getInstance();
        usersList1 = new ArrayList<>();
        usersList2 = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        TextView welcomeName = (TextView) v.findViewById(R.id.welcome_name);
        ImageView welcomeImage = (ImageView) v.findViewById(R.id.welcome_image);
        welcomeName.setText(user.getDisplayName());
        Picasso.get().load(user.getPhotoUrl()).into(welcomeImage);


        recyclerView = (RecyclerView) v.findViewById(R.id.horrecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
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
                        if (users.getLevel().equals("1")){
                            usersList1.add(users);
                        }else{
                            usersList2.add(users);
                        }


                    }
                }


                adapter = new HorizontalViewAdapter(getActivity(),usersList, mImageUrls);
                recyclerView.setAdapter(adapter);

                /*adapter.setOnItemCLickListener(new RecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        usersList.get(position);
                        activity = usersList.get(position);
                        Intent myIntent = new Intent(getActivity(), DetailedLessonActivity.class);
                        startActivity(myIntent);
                        adapter.notifyItemChanged(position);
                    }
                });*/
            }
        });
        return v;
    }

    protected void OnStart(){
        super.onStart();
    }

}
