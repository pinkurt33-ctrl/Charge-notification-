package com.pinku.chargesound

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {

    private var testPlayer: MediaPlayer? = null
    private lateinit var status: TextView
    private lateinit var toggle: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= 33 &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
        }

        val prefs = getSharedPreferences("cs", MODE_PRIVATE)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(60, 60, 60, 60)
            setBackgroundColor(Color.parseColor("#101820"))
        }

        val title = TextView(this).apply {
            text = "⚡ Charge Sound"
            textSize = 30f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
        }
        status = TextView(this).apply {
            textSize = 16f
            setTextColor(Color.parseColor("#FFD54F"))
            gravity = Gravity.CENTER
            setPadding(0, 30, 0, 40)
        }
        toggle = Button(this)
        val testOn = Button(this).apply { text = "▶ Test: Charger lagane wala sound" }
        val testOff = Button(this).apply { text = "▶ Test: Charger nikalne wala sound" }

        fun refresh() {
            val on = prefs.getBoolean("enabled", false)
            toggle.text = if (on) "⏹ Band karo" else "✅ Chalu karo"
            status.text = if (on) "App ON hai — charger lagao/nikalo!" else "App OFF hai"
        }

        toggle.setOnClickListener {
            val on = !prefs.getBoolean("enabled", false)
            prefs.edit().putBoolean("enabled", on).apply()
            val svc = Intent(this, ChargeService::class.java)
            if (on) startForegroundService(svc) else stopService(svc)
            refresh()
        }
        testOn.setOnClickListener { play(R.raw.charge_on) }
        testOff.setOnClickListener { play(R.raw.charge_off) }

        listOf(title, status, toggle, testOn, testOff).forEach { root.addView(it) }
        setContentView(root)
        refresh()
    }

    private fun play(res: Int) {
        testPlayer?.release()
        testPlayer = MediaPlayer.create(this, res)?.apply { start() }
    }

    override fun onDestroy() {
        testPlayer?.release()
        super.onDestroy()
    }
}
