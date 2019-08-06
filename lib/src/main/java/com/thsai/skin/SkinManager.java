package com.thsai.skin;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;
import com.thsai.skin.utils.SkinPrefrence;
import com.thsai.skin.utils.SkinResources;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Observable;

public class SkinManager extends Observable {
    //activity声明周期回调
    private SkinActivityLifecycle skinActivityLifecycle;
    private Application mContext;


    private static SkinManager instance;

    private SkinManager(Application application) {
        mContext = application;
        SkinPrefrence.init(application);
        SkinResources.init(application);
        skinActivityLifecycle = new SkinActivityLifecycle(this);
        application.registerActivityLifecycleCallbacks(skinActivityLifecycle);
        //加载上次使用保存的皮肤
        loadSkin(SkinPrefrence.getInstance().getSkin());
    }

    public static SkinManager getInstance() {
        return instance;
    }

    public static void init(Application application) {
        if (instance == null) {
            synchronized (SkinManager.class) {
                if (instance == null) {
                    instance = new SkinManager(application);
                }
            }
        }
    }

    /**
     * 加载皮肤应用
     *
     * @param skinPath 皮肤路径 如果为空则使用默认皮肤
     */
    public void loadSkin(String skinPath) {
        if (TextUtils.isEmpty(skinPath) || !new File(skinPath).exists()) {
            //还原默认皮肤
            SkinPrefrence.getInstance().reset();
            SkinResources.getInstance().reset();
        } else {
            try {
                //宿主的resources
                Resources appResources = mContext.getResources();

                //反射创建AssetManager与Resource
                AssetManager assetManager = AssetManager.class.newInstance();
                Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
                addAssetPath.invoke(assetManager, skinPath);

                Resources skinResource = new Resources(assetManager, appResources.getDisplayMetrics(), appResources.getConfiguration());
                //获取外部APK包名
                PackageManager packageManager = mContext.getPackageManager();
                PackageInfo info = packageManager.getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES);
                String packageName = info.packageName;
                SkinResources.getInstance().applySkin(skinResource, packageName);

                //记录
                SkinPrefrence.getInstance().setSkin(skinPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //通知采集的view更新皮肤
        //被观察者改变，通知所有观察者
        setChanged();
        notifyObservers(null);
    }
}
