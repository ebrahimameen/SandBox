package com.example.emsam.archcomp;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;


public class UserDataGenerator implements Runnable
{
    private static final String TAG = "Gen";
    private final AtomicBoolean pauseFlag = new AtomicBoolean(true);
    private static int counter;
    private final Random random = new Random();
    private int MIN = 18;
    private int MAX = 80;
    private MutableLiveData<List<UserData>> liveData;

    public UserDataGenerator()
    {
        liveData = new MutableLiveData<>();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void run()
    {
        final List<UserData>listUserData = new ArrayList<>();
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
                UserData user = new UserData(String.format("User Element_%d", (++counter)), age);
                listUserData.add(user);
                liveData.postValue(listUserData);
//                Log.d(TAG, "run: "+ user.toString());
                Thread.sleep(2500);
            } catch (InterruptedException e)
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

    public LiveData<List<UserData>> getListUserData()
    {
        return liveData;
    }

    public boolean isPaused()
    {
        return pauseFlag.get();
    }
}
