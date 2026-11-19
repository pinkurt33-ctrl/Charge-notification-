package com.pinku.chargesound

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val prefs = context.getSharedPreferences("cs", Context.MODE_PRIVATE)
            if (prefs.getBoolean("enabled", false)) {
                try { context.startForegroundService(Intent(context, ChargeService::class.java)) }
                catch (_: Exception) { }
            }
        }
    }
}
