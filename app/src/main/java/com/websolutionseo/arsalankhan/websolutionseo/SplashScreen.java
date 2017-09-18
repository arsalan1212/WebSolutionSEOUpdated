package com.websolutionseo.arsalankhan.websolutionseo;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);

        new CountDownTimer(5000,1000){
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                //handle Notification Data
                if(getIntent().getExtras() !=null && getIntent().getExtras().get("videoId")!= null){

                for(String key : getIntent().getExtras().keySet()) {

                    if (key.equals("title")) {
                    } else if (key.equals("message")) {
                    } else if (key.equals("click_action")) {
                    }

                    else if (key.equals("videoId")) {

                        Intent intent = new Intent(getApplicationContext(), SingleVideoActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("videoId", getIntent().getExtras().get(key).toString());
                        startActivity(intent);
                        finish();
                    }
                }

                }

                else{

                    Intent mainIntent = new Intent(SplashScreen.this,MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(mainIntent);
                    finish();
                }

            }
        }.start();
    }

    private void handleIntentData(){

    }
}
