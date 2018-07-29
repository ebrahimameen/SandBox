package com.example.emsam.archcomp;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.emsam.archcomp.dao.UserInfoDao;
import com.example.emsam.archcomp.model.UserInfo;

@Database(entities = {UserInfo.class}, version = 1)
public abstract class ArchComDatabase extends RoomDatabase
{
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
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ArchComDatabase.class, "archcomp_db.sqlite").build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract UserInfoDao getUserDao();
}
