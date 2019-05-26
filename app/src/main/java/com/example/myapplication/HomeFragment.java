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
public class HomeFragment extends Fragment {

    private static final String TAG = "MainActivity";

    //var
    public static Activities activity;
    private static List<Activities> usersListL1;
    private static List<Activities> usersListL2;
    public static List<Activities> m_usersList;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;

    RecyclerView recyclerView1;
    RecyclerView recyclerView2;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.LayoutManager layoutManager2;
    HorizontalViewAdapter adapter;
    HorizontalViewAdapter adapter2;

    private ArrayList<String> mImageUrls = new ArrayList<>();
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        mFirestore = FirebaseFirestore.getInstance();
        m_usersList = new ArrayList<>();
        usersListL1 = new ArrayList<>();
        usersListL2 = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        TextView welcomeName = (TextView) v.findViewById(R.id.welcome_name);
        ImageView welcomeImage = (ImageView) v.findViewById(R.id.welcome_image);
        welcomeName.setText(user.getDisplayName());
        if(user.getPhotoUrl() != null){
            Picasso.get().load(user.getPhotoUrl()).into(welcomeImage);
        }else{
            Picasso.get().load(R.drawable.profile_default).into(welcomeImage);
        }

        recyclerView1 =  v.findViewById(R.id.horrecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(layoutManager);

        recyclerView2 =  v.findViewById(R.id.horrecyclerView2);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true);
        recyclerView2.setLayoutManager(layoutManager2);

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
                            m_usersList.add(users);
                            usersListL1.add(users);
                        }
                        else{
                            m_usersList.add(users);
                            usersListL2.add(users);
                        }
                    }
                }

                adapter = new HorizontalViewAdapter(getActivity(),usersListL1, mImageUrls);
                recyclerView1.setAdapter(adapter);
                adapter2 = new HorizontalViewAdapter(getActivity(),usersListL2, mImageUrls);
                recyclerView2.setAdapter(adapter2);

                adapter.setOnItemCLickListener(new HorizontalViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        usersListL1.get(position);
                        activity = usersListL1.get(position);
                        Intent myIntent = new Intent(getActivity(), DetailedLessonActivity.class);
                        startActivity(myIntent);
                        adapter.notifyItemChanged(position);
                    }
                });

                adapter2.setOnItemCLickListener(new HorizontalViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        usersListL2.get(position);
                        activity = usersListL2.get(position);
                        Intent myIntent = new Intent(getActivity(), DetailedLessonActivity.class);
                        startActivity(myIntent);
                        adapter2.notifyItemChanged(position);
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
