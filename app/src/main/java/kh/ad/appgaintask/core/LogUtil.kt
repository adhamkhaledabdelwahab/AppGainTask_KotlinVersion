package kh.ad.appgaintask.core

import android.util.Log
import kh.ad.appgaintask.BuildConfig

object LogUtil {
    fun d(tag: String?, message: String?) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message!!)
        }
    }
}