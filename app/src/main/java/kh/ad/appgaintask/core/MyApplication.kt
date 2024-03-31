package kh.ad.appgaintask.core

import android.annotation.SuppressLint
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication

/**
 * This class is responsible for prevent multiDex(or 64k reference exceed) build error
 */
@SuppressLint("Registered")
class MyApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        MultiDex.install(applicationContext)
    }
}