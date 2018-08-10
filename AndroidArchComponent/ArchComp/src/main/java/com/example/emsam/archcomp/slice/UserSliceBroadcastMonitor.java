package com.example.emsam.archcomp.slice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.example.emsam.archcomp.view.MainActivity.sLastAddedUser;
import static com.example.emsam.archcomp.view.MainActivity.updateUser;

public class UserSliceBroadcastMonitor extends BroadcastReceiver
{
    public static String ACTION_ON_USER_UPDATE = "com.example.emsam.archcomp.ACTION_ON_USER_UPDATE";
    public static String EXTRA_NEW_USER = "com.example.emsam.archcomp.EXTRA_NEW_USER";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();

        if (ACTION_ON_USER_UPDATE.equals(action) && intent.getExtras() != null)
        {
            String newValue = intent.getExtras().getString(EXTRA_NEW_USER, sLastAddedUser.getEmail());
            updateUser(context, newValue);
        }
    }
}
