package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        TextView about = findViewById(R.id.about_content);
        about.setText("This application prototype was created by students, as an assignment, studying Software Development at the University of Malta." +
                "\n\nLesson Content by:" +
                "\nIEEE" +
                "\nWikiHow");
    }
}
