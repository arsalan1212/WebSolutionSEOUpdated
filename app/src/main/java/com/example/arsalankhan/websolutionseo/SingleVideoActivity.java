package com.example.arsalankhan.websolutionseo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class SingleVideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private YouTubePlayerView mYoutubePlayerView;
    private String videoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_video);

        mYoutubePlayerView= (YouTubePlayerView) findViewById(R.id.youtubeplayerView);
        mYoutubePlayerView.initialize(MainActivity.DeveloperKey,this);

        //getting the intent data
        Intent intent=getIntent();
        if(intent!=null){
            videoId = intent.getStringExtra("videoId");
            String videoTitle=intent.getStringExtra("videoTitle");
            String ChannelTitle=intent.getStringExtra("channelTitle");
        }

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestore) {

        if(!wasRestore){
            youTubePlayer.loadVideo(videoId);
            youTubePlayer.play();
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

        if(youTubeInitializationResult.isUserRecoverableError()){
            youTubeInitializationResult.getErrorDialog(SingleVideoActivity.this,1).show();
        }
        else{
            Toast.makeText(this, "There is some Error occur while Player Initialization", Toast.LENGTH_SHORT).show();
        }
    }
}
