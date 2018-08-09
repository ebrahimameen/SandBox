package com.example.emsam.archcomp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;

public class MessagingService extends FirebaseMessagingService
{
    private static final String TAG = "FBM:Service";
    public MessagingService()
    {
    }

    @Override
    public void onNewToken(String s)
    {
        super.onNewToken(s);
        Log.d(TAG, "onNewToken: " + s);
    }
}
