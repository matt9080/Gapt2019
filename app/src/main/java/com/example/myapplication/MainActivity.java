package com.example.myapplication;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener{

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        mAuth = FirebaseAuth.getInstance();

        //initializing buttons and calling the  onClick() method
        Button one = (Button) findViewById(R.id.btn_signup);
        one.setOnClickListener(this);
        Button two = (Button) findViewById(R.id.btn_login);
        two.setOnClickListener(this);

    }

    //click listeners for the signup and login buttons
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_signup:
                Intent myIntent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(myIntent);
                break;

            case R.id.btn_login:
                Intent myIntent2 = new Intent(MainActivity.this, LogInActivity.class);
                startActivity(myIntent2);
                break;

            default:
                break;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        //checking if the user is already logged in, if yes it takes them to the main page
        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, MainFunctionsActivity.class));

        }
    }

}
