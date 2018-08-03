package com.example.emsam.archcomp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.emsam.archcomp.model.UserInfo;
import com.example.emsam.archcomp.repository.DataRepository;


public class UserInfoGenerator implements Runnable, LifecycleObserver
{
    private static final String TAG = "Gen";
    private static final int MIN = 20;
    private static final int MAX = 80;
    private static int counter;

    // flag for pause/resume only if enabled
    private final AtomicBoolean pauseFlag = new AtomicBoolean(true);

    // flag to enable/disable data generation
    private final AtomicBoolean isEnabled = new AtomicBoolean(false);

    // by default the thread keeps alive until force terminate is triggered.
    private final AtomicBoolean forceTerminate = new AtomicBoolean(false);

    private final Random random = new Random();
    private MutableLiveData<List<UserInfo>> liveData;
    private DataRepository repository;
    private Lifecycle lifecycle;

    public UserInfoGenerator(DataRepository repository, @Nullable Lifecycle lifecycle)
    {
        this.repository = repository;
        setLifecycle(lifecycle);

        liveData = new MutableLiveData<>();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void run()
    {
        final List<UserInfo> userInfos = new ArrayList<>();
        while (!forceTerminate.get())
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
                Log.d(TAG, "resume(): invoke resume!");
                pauseFlag.notify();
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

    // region Lifecycle
    public void setLifecycle(@Nullable Lifecycle lifecycle)
    {
        this.lifecycle = lifecycle;
        if (lifecycle != null)
        {
            lifecycle.addObserver(this);
        }
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE) void onLifecyclePause()
    {
        pause();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME) void onLifecycleResume()
    {
        resume();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY) void cleanup()
    {
        if (lifecycle != null)
        {
            lifecycle.removeObserver(this);
        }

        forceTerminate.set(true);
    }
    // endregion
}
