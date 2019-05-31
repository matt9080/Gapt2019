package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.youtube.player.*;

public class VideoActivity extends  YouTubeBaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        //final EditText editTextId = findViewById(R.id.editTextId);
        //Button buttonPlay = findViewById(R.id.buttonPlay);
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