package com.example.emsam.archcomp.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.example.emsam.archcomp.ArchComDatabase;
import com.example.emsam.archcomp.dao.UserInfoDao;
import com.example.emsam.archcomp.model.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

public class DataRepository
{
    private static final String TAG = "Repo";
    private static DatabaseReference fbDatabaseReference;
    private UserInfoDao userDao;
    private LiveData<List<UserInfo>> usersInRange;

    public DataRepository(Application application)
    {
        ArchComDatabase database = ArchComDatabase.getDatabase(application);
        userDao = database.getUserDao();
        fbDatabaseReference = FirebaseDatabase.getInstance().getReference("user_table");
        fbDatabaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                for (DataSnapshot child : dataSnapshot.getChildren())
                {
                    long id = Long.valueOf(child.child("id").getValue().toString());
                    String name = child.child("name").getValue().toString();
                    String email = child.child("email").getValue().toString();
                    int age = Integer.valueOf(child.child("age").getValue().toString());
                    UserInfo userInfo = new UserInfo.Builder().setId(id).setName(name).setEmail(email).setAge(age).build();

                    Log.d(TAG, "onDataChange: " + userInfo.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Log.d(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    public DataSource.Factory<Integer, UserInfo> getAllUsers()
    {
        return userDao.getAllUsers();
    }

    public LiveData<List<UserInfo>> getUsersInRangeOf(int min, int max)
    {
        if (usersInRange == null)
        {
            usersInRange = userDao.getUsersInRangeOf(min, max);
        }

        return usersInRange;
    }

    public void insertUser(UserInfo... userInfo)
    {
        new InsertUserTask(userDao).execute(userInfo);
    }

    public void deleteUser(UserInfo userInfo)
    {
        new DeleteUserTask(userDao).execute(userInfo);
    }

    public void deleteAllUsers()
    {
        new DeleteAllUserTask(userDao).execute();
    }

    private static class InsertUserTask extends AsyncTask<UserInfo, Void, Void>
    {

        private UserInfoDao localUserDao;

        InsertUserTask(UserInfoDao dao)
        {
            localUserDao = dao;
        }

        @Override
        protected Void doInBackground(final UserInfo... userInfo)
        {
            final long[] ids = localUserDao.insertUser(userInfo);
            for (int i = 0; i < userInfo.length; i++)
            {
                UserInfo user = userInfo[i];
                user.setId(ids[i]);

                // generate firebase id
                final String id = fbDatabaseReference.push().getKey();
                if (id != null)
                {
                    fbDatabaseReference.child(id).setValue(user);
                }
            }
            return null;
        }

    }

    private static class DeleteUserTask extends AsyncTask<UserInfo, Void, Void>
    {

        private UserInfoDao localUserDao;

        DeleteUserTask(UserInfoDao dao)
        {
            localUserDao = dao;
        }

        @Override
        protected Void doInBackground(final UserInfo... params)
        {
            localUserDao.deleteUser(params);
            return null;
        }
    }

    private static class DeleteAllUserTask extends AsyncTask<Void, Void, Void>
    {
        private UserInfoDao localUserDao;

        DeleteAllUserTask(UserInfoDao dao)
        {
            localUserDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            localUserDao.deleteAllUsers();
            return null;
        }
    }
}
