package com.example.myapplication;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener{

    private ConstraintLayout layout_login; //to use
    private ConstraintLayout layout_home;   //to use

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);

        Button one = (Button) findViewById(R.id.su_btn);
        one.setOnClickListener(this); // calling onClick() method
        Button two = (Button) findViewById(R.id.btn_login);
        two.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.su_btn:
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

    //to fill
    protected void onStart() {
        super.onStart();
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onPause() {
        super.onPause();
    }
    protected void onStop() {
        super.onStop();
    }
    protected void onDestroy() {
        super.onDestroy();
    }

}
