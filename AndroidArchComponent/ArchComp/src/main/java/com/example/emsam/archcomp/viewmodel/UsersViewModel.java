package com.example.emsam.archcomp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.example.emsam.archcomp.UserInfoGenerator;
import com.example.emsam.archcomp.model.UserInfo;
import com.example.emsam.archcomp.repository.DataRepository;

public class UsersViewModel extends AndroidViewModel implements LifecycleObserver
{

    private LiveData<PagedList<UserInfo>> allUsers;
    private DataRepository dataRepository;

    // TODO: 03.08.18 CLEANING UP.
    // ######################################################################
    // Data generation should be part of the repo implementation in order to exclude the ViewModel implementation
    // from the content fetching.
    private UserInfoGenerator dataGenerator;
    // ######################################################################

    public UsersViewModel(@NonNull Application application)
    {
        super(application);
        dataRepository = new DataRepository(application);
        dataGenerator = new UserInfoGenerator(dataRepository, null);
        new Thread(dataGenerator).start();

        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(20)
                .setInitialLoadSizeHint(60)
                .setPrefetchDistance(10)
                .setEnablePlaceholders(true)
                .build();

        if (allUsers == null)
        {
            DataSource.Factory factory = dataRepository.getAllUsers();
            // TODO: 03.08.18 Important to convert PagedList to LiveData
            allUsers = new LivePagedListBuilder<Integer, UserInfo>(factory, config).build();
        }

        // allUsers = dataRepository.getAllUsers(); //dataGenerator.getListUserInfo();
        //        allUsers = dataRepository.getUsersInRangeOf(20, 30); //dataGenerator.getListUserInfo();
    }

    /**
     * TODO:03.08.18: can be added to the constructor.
     *
     * @param lifecycle
     *         lifecycle awareness.
     */
    public void setLifecycle(final Lifecycle lifecycle)
    {
        dataGenerator.setLifecycle(lifecycle);
    }

    public LiveData<PagedList<UserInfo>> getUsers()
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

    @Override
    protected void onCleared()
    {
        super.onCleared();
    }
}
