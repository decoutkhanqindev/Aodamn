package com.decoutkhanqindev.aodamn.testing

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.platform.testTag
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodContent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState
import java.util.Locale

class ThemeTestActivity : ComponentActivity() {
    private var state by mutableStateOf(initialState)

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = if (initialLandscape) {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        super.onCreate(savedInstanceState)
        setContent {
            val configuration = remember {
                Configuration(resources.configuration).apply {
                    setLocale(Locale.forLanguageTag("vi"))
                }
            }
            val localizedResources = remember(configuration) {
                createConfigurationContext(configuration).resources
            }

            CompositionLocalProvider(
                LocalConfiguration provides configuration,
                LocalResources provides localizedResources,
            ) {
                AodContent(
                    state = state,
                    onIntent = {},
                    modifier = Modifier.testTag(ROOT_TAG),
                    isInteractive = false,
                )
            }
        }
    }

    fun render(value: AodState) {
        state = value
    }

    companion object {
        const val ROOT_TAG = "theme_test_root"
        var initialState = AodState()
        var initialLandscape = false
    }
}