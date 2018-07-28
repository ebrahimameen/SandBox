package com.example.emsam.archcomp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class UsersViewModel extends AndroidViewModel
{
    private UserDataGenerator dataGenerator;
    private LiveData<List<UserData>> allUsers;

    public UsersViewModel(@NonNull Application application)
    {
        super(application);
        dataGenerator = new UserDataGenerator();
        new Thread(dataGenerator).start();

        allUsers = dataGenerator.getListUserData();
    }

    public LiveData<List<UserData>> getUsers()
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
        } else
        {
            dataGenerator.pause();
        }
        return !dataGenerator.isPaused();
    }
}
