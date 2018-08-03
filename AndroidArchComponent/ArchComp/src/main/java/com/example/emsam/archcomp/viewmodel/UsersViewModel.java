package com.example.emsam.archcomp.viewmodel;

import java.util.List;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.NonNull;

import com.example.emsam.archcomp.UserInfoGenerator;
import com.example.emsam.archcomp.model.UserInfo;
import com.example.emsam.archcomp.repository.DataRepository;

public class UsersViewModel extends AndroidViewModel implements LifecycleObserver
{

    private LiveData<List<UserInfo>> allUsers;
    private DataRepository dataRepository;
    private Lifecycle lifecycle;

    // TODO: 03.08.18 CLEANING UP.
    // ######################################################################
    // Data generation should be part of the repo implementation in order to exclude the ViewModel implementation
    // from the content fetching.
    private UserInfoGenerator dataGenerator;
    private Thread dataGeneratorThread;
    // ######################################################################

    public UsersViewModel(@NonNull Application application)
    {
        super(application);
        dataRepository = new DataRepository(application);
        dataGenerator = new UserInfoGenerator(dataRepository);
        dataGeneratorThread = new Thread(dataGenerator);
        dataGeneratorThread.start();

//        allUsers = dataRepository.getAllUsers(); //dataGenerator.getListUserInfo();
        allUsers = dataRepository.getUsersInRangeOf(20,30); //dataGenerator.getListUserInfo();
    }

    public LiveData<List<UserInfo>> getUsers()
    {
        return allUsers;
    }

    /**
     * Force start generator.
     */
    public void start()
    {
        dataGenerator.enabled(true);
        dataGenerator.resume();
    }

    /**
     * Force stop generator.
     */
    public void stop()
    {
        dataGenerator.pause();
        dataGenerator.enabled(false);
    }

    /**
     * @return true if resume, false if pause.
     */
    public boolean toggle()
    {
        if (dataGenerator.isPaused())
        {
            // force start
            start();
        }
        else
        {
            // force stop
            stop();
        }
        return !dataGenerator.isPaused();
    }

    public void setLifecycle(final Lifecycle lifecycle)
    {
        this.lifecycle = lifecycle;
       lifecycle.addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE) void onLifecyclePause()
    {
        dataGenerator.pause();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME) void onLifecycleResume()
    {
        dataGenerator.resume();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY) void cleanup()
    {
        if (lifecycle != null)
        {
            lifecycle.removeObserver(this);
        }

        if (dataGeneratorThread != null)
        {
            dataGeneratorThread.interrupt();
        }
    }
}
