package com.example.arsalankhan.websolutionseo;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.arsalankhan.websolutionseo.NavigtionViewItems.Market_summary_activity;

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

        final String singleVideo = intent.getStringExtra("single_video");
        final String marketSummary = intent.getStringExtra("market_summary");
        final String mainActivityIntent = intent.getStringExtra("activity_main");


        new CountDownTimer(TOTAL_TIME,DURATION){
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {

                if(singleVideo!=null){

                    if(singleVideo.equals(SingleVideoActivity.SINGLE_VIDEO_ACTIVITY_NAME)){

                        SingleVideoActivity.showInterstitialAd();
                        Log.d("TAG","SINGLE VIDEO: Stop");

                    }
                }
                else if(marketSummary!=null){

                    if(marketSummary.equals(Market_summary_activity.ACTIVITY_NAME)){
                        Market_summary_activity.showInterstitialAd();
                        Log.d("TAG","MARKET SUMMARY:Stop ");
                    }
                }
                else if(mainActivityIntent!= null){

                    if(mainActivityIntent.equals(MainActivity.MAIN_ACTIVITY_NAME)){
                        MainActivity.showInterstitialAd();
                    }
                }

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
