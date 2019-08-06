package com.thsai.example

import android.app.Application
import com.thsai.skin.SkinManager

class App :Application(){

    override fun onCreate() {
        super.onCreate()
        SkinManager.init(this)
    }
}