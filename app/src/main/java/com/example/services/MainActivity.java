package com.example.services;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    // notification and sms service(not working)

    // need to add <service android:name=".musicService"/> at Manifest
    // add permission ["android.permission.FOREGROUND_SERVICE"]
    public static final String Channel_ID="musicServiceChannel";
    myBroadcastReceiver mybr=new myBroadcastReceiver();
    SharedPreferences sharedPreferences;
    TextView tv;
    Button serviceBtn;
    boolean serviceStatus=false;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("ServiceStatus",false);
        editor.apply();
        serviceStatus=sharedPreferences.getBoolean("ServiceStatus",false);
        createNotificationChannel();
        tv=findViewById(R.id.textView);
        serviceBtn=findViewById(R.id.serviceBtn);
        serviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(serviceStatus==false){
                    startServiceMethod();
                } else{
                    stopServiceMethod();
                }
            }
        });

        IntentFilter filter=new IntentFilter("com.example.services.SERVICE_ACTION");
        registerReceiver(mybr,filter);

        requestPermissions();
        new smsReceiver().setView(tv,serviceBtn,serviceStatus);
    }

    private void requestPermissions() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECEIVE_SMS},100);
        }
    }

    private void startServiceMethod() {
        tv.setText("Service started");
        serviceBtn.setText("Stop service");
        startService(new Intent(this, musicService.class));
        serviceStatus=true;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("ServiceStatus",serviceStatus);
        editor.apply();
    }

    private void stopServiceMethod() {
        tv.setText("Service stopped");
        serviceBtn.setText("Start service");
        stopService(new Intent(this, musicService.class));
        serviceStatus=false;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("ServiceStatus",serviceStatus);
        editor.apply();
    }



    @RequiresApi(api= Build.VERSION_CODES.O)
    private void createNotificationChannel(){
        NotificationChannel serviceChannel=new NotificationChannel(Channel_ID, "Music Service Channel", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager=null;
        manager=getSystemService(NotificationManager.class);
        manager.createNotificationChannel(serviceChannel);
    }

}