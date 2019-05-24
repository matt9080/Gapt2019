package com.example.myapplication;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DetailedLessonActivity extends AppCompatActivity  implements View.OnClickListener {

    private ViewPager viewPager;
    private TextView title;
    private StepsFragmentCollection adapter;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fulllesson_view);
        viewPager = findViewById(R.id.viewpager);
        title = findViewById(R.id.lessonTitle);
        title.setText(NewLessonsFragment.activity.getName());
        adapter = new StepsFragmentCollection(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button).setOnClickListener(this);
        

    }

    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.button2:
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
                break;
            case R.id.button:
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                break;
        }
    }

}