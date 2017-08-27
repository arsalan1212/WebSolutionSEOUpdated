package com.example.arsalankhan.websolutionseo;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Arsalan khan on 8/27/2017.
 */

public class MyAppliction extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //Offline capabilites for firebase database
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
