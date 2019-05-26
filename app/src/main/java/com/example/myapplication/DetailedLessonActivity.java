package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class DetailedLessonActivity extends AppCompatActivity  implements View.OnClickListener {

    private ViewPager viewPager;
    private TextView title;
    private StepsFragmentCollection adapter;

    //class to display lesson details, adds the fragments to the view pager
    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fulllesson_view);
        viewPager = findViewById(R.id.viewpager);
        title = findViewById(R.id.txt_lesson_title);
        title.setText(HomeFragment.curr_activity.getName());
        adapter = new StepsFragmentCollection(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        findViewById(R.id.btn_arrow_left).setOnClickListener(this);
        findViewById(R.id.btn_arrow_right).setOnClickListener(this);
    }

    //click listeners for the page changer buttons
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_arrow_left:
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
                break;
            case R.id.btn_arrow_right:
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                break;
        }
    }
}