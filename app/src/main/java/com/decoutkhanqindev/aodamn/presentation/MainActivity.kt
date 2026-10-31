package com.decoutkhanqindev.aodamn.presentation

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ComposeUiFlags
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.decoutkhanqindev.aodamn.ads.AdsManager
import com.decoutkhanqindev.aodamn.data.local.datastore.DataStoreManager
import com.decoutkhanqindev.aodamn.data.network.connectivity.NetworkManager
import com.decoutkhanqindev.aodamn.presentation.aod.AodService
import com.decoutkhanqindev.aodamn.presentation.components.AppLanguageProvider
import com.decoutkhanqindev.aodamn.presentation.navigation.AppNavDisplay
import com.decoutkhanqindev.aodamn.presentation.theme.Theme
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val dataStoreManager: DataStoreManager by inject()
    private val adsManager: AdsManager by inject()
    private val networkManager: NetworkManager by inject()

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        ComposeUiFlags.isBypassUnfocusableComposeViewEnabled = false
        super.onCreate(savedInstanceState)
        requestConsent()
        runCatching { requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT }
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
        )
        startAodServiceIfEnabled()
        setContent {
            AppLanguageProvider {
                CompositionLocalProvider(LocalOverscrollFactory provides null) {
                    Theme { AppNavDisplay() }
                }
            }
        }
    }

    private fun requestConsent() {
        lifecycleScope.launch {
            networkManager.isAvailable.first { it }
            adsManager.requestConsent(this@MainActivity)
        }
    }

    private fun startAodServiceIfEnabled() {
        lifecycleScope.launch {
            if (dataStoreManager.isAodEnabled.filterNotNull().first()) {
                AodService.start(this@MainActivity)
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemBar()
    }

    private fun hideSystemBar() {
        runCatching {
            WindowCompat.getInsetsController(window, window.decorView).apply {
                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                hide(WindowInsetsCompat.Type.navigationBars())
            }
        }
    }
}
