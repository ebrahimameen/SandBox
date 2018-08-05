package com.example.emsam.archcomp.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.os.AsyncTask;

import com.example.emsam.archcomp.ArchComDatabase;
import com.example.emsam.archcomp.dao.UserInfoDao;
import com.example.emsam.archcomp.model.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class DataRepository
{
    private static final String TAG = "Repo";
    private UserInfoDao userDao;
    private LiveData<List<UserInfo>> usersInRange;

    private static DatabaseReference fbDatabaseReference;

    public DataRepository(Application application)
    {
        ArchComDatabase database = ArchComDatabase.getDatabase(application);
        userDao = database.getUserDao();
        fbDatabaseReference = FirebaseDatabase.getInstance().getReference("user_table");
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
