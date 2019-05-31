package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class DetailedLessonActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TextView title;
    private StepsFragmentCollection adapter;
    private ImageView swipe;
    private Button video;

    //class to display lesson details, adds the fragments to the view pager
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fulllesson_view);
        viewPager = findViewById(R.id.viewpager);
        title = findViewById(R.id.txt_lesson_title);
        title.setText(HomeFragment.curr_activity.getName());
        adapter = new StepsFragmentCollection(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        video = findViewById(R.id.video);

        video.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(DetailedLessonActivity.this, VideoActivity.class);
                startActivity(myIntent);
            }
        });

        swipe = findViewById(R.id.swipe);
        final Animation myFadeInAnimation0 = AnimationUtils.loadAnimation(this, R.anim.fadein);
        final Animation myFadeInAnimation1 = AnimationUtils.loadAnimation(this, R.anim.fadein);
        final Animation myFadeOutAnimation0 = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        final Animation myFadeOutAnimation1 = AnimationUtils.loadAnimation(this, R.anim.fadeout);

        myFadeInAnimation0.setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) { }
            public void onAnimationRepeat(Animation animation) { }
            public void onAnimationEnd(Animation animation) {
                swipe.startAnimation(myFadeOutAnimation0);
                return;
            }
        });
        myFadeOutAnimation0.setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) { }
            public void onAnimationRepeat(Animation animation) { }
            public void onAnimationEnd(Animation animation) {
                swipe.startAnimation(myFadeInAnimation1);
                return;
            }
        });
        myFadeInAnimation1.setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) { }
            public void onAnimationRepeat(Animation animation) { }
            public void onAnimationEnd(Animation animation) {
                swipe.startAnimation(myFadeOutAnimation1);
                return;
            }
        });
        myFadeOutAnimation1.setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) { }
            public void onAnimationRepeat(Animation animation) { }
            public void onAnimationEnd(Animation animation) {
                swipe.setVisibility(View.GONE);
                return;
            }
        });
        swipe.startAnimation(myFadeInAnimation0);

        /*new CountDownTimer(2000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                swipe.setVisibility(View.GONE);
            }
        }.start();*/

    }

}