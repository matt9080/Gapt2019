package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.youtube.player.*;

public class VideoActivity extends  YouTubeBaseActivity {

private TextView title;
private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        title = findViewById(R.id.txt_lesson_title);
        title.setText(HomeFragment.curr_activity.getName());
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {

                finish();
            }

        });


        final YouTubePlayerView youtubePlayerView = findViewById(R.id.youtubePlayerView);
        String videoId = HomeFragment.curr_activity.getVideo();
        playVideo(videoId, youtubePlayerView);




    }

    public void playVideo(final String videoId, YouTubePlayerView youTubePlayerView) {
        //initialize youtube player view
        youTubePlayerView.initialize("AIzaSyBfccmN2fK-gHBbNHDX9vNV6YG4vYwRx58",
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {
                        youTubePlayer.cueVideo(videoId);
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
    }
}
//