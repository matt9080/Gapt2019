package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements PopupMenu.OnMenuItemClickListener{

    private static final String TAG = "ProfileFragment";
    private static final int CHOOSE_IMAGE = 101;
    FirebaseAuth mAuth;
    FirebaseUser user;
    TextView username;

    ImageView profileView;

    Uri uriProfileImage;
    ProgressBar progressBar;
    StorageReference profileImageRef;
    String profileImageUrl;
    StorageReference usersRef;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        profileView = (ImageView) v.findViewById(R.id.profilepic);
        username = (TextView) v.findViewById(R.id.profileUsername);
        mAuth = FirebaseAuth.getInstance();
        loadUserInformation();

        Button settings = (Button)v.findViewById(R.id.psettings_btn);

        settings.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });

        progressBar = (ProgressBar) v.findViewById(R.id.profileProgressBar);

        profileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChooser();
            }
        });

        return v;
    }


    //method to load user infoormation from firebase to the ui
    private void loadUserInformation() {
        user = mAuth.getCurrentUser();

        if (user != null) {

            if (user.getPhotoUrl() != null) {

                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(profileView);
            }else{
                profileView.setImageResource(R.drawable.profile_default);
            }

            if (user.getDisplayName() != null) {
                username.setText(user.getDisplayName());
            }

        }
        //loads the completed lessons of the authenticated person
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        try {
                            lessonscompleted = (List<String>) document.getData().get("lessonscompleted");
                            lessonscompletedsize = lessonscompleted.size();
                        }catch(Exception x){
                            lessonscompletedsize=0;
                        }
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");

                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
                loadbadges(getView());
            }
        });
    }

    private int lessonscompletedsize = 0;
    private List<String> lessonscompleted;

    //method to load the badges
    private View loadbadges(View v) {

        user = mAuth.getCurrentUser();
        final ImageView badge1 = (ImageView) v.findViewById(R.id.badge1);
        Picasso.get().load(R.drawable.badge1).into(badge1);
        final ImageView badge2 = (ImageView) v.findViewById(R.id.badge2);
        Picasso.get().load(R.drawable.badge2).into(badge2);
        final ImageView badge3 = (ImageView) v.findViewById(R.id.badge3);
        Picasso.get().load(R.drawable.badge3).into(badge3);
        final ImageView badge4 = (ImageView) v.findViewById(R.id.badge4);
        Picasso.get().load(R.drawable.badge4).into(badge4);
        final TextView lessons = (TextView) v.findViewById(R.id.textView4);

        //creating a greyscale color matrix for unearned badges
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

        //badge 1: checking if users verified
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
                    //sending a verification email to the authenticated user
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getActivity(), "Verify email to unlock. Verification Email Sent.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

        //badge 2: checking if user completed 1 lesson
        if (lessonscompletedsize>=1) {
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
        //badge 3: checking if user completed 5 lessons
        if(lessonscompletedsize>=5){
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
        //listing all competed lessons
        for (int i = 0; i< HomeFragment.m_activitiesList.size(); i++){
            for (int j=0 ;j<lessonscompletedsize;j++) {
                if (lessonscompleted.get(j).equals(HomeFragment.m_activitiesList.get(i).getID())) {
                    lessons.setText(lessons.getText() + "\n" + HomeFragment.m_activitiesList.get(i).getName());
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
            case R.id.about:
                Intent myIntent2 = new Intent(getActivity(), AboutActivity.class);
                getActivity().startActivity(myIntent2);
                return true;
            default:
                return false;
        }
    }

    //method to create a custom alert dialog based on dialogbox.xml to handle password changes
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
        alertDialogBuilder.setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        final AlertDialog alert = alertDialogBuilder.create();
        alert.show();
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (input1.getText().toString().isEmpty()) {
                    input1.setError("Old password required");
                    input1.requestFocus();
                    return;

                }
                if (input2.getText().toString().isEmpty()) {
                    input2.setError("New password required");
                    input2.requestFocus();
                    return;
                }
                if (input2.length() < 6) {
                    input2.setError("Minimum length of the new password should be 6");
                    input2.requestFocus();
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
                                    // updating password in firebase authentication
                                    user.updatePassword(input2.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getActivity(), "Password updated", Toast.LENGTH_SHORT).show();
                                                alert.dismiss();
                                            } else {
                                                input1.setError(task.getException().getMessage());
                                                input1.requestFocus();
                                                return;

                                            }
                                        }
                                    });
                                } else {
                                    input1.setError(task.getException().getMessage());
                                    input1.requestFocus();
                                    return;

                                }
                            }
                        });


            }
        });
    }

    //method to create a custom alert dialog based on dialogbox.xml to handle email changes
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
        alertDialogBuilder.setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        final AlertDialog alert = alertDialogBuilder.create();
        alert.show();
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (input1.getText().toString().isEmpty()) {
                    input1.setError("New email required");
                    input1.requestFocus();
                    return;
                }
                if (input2.getText().toString().isEmpty()) {
                    input2.setError("Password required");
                    input2.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(input1.getText().toString()).matches()) {
                    input1.setError("Please enter a valid email");
                    input1.requestFocus();
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
                                    // updating email in in firebase authentication
                                    user.updateEmail(input1.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                DocumentReference updateuser = db.collection("users").document(user.getUid());

                                                // updating email in in firestore
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
                                                Toast.makeText(getActivity(), "Email updated", Toast.LENGTH_SHORT).show();
                                                alert.dismiss();
                                            } else {
                                                input1.setError(task.getException().getMessage());
                                                input1.requestFocus();
                                                return;
                                            }
                                        }
                                    });
                                } else {
                                    input2.setError(task.getException().getMessage());
                                    input2.requestFocus();
                                    return;

                                }
                            }
                        });

            }
        });
    }

    //method to create a custom alert dialog based on dialogbox.xml to handle username changes
    protected void changeUsername() {

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.dialogbox, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity())
                .setPositiveButton(android.R.string.ok, null);
        alertDialogBuilder.setView(promptView);

        final TextView title = (TextView) promptView.findViewById(R.id.dialogboxTitle);
        title.setText("Change Username");
        final EditText input1 = (EditText) promptView.findViewById(R.id.input1);
        input1.setHint("New Username...");

        final EditText input2 = (EditText) promptView.findViewById(R.id.input2);
        input2.setVisibility(View.GONE);
        // setup a dialog window

        alertDialogBuilder.setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        final AlertDialog alert = alertDialogBuilder.create();
        alert.show();
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (input1.getText().toString().isEmpty()) {
                    input1.setError("Username is required");
                    input1.requestFocus();
                    return;
                }

                if (input1.getText().toString().length() < 3 || input1.getText().toString().length() > 16 ) {
                    input1.setError("Length should be between 3 and 16 characters");
                    input1.requestFocus();
                    return;
                }

                FirebaseUser user = mAuth.getCurrentUser();

                if (user != null) {
                    // updating display name in firebase authentication
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

                    // updating display name in firestore
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
                    alert.dismiss();
                }
            }
        });
    }

    //method to save users new photo url in firebase authentication
    private void saveUserInformation() {

        FirebaseUser user = mAuth.getCurrentUser();

        if (profileImageUrl != null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build();

            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                            }
                        }
                    });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImage = data.getData();

            try {

                profileView.setImageBitmap(decodeUri(uriProfileImage));

                uploadImageToFirebaseStorage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //method to change uri to bitmap (resizes the profile image to a square)
    public  Bitmap decodeUri(Uri uri) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
        Bitmap newbitmap;

        if (bitmap.getWidth() >= bitmap.getHeight()){

            newbitmap = Bitmap.createBitmap(
                    bitmap,
                    bitmap.getWidth()/2 - bitmap.getHeight()/2,
                    0,
                    bitmap.getHeight(),
                    bitmap.getHeight()
            );

        }else{

            newbitmap = Bitmap.createBitmap(
                    bitmap,
                    0,
                    bitmap.getHeight()/2 - bitmap.getWidth()/2,
                    bitmap.getWidth(),
                    bitmap.getWidth()
            );
        }
        return newbitmap;
    }

    //method to upload the new profile image to firebase storage
    private void uploadImageToFirebaseStorage() throws IOException {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        profileImageRef =
                FirebaseStorage.getInstance().getReference("profilepics/" + user.getUid() + ".jpg");

        if (uriProfileImage != null) {
            //progressBar.setVisibility(View.VISIBLE);

            Uri file = uriProfileImage;

            usersRef = FirebaseStorage.getInstance().getReference().child("profilepics/" + user.getUid() + ".jpg");


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            decodeUri(uriProfileImage).compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = usersRef.putBytes(data);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    //handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    FirebaseStorage.getInstance().getReference().child("profilepics/" + user.getUid() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            // Update image uri, creating the document if it does not already exist.
                            Map<String, Object> data = new HashMap<>();
                            data.put("image", uri.toString());
                            profileImageUrl = uri.toString();

                            db.collection("users").document(user.getUid())
                                    .set(data, SetOptions.merge());

                            saveUserInformation();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //handle any errors
                        }
                    });
                }
            });

        }

    }

    //method to allow the user to choose an image from the phone's storage
    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), CHOOSE_IMAGE);
    }
}
