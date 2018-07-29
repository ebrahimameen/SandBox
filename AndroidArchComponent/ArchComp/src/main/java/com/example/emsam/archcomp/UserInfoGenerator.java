package com.example.emsam.archcomp;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.emsam.archcomp.model.UserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;


public class UserInfoGenerator implements Runnable
{
    private static final String TAG = "Gen";
    private static final int MIN = 18;
    private static final int MAX = 80;
    private static int counter;
    private final AtomicBoolean pauseFlag = new AtomicBoolean(true);
    private final Random random = new Random();
    private MutableLiveData<List<UserInfo>> liveData;

    public UserInfoGenerator()
    {
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
                        Log.d(TAG, "run: Pause");
                        pauseFlag.wait();
                    }
                }
                int age = random.nextInt(MAX - MIN) + MIN;
                UserInfo user = new UserInfo(String.format("User Element_%d", (++counter)), age);
                userInfos.add(user);
                liveData.postValue(userInfos);
                Thread.sleep(2500);
            }
            catch (InterruptedException e)
            {
                Log.e(TAG, "run: ", e);
            }
        }
    }

    public void pause()
    {
        pauseFlag.set(true);
    }

    public void resume()
    {
        pauseFlag.set(false);
        synchronized (pauseFlag)
        {
            pauseFlag.notify();
            Log.d(TAG, "resume: Resume");
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
}
