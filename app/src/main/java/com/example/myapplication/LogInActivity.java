package com.example.myapplication;
import android.support.v4.view.ViewPager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_view);
        configureLessonsButton();
    }

    private void configureLessonsButton() {
        Button btn_lessons = (Button) findViewById(R.id.btn_signin);
        btn_lessons.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Intent myIntent = new Intent(LogInActivity.this, HomeActivity.class);
                startActivity(myIntent);
            }
        });
    }

}