package com.thsai.skin;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import androidx.core.view.LayoutInflaterCompat;

import java.lang.reflect.Field;
import java.util.Observable;

public class SkinActivityLifecycle implements Application.ActivityLifecycleCallbacks {
    private Observable mObservable;
    private ArrayMap<Activity, SkinLayoutInflaterFactory> mLayoutInflaterFactories = new ArrayMap<>();

    public SkinActivityLifecycle(Observable observable) {
        mObservable = observable;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        //更新状态栏
//        SkinThemeUtils.updateStatusBarColor(activity);

        LayoutInflater layoutInflater = activity.getLayoutInflater();
        try {
            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
            field.setAccessible(true);
            field.setBoolean(layoutInflater, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SkinLayoutInflaterFactory skinLayoutInflaterFactory = new SkinLayoutInflaterFactory(activity);
        LayoutInflaterCompat.setFactory2(layoutInflater, skinLayoutInflaterFactory);
        mObservable.addObserver(skinLayoutInflaterFactory);
        mLayoutInflaterFactories.put(activity, skinLayoutInflaterFactory);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
