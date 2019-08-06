package com.thsai.example;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyView extends TextView {
    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
