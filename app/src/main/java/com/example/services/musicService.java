package com.example.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class musicService extends Service {
    MediaPlayer player;
    public static final String Channel_ID="musicServiceChannel";
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        player=MediaPlayer.create(this,R.raw.song);
        player.setLooping(true);
        player.start();
        //Notification intent at System Notification
        Intent notificationIntent=new Intent(this,MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,notificationIntent,0);
        Notification notification=new NotificationCompat.Builder(this,Channel_ID)
                .setContentTitle("Song Service")
                .setContentText("Music started")
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1,notification);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(player!=null)
            player.stop();
        Intent intent=new Intent("com.example.services.SERVICE_ACTION");
        intent.putExtra("com.example.services.EXTRA_TEXT","Service was stopped");
        sendBroadcast(intent);
        stopSelf();

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
