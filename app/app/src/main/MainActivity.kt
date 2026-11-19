package com.pinku.chargesound

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import java.io.PrintWriter
import java.io.StringWriter

class MainActivity : Activity() {

    private var testPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences("cs", MODE_PRIVATE)
        try {
            val old = prefs.getString("crash", null)
            if (old != null) {
                showError(old, prefs)
                return
            }
            buildUi(prefs)
        } catch (e: Throwable) {
            showError(trace(e), prefs)
        }
    }

    private fun buildUi(prefs: SharedPreferences) {
        if (Build.VERSION.SDK_INT >= 33 &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
        }

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
        val status = TextView(this).apply {
            textSize = 16f
            setTextColor(Color.parseColor("#FFD54F"))
            gravity = Gravity.CENTER
            setPadding(0, 30, 0, 40)
        }
        val toggle = Button(this)
        val testOn = Button(this).apply { text = "▶ Test: Charger lagane wala sound" }
        val testOff = Button(this).apply { text = "▶ Test: Charger nikalne wala sound" }

        fun refresh() {
            val on = prefs.getBoolean("enabled", false)
            toggle.text = if (on) "⏹ Band karo" else "✅ Chalu karo"
            status.text = if (on) "App ON hai — charger lagao/nikalo!" else "App OFF hai"
        }

        toggle.setOnClickListener {
            try {
                val on = !prefs.getBoolean("enabled", false)
                prefs.edit().putBoolean("enabled", on).apply()
                val svc = Intent(this, ChargeService::class.java)
                if (on) startForegroundService(svc) else stopService(svc)
                refresh()
            } catch (e: Throwable) {
                showError(trace(e), prefs)
            }
        }
        testOn.setOnClickListener { play(R.raw.charge_on, prefs) }
        testOff.setOnClickListener { play(R.raw.charge_off, prefs) }

        listOf(title, status, toggle, testOn, testOff).forEach { root.addView(it) }
        setContentView(root)
        refresh()
    }

    private fun play(res: Int, prefs: SharedPreferences) {
        try {
            testPlayer?.release()
            testPlayer = MediaPlayer.create(this, res)?.apply { start() }
        } catch (e: Throwable) {
            showError(trace(e), prefs)
        }
    }

    private fun trace(e: Throwable): String {
        val sw = StringWriter()
        e.printStackTrace(PrintWriter(sw))
        return sw.toString()
    }

    private fun showError(text: String, prefs: SharedPreferences) {
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(30, 60, 30, 30)
            setBackgroundColor(Color.parseColor("#101820"))
        }
        val clear = Button(this).apply {
            this.text = "Clear karke app kholo"
            setOnClickListener {
                prefs.edit().remove("crash").apply()
                recreate()
            }
        }
        val info = TextView(this).apply {
            this.text = "⚠️ Error aaya. Is text ka screenshot bhejo:\n\n$text"
            setTextColor(Color.WHITE)
            textSize = 12f
            setTextIsSelectable(true)
        }
        box.addView(clear)
        box.addView(
            ScrollView(this).apply { addView(info) },
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f)
        )
        setContentView(box)
    }

    override fun onDestroy() {
        testPlayer?.release()
        super.onDestroy()
    }
}
