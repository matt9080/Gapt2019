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

    private static final String TAG = "HomeFragment";

    public static Activities curr_activity;
    private static List<Activities> activitiesListL1;  //list to hold level 1 activities
    private static List<Activities> activitiesListL2;  //list to hold level 2 activities
    public static List<Activities> m_activitiesList;   //list to hold all activities
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;

    RecyclerView recyclerViewL1, recyclerViewL2;
    HorizontalViewAdapter adapter;  //adapter to handle recyclerview for Level 1
    HorizontalViewAdapter adapter2; //adapter to handle recyclerview for Level 2

    private ArrayList<String> mImageUrls = new ArrayList<>();
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        //initialization
        mFirestore = FirebaseFirestore.getInstance();
        m_activitiesList = new ArrayList<>();
        activitiesListL1 = new ArrayList<>();
        activitiesListL2 = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        TextView welcomeName = (TextView) v.findViewById(R.id.welcome_name);
        ImageView welcomeImage = (ImageView) v.findViewById(R.id.welcome_image);
        welcomeName.setText("Welcome Back, " + user.getDisplayName());

        if(user.getPhotoUrl() != null){
            Picasso.get().load(user.getPhotoUrl()).into(welcomeImage);
        }else{
            Picasso.get().load(R.drawable.profile_default).into(welcomeImage);  // if there is no photo, default is used
        }

        recyclerViewL1 =  v.findViewById(R.id.horrecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewL1.setLayoutManager(layoutManager);

        recyclerViewL2 =  v.findViewById(R.id.horrecyclerView2);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true);
        recyclerViewL2.setLayoutManager(layoutManager2);

        mFirestore.collection("activities").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Log.d(TAG, "Error:" + e.getMessage());
                }
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {

                        Activities users = doc.getDocument().toObject(Activities.class);
                        if (users.getLevel().equals("1")){          //activities are loaded from firestore and put in different
                            m_activitiesList.add(users);            //lists according to level
                            activitiesListL1.add(users);
                        }
                        else{
                            m_activitiesList.add(users);
                            activitiesListL2.add(users);
                        }
                    }
                }

                adapter = new HorizontalViewAdapter(getActivity(), activitiesListL1);
                recyclerViewL1.setAdapter(adapter);
                adapter2 = new HorizontalViewAdapter(getActivity(), activitiesListL2);
                recyclerViewL2.setAdapter(adapter2);

                //when clicking on the recyclerview, get the position of the item and open the lesson activity.
                adapter.setOnItemCLickListener(new HorizontalViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        curr_activity = activitiesListL1.get(position);
                        Intent myIntent = new Intent(getActivity(), DetailedLessonActivity.class);
                        startActivity(myIntent);
                        adapter.notifyItemChanged(position);
                    }
                });

                adapter2.setOnItemCLickListener(new HorizontalViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        curr_activity = activitiesListL2.get(position);
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
