package com.thsai.skin.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class SkinPrefrence {
    private static final String NAME = "name";
    private static final String KEY_PATH = "path";
    private static SkinPrefrence instance;
    private Context mContext;

    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    private SkinPrefrence(Application application) {
        mContext = application;
        sharedPreferences = application.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static SkinPrefrence getInstance() {
        return instance;
    }

    public static void init(Application application) {
        if (instance == null) {
            synchronized (SkinResources.class) {
                if (instance == null)
                    instance = new SkinPrefrence(application);
            }
        }
    }

    public String getSkin() {
        return sharedPreferences.getString(KEY_PATH, "");
    }

    public void setSkin(String skinPath) {
        editor.putString(KEY_PATH, skinPath).apply();
    }

    public void reset() {
        editor.putString(KEY_PATH, "").apply();
    }
}
