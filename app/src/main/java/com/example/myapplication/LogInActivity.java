package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import android.support.annotation.NonNull;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    EditText editTextEmail, editTextPassword;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_view);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = (EditText) findViewById(R.id.email_textbox);
        editTextPassword = (EditText) findViewById(R.id.pass_textbox);
        progressBar = (ProgressBar) findViewById(R.id.su_progressBar);

        findViewById(R.id.btn_su).setOnClickListener(this);
        findViewById(R.id.forgotPassword).setOnClickListener(this);
    }

    //method to handle user log in
    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        //checking all input is valid
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

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Minimum length of password should be 6");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        //signing in the user using firebase authentication
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    finish();
                    Intent intent = new Intent(LogInActivity.this, MainFunctionsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //creating click activities for buttons
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_su:
                userLogin();
                break;
            case R.id.forgotPassword:
                forgotPass();
                break;
        }
    }


    //method to handle the forget password button
    protected void forgotPass() {

        //creating a new custom dialog box based on dialogbox.xml
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.dialogbox, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);

        final TextView title = (TextView) promptView.findViewById(R.id.dialogboxTitle);
        title.setText("Forgot Password?");
        final EditText input1 = (EditText) promptView.findViewById(R.id.input1);
        input1.setHint("Email...");
        final EditText input2 = (EditText) promptView.findViewById(R.id.input2);
        input2.setVisibility(View.GONE);

        //setting up the dialog window
        alertDialogBuilder.setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
        });

        final AlertDialog alert = alertDialogBuilder.create();
        alert.show();
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (input1.getText().toString().isEmpty()) {
                    input1.setError("Email required");
                    input1.requestFocus();
                    return;
                }
                FirebaseAuth auth = FirebaseAuth.getInstance();
                //sending an password reset link to the users email
                auth.sendPasswordResetEmail(input1.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Reset Password Email Sent", Toast.LENGTH_SHORT).show();
                                    alert.dismiss();
                                } else {
                                    input1.setError(task.getException().getMessage());
                                    input1.requestFocus();
                                    return;
                                }
                            }
                        });

                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
            }
        });
    }

}

