package com.thsai.skin;

import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import com.thsai.skin.utils.SkinResources;

import java.util.ArrayList;
import java.util.List;

public class SkinAttribute {
    private static final List<String> mAttributes = new ArrayList<>();

    static {
        mAttributes.add("background");
        mAttributes.add("src");
        mAttributes.add("textColor");
        mAttributes.add("drawableLeft");
        mAttributes.add("drawableTop");
        mAttributes.add("drawableRight");
        mAttributes.add("drawableBottom");
    }

    //记录换肤需要操作的view与属性信息
    private List<SkinView> mSkinViews = new ArrayList<>();

    public void look(View view, AttributeSet attrs) {
        List<SkinPair> mSkinPairs = new ArrayList<>();

        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            //获得属性名
            String attributeName = attrs.getAttributeName(i);
            if (mAttributes.contains(attributeName)) {
                //#
                //8537874835
                //@8537874835
                String attributeValue = attrs.getAttributeValue(i);
                //比如color以#开头表示写死的颜色，不可用与换肤
                if (attributeValue.startsWith("#")) {
                    continue;
                }
                int resId;
                //以？开头的表示使用属性
                if (attributeValue.startsWith("?")) {
                    int attrId = Integer.parseInt(attributeValue.substring(1));
//                    resId = SkinThemeUtils.getResId(view.getContext(), new int[]{attrId})[0];
                    resId = Integer.parseInt(attributeValue.substring(1));
                } else {
                    //正常以@开头
                    resId = Integer.parseInt(attributeValue.substring(1));
                }
                Log.e("skin", "  " + attributeName + " = " + attributeValue);
                SkinPair skinPair = new SkinPair(attributeName, resId);
                mSkinPairs.add(skinPair);
            }
        }

        if (!mSkinPairs.isEmpty()) {
            SkinView skinView = new SkinView(view, mSkinPairs);
            //如果选择过皮肤，调用一次applySkin加载皮肤的资源
            skinView.applySkin();
            mSkinViews.add(skinView);
        }
    }

    //对view中的所有属性进行修改
    public void applySkin() {
        for (SkinView mSkinView : mSkinViews) {
            mSkinView.applySkin();
        }
    }


    static class SkinView {
        View view;
        //这个view的能被换肤的属性与它对应的id集合
        List<SkinPair> skinPairs;

        public SkinView(View view, List<SkinPair> skinPairs) {
            this.view = view;
            this.skinPairs = skinPairs;
        }

        //对view中的所有属性进行修改
        public void applySkin() {
            for (SkinPair skinPair : skinPairs) {
                Drawable left = null, top = null, right = null, bottom = null;
                switch (skinPair.attributeName) {
                    case "background":
//                        Object background = SkinResources.getInstance().getBackground(skinPair.resId);
//                        //背景可能是@color也可能是@drawable
//                        if (background instanceof Integer) {
//                            view.setBackgroundColor((int) background);
//                        } else {
//                            ViewCompat.setBackground(view, (Drawable) background);
//                        }
                        ViewCompat.setBackground(view, SkinResources.getInstance().getDrawable(skinPair.resId));
                        break;
                    case "src":
//                        background = SkinResources.getInstance().getBackground(skinPair.resId);
//                        if (background instanceof Integer) {
//                            ((ImageView) view).setImageDrawable(new ColorDrawable((Integer) background));
//                        } else {
//                            ((ImageView) view).setImageDrawable((Drawable) background);
//                        }
                        ((ImageView) view).setImageDrawable(SkinResources.getInstance().getDrawable(skinPair.resId));
                        break;
                    case "textColor":
                        ((TextView) view).setTextColor(SkinResources.getInstance().getColorStateList(skinPair.resId));
                        break;
                    case "drawableLeft":
                        left = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                    case "drawableTop":
                        top = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                    case "drawableRight":
                        right = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                    case "drawableBottom":
                        bottom = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                    default:
                        break;
                }
                if (null != left || null != right || null != top || null != bottom) {
                    ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
                }
            }
        }
    }

    static class SkinPair {
        //属性名
        String attributeName;
        //对应的资源ID
        int resId;

        public SkinPair(String attributeName, int resId) {
            this.attributeName = attributeName;
            this.resId = resId;
        }
    }
}
