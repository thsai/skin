package com.thsai.example

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import androidx.core.app.ActivityCompat
import com.thsai.skin.SkinManager
import com.thsai.skin.utils.SkinPrefrence
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(
        "android.permission.READ_EXTERNAL_STORAGE",
        "android.permission.WRITE_EXTERNAL_STORAGE"
    )

    private var isRest: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        verifyStoragePermissions(this)
        isRest = TextUtils.isEmpty(SkinPrefrence.getInstance().skin)

        change.setOnClickListener {
            val skinFile = File(Environment.getExternalStorageDirectory(), "skin.apk")
            isRest = if (isRest) {
                SkinManager.getInstance().loadSkin(skinFile.absolutePath)
                false
            } else {
                SkinManager.getInstance().loadSkin(null)
                true
            }
        }
    }

    /**
     * 申请权限，为了要把外部文件写入到 手机内存中
     *
     * @param activity
     */
    private fun verifyStoragePermissions(activity: AppCompatActivity?) {
        try {
            //检测是否有写的权限

            val permission: Int = ActivityCompat.checkSelfPermission(
                activity!!,
                PERMISSIONS_STORAGE[1]
            )
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框

                ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
