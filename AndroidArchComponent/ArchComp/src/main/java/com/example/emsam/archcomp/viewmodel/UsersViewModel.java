package com.example.emsam.archcomp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.emsam.archcomp.UserInfoGenerator;
import com.example.emsam.archcomp.model.UserInfo;

import java.util.List;

public class UsersViewModel extends AndroidViewModel
{
    private UserInfoGenerator dataGenerator;
    private LiveData<List<UserInfo>> allUsers;

    public UsersViewModel(@NonNull Application application)
    {
        super(application);
        dataGenerator = new UserInfoGenerator(application);
        new Thread(dataGenerator).start();

        allUsers = dataGenerator.getListUserInfo();
    }

    public LiveData<List<UserInfo>> getUsers()
    {
        return allUsers;
    }

    public void start()
    {
        dataGenerator.resume();
    }

    public void stop()
    {
        dataGenerator.pause();
    }

    /**
     * @return true if resume, false if pause.
     */
    public boolean toggle()
    {
        if (dataGenerator.isPaused())
        {
            dataGenerator.resume();
        }
        else
        {
            dataGenerator.pause();
        }
        return !dataGenerator.isPaused();
    }
}
