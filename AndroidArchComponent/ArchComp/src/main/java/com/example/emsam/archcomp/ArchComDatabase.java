package com.example.emsam.archcomp;

import android.content.Context;
import android.util.Log;

import com.example.emsam.archcomp.dao.UserInfoDao;
import com.example.emsam.archcomp.model.UserInfo;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {UserInfo.class}, version = 1)
public abstract class ArchComDatabase extends RoomDatabase
{
    private static final String TAG = ArchComDatabase.class.getName();
    private static ArchComDatabase INSTANCE;

    public static ArchComDatabase getDatabase(final Context context)
    {
        if (INSTANCE == null)
        {
            synchronized (ArchComDatabase.class)
            {
                //INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ArchComDatabase.class, "archcomp_db").build();
                if (INSTANCE == null)
                {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ArchComDatabase.class, "archcomp_db.sqlite").addCallback(new Callback()
                    {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db)
                        {
                            super.onCreate(db);
                            Log.d(TAG, "onCreate()");
                        }

                        @Override
                        public void onOpen(@NonNull SupportSQLiteDatabase db)
                        {
                            super.onOpen(db);
                            Log.d(TAG, "onOpen()");
                        }
                    }).build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract UserInfoDao getUserDao();
}
