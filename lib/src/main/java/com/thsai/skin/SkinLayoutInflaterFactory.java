package com.thsai.skin;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class SkinLayoutInflaterFactory implements LayoutInflater.Factory2, Observer {
    //安卓里面控件的包名,这个变量是为了下面代码里，反射创建类的class而预备的
    private static final String[] prefixs = new String[]{
            "android.widget.",
            "android.view.",
            "android.webkit.",
            "android.app."
    };
    //记录对应view的构造函数
    private static final Class<?>[] mConstructorSignature = new Class[]{Context.class, AttributeSet.class};
    //用映射，将View的反射构造函数都存起来
    private static final HashMap<String, Constructor<? extends View>> sConstructorMap = new HashMap<String, Constructor<? extends View>>();

    private SkinAttribute skinAttribute;
    private Activity activity;

    //当选择新皮肤后需要替换view与之对应的属性
    //页面属性管理器
    public SkinLayoutInflaterFactory(Activity activity) {
        this.activity = activity;
        skinAttribute = new SkinAttribute();
    }

    @Override
    public void update(Observable o, Object arg) {
        skinAttribute.applySkin();
    }


    /**
     * 创建对应布局并且返回
     *
     * @param parent  当前TAG父布局
     * @param name    在布局中的TAG，如TextView
     * @param context 上下文
     * @param attrs   对应TAG中的属性，如android:text
     * @return View      null则由系统创建
     */
    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        //换肤就是在需要时替换view的属性（src、background等）
        //所以这里创建view，从而修改view属性
        View view = createSDKView(name, context, attrs);
        if (null == view) {
            view = createView(name, context, attrs);
        }
        if (null != view) {
            Log.v("skin", String.format("[%s] 筛选：%s", context.getClass().getName(), name));
            //加载属性
            skinAttribute.look(view, attrs);
        }
        return view;
    }

    private View createView(String name, Context context, AttributeSet attrs) {
        Constructor<? extends View> constructor = findConstructor(context, name);
        try {
            return constructor.newInstance(context, attrs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Constructor<? extends View> findConstructor(Context context, String name) {
        Constructor<? extends View> constructor = sConstructorMap.get(name);
        Log.v("skin", "findConstructor  " + name);
        if (constructor == null) {
            try {
                Class<? extends View> clazz = context.getClassLoader().loadClass(name).asSubclass(View.class);
                constructor = clazz.getConstructor(mConstructorSignature);
                sConstructorMap.put(name, constructor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return constructor;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return null;
    }

    private View createSDKView(String name, Context context, AttributeSet attrs) {
        //如果包含，则不是SDK中的view 可能是自定义view包括support库中的view
        if (name.contains(".")) {
            return null;
        }
        //不包含就要在解析的及诶单name前拼上：android.widget等尝试去反射
        for (String prefix : prefixs) {
            View view = createView(prefix + name, context, attrs);
            if (view != null)
                return view;
        }
        return null;
    }
}
