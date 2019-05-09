package com.example.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUpActivity extends AppCompatActivity  implements View.OnClickListener {

    ProgressBar progressBar;
    EditText editTextEmail, editTextPassword1, editTextPassword2;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_view);

        editTextEmail = (EditText) findViewById(R.id.email_textbox);
        editTextPassword1 = (EditText) findViewById(R.id.su_pass1);
        editTextPassword2 = (EditText) findViewById(R.id.su_pass2);
        progressBar = (ProgressBar) findViewById(R.id.su_progressBar);

        mAuth = FirebaseAuth.getInstance();


        Button btn = (Button) findViewById(R.id.su_btn);
        btn.setOnClickListener(this);
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password1 = editTextPassword1.getText().toString().trim();
        String password2 = editTextPassword2.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (password1.isEmpty()) {
            editTextPassword1.setError("Password is required");
            editTextPassword1.requestFocus();
            return;
        }

        if (password2.isEmpty()) {
            editTextPassword2.setError("Re-Enter Password is required");
            editTextPassword2.requestFocus();
            return;
        }

        if (password1.length() < 6) {
            editTextPassword1.setError("Minimum lenght of password should be 6");
            editTextPassword1.requestFocus();
            return;
        }

        if (!(password2.equals(password1))) {
            editTextPassword2.setError("Passwords do not match");
            editTextPassword2.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    finish();
                    Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {

                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.su_btn:
                registerUser();
                break;


        }
    }
}