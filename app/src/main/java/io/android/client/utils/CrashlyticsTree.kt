/*
 * *
 *  * Created by kevingitonga on 27/12/2021, 16:57
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 27/12/2021, 16:30
 *
 *//*


package io.android.client.utils

import android.util.Log
import com.google.firebase.crashlytics.BuildConfig
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

*/
/**
 *  CrashlyticsTree
 *
 *  This is a utility class borrowed from the Google IO19 Android App. It allows us to log/send logs from (Timber)[https://github.com/JakeWharton/timber] to Firebase
 *//*

class CrashlyticsTree : Timber.Tree() {

    private val crashlytics = FirebaseCrashlytics.getInstance()

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {

        try {
            if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
                return
            }

            if (BuildConfig.DEBUG) {
                crashlytics.setCrashlyticsCollectionEnabled(true)
                return
            }

            crashlytics.setCustomKey(CRASHLYTICS_KEY_PRIORITY, priority)
            if (tag != null) {
                crashlytics.setCustomKey(CRASHLYTICS_KEY_TAG, tag)
            }
            crashlytics.setCustomKey(CRASHLYTICS_KEY_MESSAGE, message)

            if (t == null) {
                crashlytics.recordException(Exception(message))
            } else {
                crashlytics.recordException(t)
            }
        } catch (exception: Exception) {
            Timber.e("ERROR SETTING UP" + exception.message)
        }
    }

    companion object {
        private const val CRASHLYTICS_KEY_PRIORITY = "priority"
        private const val CRASHLYTICS_KEY_TAG = "tag"
        private const val CRASHLYTICS_KEY_MESSAGE = "message"
    }
}
*/
