package com.example.emsam.archcomp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.emsam.archcomp.model.UserInfo;
import com.example.emsam.archcomp.repository.DataRepository;


public class UserInfoGenerator implements Runnable
{
    private static final String TAG = "Gen";
    private static final int MIN = 20;
    private static final int MAX = 80;
    private static int counter;
    private final AtomicBoolean pauseFlag = new AtomicBoolean(true);
    private final Random random = new Random();
    private MutableLiveData<List<UserInfo>> liveData;
    private DataRepository repository;
    private AtomicBoolean isEnabled = new AtomicBoolean(false);

    public UserInfoGenerator(DataRepository repository)
    {
        this.repository = repository;
        liveData = new MutableLiveData<>();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void run()
    {
        final List<UserInfo> userInfos = new ArrayList<>();
        while (!Thread.currentThread().isInterrupted())
        {
            try
            {
                if (pauseFlag.get())
                {
                    synchronized (pauseFlag)
                    {
                        Log.d(TAG, "run: Paused");
                        pauseFlag.wait();
                    }
                }
                int age = random.nextInt(MAX - MIN) + MIN;
                UserInfo user = new UserInfo(String.format("User Element_%d", (++counter)), age);
                userInfos.add(user);
                repository.insertUser(user);
                liveData.postValue(userInfos);
                Log.d(TAG, "run: " + user);
                Thread.sleep(2500);
            }
            catch (InterruptedException e)
            {
                Log.e(TAG, "run: ", e);
                break;
            }
        }

        Log.d(TAG, "run: UserInfoGenerator/terminated!");
    }

    public void pause()
    {
        if (isEnabled())
        {
            pauseFlag.set(true);
        }
        else
        {
            Log.d(TAG, "pause() -> operation not allowed, is protected!");
        }
    }

    public void resume()
    {
        if (isEnabled())
        {
            pauseFlag.set(false);
            synchronized (pauseFlag)
            {
                pauseFlag.notify();
                Log.d(TAG, "resume(): Resumed!");
            }
        }
        else
        {
            Log.d(TAG, "resume() -> operation not allowed, is protected!");
        }
    }

    public LiveData<List<UserInfo>> getListUserInfo()
    {
        return liveData;
    }

    public boolean isPaused()
    {
        return pauseFlag.get();
    }

    public void enabled(final boolean enable)
    {
        this.isEnabled.set(enable);
    }

    public boolean isEnabled()
    {
        return isEnabled.get();
    }
}
