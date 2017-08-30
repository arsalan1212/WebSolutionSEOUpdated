package com.example.arsalankhan.websolutionseo;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class InterstitialAdService extends Service {


    private static final int TOTAL_TIME=60000;
    public static final int DURATION = 1000;



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent ,int flags, int startId) {

        new CountDownTimer(TOTAL_TIME,DURATION){
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                SingleVideoActivity.showInterstitialAd();
                stopSelf();
            }
        }.start();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }
}
