package com.example.emsam.archcomp.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.example.emsam.archcomp.ArchComDatabase;
import com.example.emsam.archcomp.dao.UserInfoDao;
import com.example.emsam.archcomp.model.UserInfo;

import java.util.List;

public class DataRepository
{
    private UserInfoDao userDao;
    private List<Class<?>> models;
    private LiveData<List<UserInfo>> allUsers;

    public DataRepository(Application application)
    {
        ArchComDatabase database = ArchComDatabase.getDatabase(application);
        userDao = database.getUserDao();
        allUsers = userDao.getAllUsers();
    }

    public LiveData<List<UserInfo>> getAllUsers()
    {
        return allUsers;
    }

    public void insertUser(UserInfo userInfo)
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

    public LiveData<List<UserInfo>> getUsersInRangeOf(int min, int max)
    {
        // TODO: 7/29/2018  
        return null; //new FetchUserInRangeTask(userDao).execute(Integer.valueOf(min) ,Integer.valueOf(max));
    }

    private static class InsertUserTask extends AsyncTask<UserInfo, Void, Void>
    {

        private UserInfoDao localUserDao;

        InsertUserTask(UserInfoDao dao)
        {
            localUserDao = dao;
        }

        @Override
        protected Void doInBackground(final UserInfo... params)
        {
            localUserDao.insertUser(params[0]);
            return null;
        }
    }

    private static class FetchUserInRangeTask extends AsyncTask<Integer, Void,  LiveData<List<UserInfo>>>
    {
        private static final String TAG = "FetchUserInRangeTask";
        private UserInfoDao localUserDao;

        FetchUserInRangeTask(UserInfoDao dao)
        {
            localUserDao = dao;
        }

        @Override
        protected LiveData<List<UserInfo>> doInBackground(Integer... age)
        {
            if (age.length < 2 || age.length > 2)
            {
                Log.e(TAG, "doInBackground: invalid range: " + age.length );
            }
            return localUserDao.getUsersInRangeOf(age[0], age[1]);
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
            localUserDao.deleteUser(params[0]);
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
