package com.pinku.chargesound

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.IBinder

class ChargeService : Service() {

    private var player: MediaPlayer? = null

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                Intent.ACTION_POWER_CONNECTED -> play(R.raw.charge_on)
                Intent.ACTION_POWER_DISCONNECTED -> play(R.raw.charge_off)
            }
        }
    }

    private fun play(resId: Int) {
        player?.release()
        player = MediaPlayer.create(this, resId)?.apply {
            setOnCompletionListener { it.release(); if (player === it) player = null }
            start()
        }
    }

    override fun onCreate() {
        super.onCreate()
        val channelId = "charge_sound"
        val nm = getSystemService(NotificationManager::class.java)
        nm.createNotificationChannel(
            NotificationChannel(channelId, "Charge Sound", NotificationManager.IMPORTANCE_MIN)
        )
        val notification = Notification.Builder(this, channelId)
            .setContentTitle("Charge Sound chalu hai")
            .setContentText("Charger lagane/nikalne pe sound bajega")
            .setSmallIcon(android.R.drawable.ic_lock_idle_charging)
            .build()
        startForeground(1, notification)

        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        }
        registerReceiver(receiver, filter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int) = START_STICKY

    override fun onDestroy() {
        unregisterReceiver(receiver)
        player?.release()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
