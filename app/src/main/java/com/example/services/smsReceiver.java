package com.example.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.Button;
import android.widget.TextView;

public class smsReceiver extends BroadcastReceiver {
    // need to include <receiver android:name=".smsReceiver"> and <action android:name="android.provider.Telephony.SMS_RECEIVED"/> in Manifest
    // permission for READ_SMS and RECEIVE_SMS
    private static TextView tv;
    private static Button serviceBtn;
    SharedPreferences sharedPreferences;
    boolean serviceStatus;

    public void setView(TextView tv, Button serviceBtn, boolean serviceStatus) {
        this.tv=tv;
        this.serviceBtn=serviceBtn;
        this.serviceStatus = serviceStatus;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        serviceStatus=sharedPreferences.getBoolean("ServiceStatus",false);
        SmsMessage[] smsMessages= Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for(SmsMessage smsMessage:smsMessages){
            String message_body=smsMessage.getMessageBody();
            String service_action=message_body.split(":")[1];
            tv.setText(service_action);
            if(service_action.equals("play")){
                if(serviceStatus==false){
                    startServiceMethod(context);
                } else{
                    stopServiceMethod(context);
                }
            }
        }
    }

    private void startServiceMethod(Context context) {
        tv.setText("Service started");
        serviceBtn.setText("Stop service");
        context.startService(new Intent(context, musicService.class));
        serviceStatus=true;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("ServiceStatus",serviceStatus);
        editor.apply();
    }

    private void stopServiceMethod(Context context) {
        tv.setText("Service stopped");
        serviceBtn.setText("Start service");
        context.stopService(new Intent(context, musicService.class));
        serviceStatus=false;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("ServiceStatus",serviceStatus);
        editor.apply();
    }
}
