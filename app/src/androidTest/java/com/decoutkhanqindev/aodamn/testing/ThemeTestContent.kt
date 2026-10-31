package com.decoutkhanqindev.aodamn.testing

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.platform.testTag
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodContent
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodDate
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodDrawing
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodEvents
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodMediaControls
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodMemo
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodNotificationContent
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodNotificationIcons
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodStatus
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodWeather
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.options
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentsUiModel
import com.decoutkhanqindev.aodamn.presentation.theme.Black
import java.util.Locale

@Composable
fun ThemeTestLocaleProvider(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val configuration = remember {
        Configuration(context.resources.configuration).apply {
            setLocale(Locale.forLanguageTag("vi"))
        }
    }
    val resources = remember(configuration) {
        context.createConfigurationContext(configuration).resources
    }

    CompositionLocalProvider(
        LocalConfiguration provides configuration,
        LocalResources provides resources,
        content = content,
    )
}

@Composable
fun SharedBlockTestContent(case: SharedBlockCase) {
    val state = remember(case) {
        ThemeTestFixtures.state(
            theme = AodThemeValue.CLASSIC,
            profile = ThemeRenderProfile.MAXIMUM,
            minute = 30,
        )
    }
    val options = AodComponentSpec.optionsOf(case.component)
    val componentConfig = remember(case) {
        case.layout?.let { layout ->
            options.layout?.let { option -> AodThemeConfig().withRaw(option.key, layout.code) }
        } ?: AodThemeConfig()
    }
    val components = remember(componentConfig) { AodComponentsUiModel(componentConfig) }
    val style = components.styleOf(case.component)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .testTag(ThemeTestCatalog.SHARED_ROOT_TAG),
        contentAlignment = Alignment.Center,
    ) {
        when (case.component) {
            AodComponentValue.DATE -> AodDate(
                nowMillis = state.nowMillis,
                style = style,
                layout = components.dateLayout,
            )

            AodComponentValue.BATTERY -> AodStatus(
                battery = state.battery,
                isBatteryEnabled = true,
                isFlashlightOn = false,
                style = style,
                layout = components.batteryLayout,
            )

            AodComponentValue.WEATHER -> AodWeather(
                weather = state.weather,
                style = style,
                layout = components.weatherLayout,
            )

            AodComponentValue.EVENTS -> AodEvents(
                events = state.events,
                style = style,
                layout = components.eventsLayout,
            )

            AodComponentValue.MEMO -> AodMemo(
                memo = state.extras.memo,
                style = style,
                layout = components.memoLayout,
            )

            AodComponentValue.NOTIFICATIONS -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AodNotificationIcons(
                    notifications = state.notifications,
                    style = style,
                    layout = components.notificationsLayout,
                )
                AodNotificationContent(
                    content = state.notifications.latest,
                    style = style,
                    layout = components.notificationsLayout,
                )
            }

            AodComponentValue.MEDIA -> AodMediaControls(
                media = state.media,
                theme = state.appearance.theme,
                style = style,
                layout = components.mediaLayout,
                onIntent = {},
            )

            AodComponentValue.DRAWING -> AodDrawing(
                drawing = state.drawing,
                style = style,
            )
        }
    }
}

@Composable
fun ThemeOptionBoardContent(theme: AodThemeValue) {
    val states = remember(theme) {
        val configs = theme.spec.options
            .flatMap { option -> ThemeOptionCases.all(option) }
            .map { raws -> AodThemeConfig().withRaws(raws) }
            .ifEmpty { listOf(AodThemeConfig()) }
        configs.map { config ->
            val base = ThemeTestFixtures.state(theme, ThemeRenderProfile.REFERENCE, minute = 30)
            base.copy(
                appearance = base.appearance.copy(theme = AodThemeUiModel(value = theme, config = config)),
                isPreview = true,
                isStill = true,
            )
        }
    }
    val columns = when {
        states.size <= 4 -> 2
        states.size <= 9 -> 3
        else -> 4
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .testTag(ThemeTestCatalog.OPTION_ROOT_TAG),
    ) {
        states.chunked(columns).forEach { rowStates ->
            Row(modifier = Modifier.weight(1f)) {
                rowStates.forEach { state ->
                    Box(modifier = Modifier.weight(1f)) {
                        AodContent(
                            state = state,
                            onIntent = {},
                            modifier = Modifier.fillMaxSize(),
                            isInteractive = false,
                        )
                    }
                }
                repeat(columns - rowStates.size) {
                    Box(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}