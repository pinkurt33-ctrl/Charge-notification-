package com.pinku.chargesound

import android.app.Application
import java.io.PrintWriter
import java.io.StringWriter

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        val old = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            try {
                val sw = StringWriter()
                e.printStackTrace(PrintWriter(sw))
                getSharedPreferences("cs", MODE_PRIVATE).edit()
                    .putString("crash", sw.toString()).commit()
            } catch (_: Throwable) { }
            old?.uncaughtException(t, e)
        }
    }
}
