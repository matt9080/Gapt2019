package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
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
import com.google.android.gms.tasks.*;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.stream.Collectors;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements PopupMenu.OnMenuItemClickListener{

    private static final String TAG = "ProfileFragment";
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
        v = loadbadges(v);

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

    private int ldone = 0;
    private List<String> doneWithDuplicates;
    private View loadbadges(View v) {
        final FirebaseUser user = mAuth.getCurrentUser();

        final ImageView badge1 = (ImageView) v.findViewById(R.id.badge1);
        Picasso.get().load(R.drawable.badge1).into(badge1);
        final ImageView badge2 = (ImageView) v.findViewById(R.id.badge2);
        Picasso.get().load(R.drawable.badge2).into(badge2);
        final ImageView badge3 = (ImageView) v.findViewById(R.id.badge3);
        Picasso.get().load(R.drawable.badge3).into(badge3);
        final ImageView badge4 = (ImageView) v.findViewById(R.id.badge4);
        Picasso.get().load(R.drawable.badge4).into(badge4);
        final TextView lessons = (TextView) v.findViewById(R.id.textView4);

        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        doneWithDuplicates = (List<String>) document.getData().get("lessonscompleted");
                        ldone = doneWithDuplicates.stream().distinct().collect(Collectors.toList()).size();

                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");

                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        if (user.isEmailVerified()) {
            badge1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "Verify email to unlock. Verification Email Sent.", Toast.LENGTH_SHORT).show();
                }
            });
        }else{

            badge1.setColorFilter(filter);
            badge1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getActivity(), "Verify email to unlock. Verification Email Sent.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

        if (ldone>=1) {
            badge2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "1 Lesson Complete", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            badge2.setColorFilter(filter);
            badge2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getActivity(), "Complete 1 lesson to unlock", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        }
        if(ldone>=5){
            badge3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "5 Lessons Complete", Toast.LENGTH_SHORT).show();
                }
            });
        }else{

            badge3.setColorFilter(filter);
            badge3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getActivity(), "Complete 5 lesson to unlock", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

        badge4.setColorFilter(filter);
        badge4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getActivity(), "More badges comming soon", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        lessons.setText("");
        //lessons
        //List<String> doneWithoutDuplicates = doneWithDuplicates.to.stream().distinct().collect(Collectors.toList()).toArray();
        for (int i=0 ;i<HomeFragment.usersList.size();i++){
            for (int j=0 ;j<ldone;j++) {

                if (doneWithDuplicates.stream().distinct().collect(Collectors.toList()).get(j).equals(HomeFragment.usersList.get(i).getID())) {
                    lessons.setText(lessons.getText() + "\n" + HomeFragment.usersList.get(i).getName());
                }

            }
        }

        return v;
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
                                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                        DocumentReference updateuser = db.collection("users").document(user.getUid());

                                                        // Set the "isCapital" field of the city 'DC'
                                                        updateuser
                                                                .update("email", input1.getText().toString())
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Log.w(TAG, "Error updating document", e);
                                                                    }
                                                                });
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

                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            DocumentReference updateuser = db.collection("users").document(user.getUid());

                            // Set the "isCapital" field of the city 'DC'
                            updateuser
                                    .update("name", input1.getText().toString())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error updating document", e);
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
