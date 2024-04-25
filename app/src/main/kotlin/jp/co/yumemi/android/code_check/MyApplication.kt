package jp.co.yumemi.android.code_check

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Custom [Application] class for MyApplication.
 *
 * This class is annotated with [@HiltAndroidApp]
 * to enable Hilt for dependency injection throughout the application.
 */
@HiltAndroidApp
class MyApplication : Application() {
    /**
     * Called when the application is starting, before any other application objects have been created.
     */
    override fun onCreate() {
        super.onCreate()
    }
}