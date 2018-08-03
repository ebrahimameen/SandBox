package com.example.emsam.archcomp.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.example.emsam.archcomp.ArchComDatabase;
import com.example.emsam.archcomp.dao.UserInfoDao;
import com.example.emsam.archcomp.model.UserInfo;

import java.util.List;

public class DataRepository
{
    private static final String TAG = "Repo";
    private UserInfoDao userDao;
    private List<Class<?>> models;
    private LiveData<List<UserInfo>> allUsers;
    private LiveData<List<UserInfo>> usersInRange;


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

    public LiveData<List<UserInfo>> getUsersInRangeOf(int min, int max)
    {
        if (usersInRange == null)
        {
            usersInRange = userDao.getUsersInRangeOf(min, max);
        }

        return usersInRange;
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
