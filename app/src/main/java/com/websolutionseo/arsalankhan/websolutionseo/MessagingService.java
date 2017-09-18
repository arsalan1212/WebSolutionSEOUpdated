package com.websolutionseo.arsalankhan.websolutionseo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Arsalan khan on 9/18/2017.
 */

public class MessagingService extends FirebaseMessagingService {

    Intent intent;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(remoteMessage.getData().size() > 0){

            String title = remoteMessage.getData().get("title");
            String message = remoteMessage.getData().get("message");

            if(title.equals("New Comment") && message.equals("SomeOne Comment on your Video")){

                intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                showNotification(title,message);
            }
            else if(remoteMessage.getData().get("videoId")!=null){

                String videoId = remoteMessage.getData().get("videoId");
                String click_action = remoteMessage.getData().get("click_action");

                intent = new Intent(click_action);
                intent.putExtra("videoId",videoId);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                Log.d("TAG","VIDEOID: "+videoId);
                Log.d("TAG","CLICK_ACTION: "+click_action);

                showNotification(title,message);

            }
        }


    }

    private void showNotification(String title, String message) {

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setSmallIcon(R.mipmap.message);
        builder.setAutoCancel(true);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(soundUri);
        builder.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0,builder.build());
    }
}
