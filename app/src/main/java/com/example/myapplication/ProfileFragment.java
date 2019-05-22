package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements PopupMenu.OnMenuItemClickListener{

    FirebaseAuth mAuth;
    TextView username;
    ImageView profilepic;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        profilepic = (ImageView) v.findViewById(R.id.profilepic);
        username = (TextView) v.findViewById(R.id.username);
        mAuth = FirebaseAuth.getInstance();
        loadUserInformation();

        Button settings = (Button)v.findViewById(R.id.psettings_btn);

        settings.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showPopup(v);
                /**/
            }
        });

        return v;

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    private void loadUserInformation() {
        final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(profilepic);
            }else{
                //profilepic.setImageResource(R.drawable.profile_default);
            }

            if (user.getDisplayName() != null) {
                username.setText(user.getDisplayName());
            }

        }
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.popup_menu);
        popup.show();
    }

    private String m_Text = "";
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.changeUsername:
                Intent myIntent = new Intent(getActivity(), EditProfileActivity.class);
                getActivity().startActivity(myIntent);
                return true;
            case R.id.changePassword:


                changeUsername();

                return true;
            case R.id.changeEmail:
                Toast.makeText(getActivity(), "Item 3 clicked", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.popupLogout:
                mAuth = FirebaseAuth.getInstance();
                FirebaseAuth.getInstance().signOut();
                Intent myIntent1 = new Intent(getActivity(), MainActivity.class);
                getActivity().startActivity(myIntent1);
                Toast.makeText(getActivity(), "Logged Out", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }

    public void changeUsername() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Title");

        // Set up the input
        final EditText input = new EditText(getActivity());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text| InputType.TYPE_TEXT_VARIATION_PASSWORD
        input.setInputType(InputType.TYPE_CLASS_TEXT );
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Name required", Toast.LENGTH_SHORT).show();
                }

                if (input.getText().toString().length() < 4 || input.getText().toString().length() > 16 ) {

                    Toast.makeText(getActivity(), "Length should be between 4 and 16 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseUser user = mAuth.getCurrentUser();

                if (user != null) {
                    UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                            .setDisplayName(input.getText().toString())
                            .build();

                    user.updateProfile(profile)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        loadUserInformation();
                                    }
                                }
                            });
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

}
