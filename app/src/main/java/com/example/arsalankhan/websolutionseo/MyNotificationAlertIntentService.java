package com.example.arsalankhan.websolutionseo;

import android.app.IntentService;
import android.content.Intent;
import android.media.MediaPlayer;

public class MyNotificationAlertIntentService extends IntentService {


    MediaPlayer mediaPlayer;
    public MyNotificationAlertIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        mediaPlayer =MediaPlayer.create(getApplicationContext(),R.raw.alert);
        mediaPlayer.setLooping(false);
        mediaPlayer.start();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
    }
}
