package com.decoutkhanqindev.aodamn.presentation.components.aod.theme

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.AodNotificationsUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.CalendarEventUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaProgressUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.NotificationContentUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.WeatherConditionValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.WeatherUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.settings.AodExtrasUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState
import com.decoutkhanqindev.aodamn.presentation.theme.Black
import com.github.takahirom.roborazzi.captureRoboImage
import kotlinx.collections.immutable.persistentListOf
import org.junit.Assume.assumeTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.util.Locale
import java.util.TimeZone

@RunWith(ParameterizedRobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [36], application = Application::class)
class AodThemeSnapshotTest(private val theme: AodThemeValue) {

    @get:Rule
    val composeRule = createComposeRule()

    @Before
    fun setUp() {
        assumeTrue(System.getProperty("roborazzi.test.record") == "true")
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
        Locale.setDefault(Locale.US)
        composeRule.mainClock.autoAdvance = false
    }

    @Test
    fun portraitPlaying() = capture(isPortrait = true, isPlaying = true)

    @Test
    fun portraitPaused() = capture(isPortrait = true, isPlaying = false)

    @Test
    fun landscapePlaying() = capture(isPortrait = false, isPlaying = true)

    @Test
    fun landscapePaused() = capture(isPortrait = false, isPlaying = false)

    private fun capture(isPortrait: Boolean, isPlaying: Boolean) {
        val width = if (isPortrait) 393 else 873
        val height = if (isPortrait) 873 else 393
        RuntimeEnvironment.setQualifiers("w${width}dp-h${height}dp-${if (isPortrait) "port" else "land"}-xhdpi")
        val state = sampleState(isPlaying)

        composeRule.setContent {
            Box(
                modifier = Modifier
                    .size(width.dp, height.dp)
                    .background(Black),
            ) {
                if (isPortrait) AodPortrait(state = state, onIntent = {}) else AodLandscape(state = state, onIntent = {})
            }
        }
        composeRule.mainClock.advanceTimeByFrame()

        val name = "${theme.name.lowercase()}_${if (isPortrait) "portrait" else "landscape"}_${if (isPlaying) "playing" else "paused"}"
        composeRule.onRoot().captureRoboImage("build/aod-snapshots/$name.png")
    }

    private fun sampleState(isPlaying: Boolean): AodState {
        val icon = Bitmap.createBitmap(48, 48, Bitmap.Config.ARGB_8888).apply { eraseColor(-1) }

        return AodState(
            nowMillis = NOW_MILLIS,
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = theme)),
            extras = AodExtrasUiModel(memo = "Buy milk"),
            weather = WeatherUiModel(
                temperature = 24,
                condition = WeatherConditionValue.CLOUDY,
                isDay = true,
                updatedAtMillis = NOW_MILLIS,
            ),
            events = persistentListOf(
                CalendarEventUiModel(
                    title = "Team meeting",
                    beginMillis = NOW_MILLIS + 3_600_000L,
                    endMillis = NOW_MILLIS + 7_200_000L,
                    isAllDay = false,
                ),
            ),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            notifications = AodNotificationsUiModel(
                latest = NotificationContentUiModel(
                    key = "sample",
                    icon = icon,
                    title = "Alice",
                    text = "See you at 7 at the usual place?",
                    isHidden = false,
                ),
            ),
            media = MediaUiModel(
                title = "That Cool Song",
                artist = "It's Artist",
                isPlaying = isPlaying,
                canSkipToPrevious = isPlaying,
                canSkipToNext = true,
                progress = MediaProgressUiModel(
                    durationMillis = 180_000L,
                    positionMillis = 75_000L,
                    updatedAtMillis = 0L,
                    speed = 0f,
                ),
            ),
            isPreview = true,
            isStill = true,
        )
    }

    companion object {
        private const val NOW_MILLIS = 1_793_003_400_000L

        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun themes(): List<AodThemeValue> = AodThemeValue.entries
    }
}
