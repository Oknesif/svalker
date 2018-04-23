package com.zzzombiecoder.svalker.application

import android.util.Log
import com.crashlytics.android.Crashlytics
import timber.log.Timber

class CrashReportingTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (t != null) {
            if (priority == Log.ERROR) {
                Crashlytics.logException(t)
            }
        }
    }
}