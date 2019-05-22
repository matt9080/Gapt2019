package com.example.myapplication;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

public class DetailedLessonActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TextView title;
    private StepsFragmentCollection adapter;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fulllesson_view);
        viewPager = findViewById(R.id.viewpager);
        title = findViewById(R.id.lessonTitle);
        title.setText(HomeFragment.activity.getName());
        adapter = new StepsFragmentCollection(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }
}