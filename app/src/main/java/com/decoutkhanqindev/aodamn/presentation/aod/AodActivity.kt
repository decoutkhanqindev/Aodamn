package com.decoutkhanqindev.aodamn.presentation.aod

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.ComposeUiFlags
import androidx.compose.ui.ExperimentalComposeUiApi
import com.decoutkhanqindev.aodamn.data.local.datastore.DataStoreManager
import com.decoutkhanqindev.aodamn.presentation.components.AppLanguageProvider
import com.decoutkhanqindev.aodamn.presentation.components.AppTagProvider
import com.decoutkhanqindev.aodamn.presentation.screens.aod.AodScreen
import org.koin.android.ext.android.inject

class AodActivity : ComponentActivity() {

    private val dataStoreManager: DataStoreManager by inject()
    private val session: AodSession by inject()

    private var normalBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
    private var isDark = false
    private var isDimmed = false

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        ComposeUiFlags.isBypassUnfocusableComposeViewEnabled = false
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        window.addFlags(
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
        )

        val isCustomBrightness =
            dataStoreManager.isAodCustomBrightness.value
                ?: DataStoreManager.DEFAULT_IS_AOD_CUSTOM_BRIGHTNESS
        val brightnessPercent =
            dataStoreManager.aodBrightnessPercent.value
                ?: DataStoreManager.DEFAULT_AOD_BRIGHTNESS_PERCENT
        normalBrightness = if (isCustomBrightness) {
            brightnessPercent / MAX_BRIGHTNESS_PERCENT
        } else {
            WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
        }
        session.attach(this)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setBrightness(normalBrightness)
        if (dataStoreManager.isAodLandscape.value == true) lockLandscape()
        setContent {
            AppLanguageProvider {
                AppTagProvider(tag = "AodScreen") {
                    AodScreen(
                        onDarkChange = ::renderDark,
                        onDimChange = ::renderDim,
                        onClose = ::close,
                    )
                }
            }
        }
        hideSystemBars()
    }

    override fun onDestroy() {
        session.detach(this)
        super.onDestroy()
    }

    fun close() {
        if (isFinishing) return
        finish()
        @Suppress("DEPRECATION")
        overridePendingTransition(0, 0)
    }

    private fun renderDark(isDark: Boolean) {
        if (isDark == this.isDark) return
        this.isDark = isDark
        if (isDark) {
            session.requestSleep()
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            setBrightness(WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_OFF)
        } else {
            session.clearSleepRequest()
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            setBrightness(lightBrightness())
        }
    }

    private fun renderDim(isDimmed: Boolean) {
        if (isDimmed == this.isDimmed) return
        this.isDimmed = isDimmed
        if (!isDark) setBrightness(lightBrightness())
    }

    private fun lightBrightness(): Float = if (isDimmed) DIM_BRIGHTNESS else normalBrightness

    private fun setBrightness(value: Float) {
        window.attributes = window.attributes.apply { screenBrightness = value }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun lockLandscape() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
    }

    @Suppress("DEPRECATION")
    private fun hideSystemBars() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            window.setDecorFitsSystemWindows(false)
        }
        window.insetsController?.apply {
            hide(WindowInsets.Type.systemBars())
            systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    companion object {
        private const val MAX_BRIGHTNESS_PERCENT = 100f
        private const val DIM_BRIGHTNESS = 0.01f
    }
}
