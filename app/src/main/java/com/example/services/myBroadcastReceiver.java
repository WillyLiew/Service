package com.example.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class myBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if("com.example.services.SERVICE_ACTION".equals(intent.getAction())){
            String text=intent.getStringExtra("com.example.services.EXTRA_TEXT");
            Toast.makeText(context,"Received text from broadcast receiver: "+text,Toast.LENGTH_SHORT).show();
        }
    }
}
