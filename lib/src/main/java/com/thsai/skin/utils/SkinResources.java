package com.thsai.skin.utils;

import android.app.Application;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

public class SkinResources {
    private boolean isDefaultSkin;
    private static SkinResources instance;
    private Resources mAppResources, mSkinResources;
    private Context mContext;

    private String mSkinPkgName;

    private SkinResources(Application application) {
        mContext = application;
        mAppResources = application.getResources();
    }

    public static SkinResources getInstance() {
        return instance;
    }

    public static void init(Application application) {
        if (instance == null) {
            synchronized (SkinResources.class) {
                if (instance == null) {
                    instance = new SkinResources(application);
                }
            }
        }
    }


    /**
     * 通过原始app中的resid获取其对应的resources中的名字，类型
     * 从而根据名字和类型获取到对应的皮肤包中的信息
     *
     * @param resId
     * @return
     */
    public int getIdentifier(int resId) {
        if (isDefaultSkin)
            return resId;
        //在皮肤包中不一定就是当前程序的   id
        //获取对应id在当前的名称 colorPrimary
        String resName = mAppResources.getResourceEntryName(resId);
        String resType = mAppResources.getResourceTypeName(resId);
        return mSkinResources.getIdentifier(resName, resType, mSkinPkgName);
    }

    public int getColor(int resId) {
        if (isDefaultSkin) {
            return mAppResources.getColor(resId);
        }
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return mAppResources.getColor(resId);
        }
        return mSkinResources.getColor(skinId);
    }

    public ColorStateList getColorStateList(int resId) {
        if (isDefaultSkin) {
            return mAppResources.getColorStateList(resId);
        }
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return mAppResources.getColorStateList(resId);
        }
        return mSkinResources.getColorStateList(skinId);
    }

    public Drawable getDrawable(int resId) {
        if (isDefaultSkin) {
            return mAppResources.getDrawable(resId);
        }
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return mAppResources.getDrawable(resId);
        }
        return mSkinResources.getDrawable(skinId);
    }

    public void applySkin(Resources skinResource, String packageName) {
        this.mSkinResources = skinResource;
        this.mSkinPkgName = packageName;
        isDefaultSkin = false;
    }

    public void reset() {
        isDefaultSkin = true;
    }
}
