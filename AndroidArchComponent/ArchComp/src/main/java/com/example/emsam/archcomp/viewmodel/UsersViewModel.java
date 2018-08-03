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
        dataRepository = new DataRepository(application);
        dataGenerator = new UserInfoGenerator(dataRepository);
        new Thread(dataGenerator).start();

//        allUsers = dataRepository.getAllUsers(); //dataGenerator.getListUserInfo();
        allUsers = dataRepository.getUsersInRangeOf(70,70); //dataGenerator.getListUserInfo();
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
