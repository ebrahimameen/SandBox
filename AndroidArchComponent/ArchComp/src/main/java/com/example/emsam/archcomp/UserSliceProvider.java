package com.example.emsam.archcomp;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.emsam.archcomp.R;
import com.example.emsam.archcomp.view.MainActivity;

import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.Slice;
import androidx.slice.SliceProvider;
import androidx.slice.builders.ListBuilder;
import androidx.slice.builders.SliceAction;

public class UserSliceProvider extends SliceProvider
{
    private static int sReqCode = 0;
    private Context context;

    public static Uri getUri(Context context, String path)
    {
        return new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(context.getPackageName()).appendPath(path).build();
    }

    @Override
    public boolean onCreateSliceProvider() {
        context = getContext();
        return true;
    }

    @Override
    public Slice onBindSlice(Uri sliceUri)
    {
        //slice-content://com.example.emsam.archcomp/userinfo
        if (sliceUri.getPath().contains("userinfo"))
        {
            return createUser(sliceUri);
        }
        return null;
    }

    private Slice createUser(Uri sliceUri)
    {
        // Define the actions used in this slice
//        SliceAction tempUp = new SliceAction(getChangeTempIntent(sTemperature + 1), IconCompat.createWithResource(context, R.drawable.ic_temp_up).toIcon(), "Increase temperature");
//        SliceAction tempDown = new SliceAction(getChangeTempIntent(sTemperature - 1), IconCompat.createWithResource(context, R.drawable.ic_temp_down).toIcon(), "Decrease temperature");

        // Construct our parent builder
        ListBuilder listBuilder = new ListBuilder(context, sliceUri);

        // Construct the builder for the row
        ListBuilder.RowBuilder userRow = new ListBuilder.RowBuilder(listBuilder);

        // Set title
        userRow.setTitle(MainActivity.getLastAddedUser(context));

//        // Add the actions to appear at the end of the row
//        userRow.addEndItem(tempDown);
//        userRow.addEndItem(tempUp);

        // Set the primary action; this will activate when the row is tapped
        Intent intent = new Intent(getContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), sliceUri.hashCode(), intent, 0);
        SliceAction openUserActivity = new SliceAction(pendingIntent, IconCompat.createWithResource(context, R.drawable.ic_message).toIcon(), "User Info");
        userRow.setPrimaryAction(openUserActivity);

        // Add the row to the parent builder
        listBuilder.addRow(userRow);

        // Build the slice
        return listBuilder.build();
    }

//    private PendingIntent getChangeTempIntent(int value)
//    {
//        Intent intent = new Intent(UserSliceBroadcastMonitor.ACTION_ON_USER_UPDATE);
//        intent.setClass(context, UserSliceBroadcastMonitor.class);
//        intent.putExtra(UserSliceBroadcastMonitor.EXTRA_NEW_USER, value);
//        return PendingIntent.getBroadcast(getContext(), sReqCode++, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//    }
}
