package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.text.InputType;
import android.util.Patterns;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
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
        username = (TextView) v.findViewById(R.id.profileUsername);
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
                changeUsername();
                return true;
            case R.id.changePassword:
                changePassword();
                return true;
            case R.id.changeEmail:
                changeEmail();
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

    protected void changePassword() {

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.dialogbox, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptView);

        final TextView title = (TextView) promptView.findViewById(R.id.dialogboxTitle);
        title.setText("Change Password");
        final EditText input1 = (EditText) promptView.findViewById(R.id.input1);
        input1.setHint("Old Password...");
        final EditText input2 = (EditText) promptView.findViewById(R.id.input2);
        input2.setHint("New Password...");
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (input1.getText().toString().isEmpty()) {
                            Toast.makeText(getActivity(), "Old password required", Toast.LENGTH_SHORT).show();
                            changePassword();
                            return;

                        }
                        if (input2.getText().toString().isEmpty()) {
                            Toast.makeText(getActivity(), "New password required", Toast.LENGTH_SHORT).show();
                            changePassword();
                            return;
                        }
                        if (input2.length() < 6) {
                            Toast.makeText(getActivity(), "Minimum length of the new password should be 6", Toast.LENGTH_SHORT).show();
                            changePassword();
                            return;
                        }

                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        AuthCredential credential = EmailAuthProvider
                                .getCredential(user.getEmail(), input1.getText().toString());

                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            user.updatePassword(input2.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getActivity(), "Password updated", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        } else {
                                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });

                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    protected void changeEmail() {

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.dialogbox, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptView);

        final TextView title = (TextView) promptView.findViewById(R.id.dialogboxTitle);
        title.setText("Change Email");
        final EditText input1 = (EditText) promptView.findViewById(R.id.input1);
        input1.setHint("New Email...");
        final EditText input2 = (EditText) promptView.findViewById(R.id.input2);
        input2.setHint("Password...");
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (input1.getText().toString().isEmpty()) {
                            Toast.makeText(getActivity(), "New email required", Toast.LENGTH_SHORT).show();
                            changeEmail();
                            return;
                        }
                        if (input2.getText().toString().isEmpty()) {
                            Toast.makeText(getActivity(), "Password required", Toast.LENGTH_SHORT).show();
                            changeEmail();
                            return;
                        }
                        if (!Patterns.EMAIL_ADDRESS.matcher(input1.getText().toString()).matches()) {
                            Toast.makeText(getActivity(), "Please enter a valid email", Toast.LENGTH_SHORT).show();
                            changeEmail();
                            return;
                        }

                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        AuthCredential credential = EmailAuthProvider
                                .getCredential(user.getEmail(), input2.getText().toString());

                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            user.updateEmail(input1.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getActivity(), "Email updated", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        } else {
                                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });

                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    protected void changeUsername() {

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.dialogbox, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptView);

        final TextView title = (TextView) promptView.findViewById(R.id.dialogboxTitle);
        title.setText("Change Username");
        final EditText input1 = (EditText) promptView.findViewById(R.id.input1);
        input1.setHint("New Username...");
        final EditText input2 = (EditText) promptView.findViewById(R.id.input2);
        input2.setVisibility(View.GONE);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (input1.getText().toString().isEmpty()) {
                            Toast.makeText(getActivity(), "Name required", Toast.LENGTH_SHORT).show();
                            changeUsername();
                            return;
                        }

                        if (input1.getText().toString().length() < 4 || input1.getText().toString().length() > 16 ) {
                            Toast.makeText(getActivity(), "Length should be between 4 and 16 characters", Toast.LENGTH_SHORT).show();
                            changeUsername();
                            return;
                        }

                        FirebaseUser user = mAuth.getCurrentUser();

                        if (user != null) {
                            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(input1.getText().toString())
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
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

}
