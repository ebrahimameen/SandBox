package com.example.emsam.archcomp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.emsam.archcomp.UserInfoGenerator;
import com.example.emsam.archcomp.model.UserInfo;
import com.example.emsam.archcomp.repository.DataRepository;

import java.util.List;

public class UsersViewModel extends AndroidViewModel
{
    private UserInfoGenerator dataGenerator;
    private LiveData<List<UserInfo>> allUsers;
    private DataRepository dataRepository;

    public UsersViewModel(@NonNull Application application)
    {
        super(application);
        dataGenerator = new UserInfoGenerator();
        new Thread(dataGenerator).start();

        dataRepository = new DataRepository(application);
        allUsers = dataRepository.getAllUsers(); //dataGenerator.getListUserInfo();
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
