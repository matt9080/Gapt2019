package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment{

    FirebaseAuth mAuth;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        Button settings = (Button)v.findViewById(R.id.psettings_btn);
        Button edit = (Button)v.findViewById(R.id.pedit_btn);

        settings.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                FirebaseAuth.getInstance().signOut();
                Intent myIntent1 = new Intent(getActivity(), MainActivity.class);
                getActivity().startActivity(myIntent1);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), EditProfileActivity.class);
                getActivity().startActivity(myIntent);
            }
        });

        return v;

    }

}
