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
    TextView username;

    ImageView profileView;

    Uri uriProfileImage;
    ProgressBar progressBar;
    StorageReference profileImageRef;
    String profileImageUrl;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        profileView = (ImageView) v.findViewById(R.id.profilepic);
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

        progressBar = (ProgressBar) v.findViewById(R.id.profileProgressBar);

        profileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChooser();
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
                        .into(profileView);
            }else{
                profileView.setImageResource(R.drawable.profile_default);
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


                        try {
                            doneWithDuplicates = (List<String>) document.getData().get("lessonscompleted");
                            ldone = doneWithDuplicates.stream().distinct().collect(Collectors.toList()).size();
                        }catch(Exception x){
                            ldone=0;
                        }

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
        for (int i = 0; i< NewLessonsFragment.usersList.size(); i++){
            for (int j=0 ;j<ldone;j++) {

                if (doneWithDuplicates.stream().distinct().collect(Collectors.toList()).get(j).equals(NewLessonsFragment.usersList.get(i).getID())) {
                    lessons.setText(lessons.getText() + "\n" + NewLessonsFragment.usersList.get(i).getName());
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
                                Toast.makeText(getActivity(), "Profile Picture Updated", Toast.LENGTH_SHORT).show();
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

    private void uploadImageToFirebaseStorage() throws IOException {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        profileImageRef =
                FirebaseStorage.getInstance().getReference("profilepics/" + user.getUid() + ".jpg");

        if (uriProfileImage != null) {
            //progressBar.setVisibility(View.VISIBLE);

            Uri file = uriProfileImage;

            StorageReference usersRef = FirebaseStorage.getInstance().getReference().child("profilepics/" + user.getUid() + ".jpg");


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            decodeUri(uriProfileImage).compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = usersRef.putBytes(data);

// Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    FirebaseStorage.getInstance().getReference().child("profilepics/" + user.getUid() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            // Update one field, creating the document if it does not already exist.
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
                            // Handle any errors
                        }
                    });
                }
            });


/*

            profileImageRef.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //progressBar.setVisibility(View.GONE);

                            profileImageRef =
                                    FirebaseStorage.getInstance().getReference("profilepics/" + user.getUid() + ".jpg");
                            profileImageUrl = profileImageRef.getDownloadUrl().toString();


                            FirebaseStorage.getInstance().getReference().child("profilepics/" + user.getUid() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    // Update one field, creating the document if it does not already exist.
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("image", profileImageUrl);

                                    db.collection("users").document(user.getUid())
                                            .set(data, SetOptions.merge());
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //progressBar.setVisibility(View.GONE);

                        }
                    });*/
        }

    }

    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), CHOOSE_IMAGE);
    }

}
