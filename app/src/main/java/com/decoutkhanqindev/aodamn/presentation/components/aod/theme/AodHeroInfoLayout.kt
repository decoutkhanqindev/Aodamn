package com.decoutkhanqindev.aodamn.presentation.components.aod.theme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodPanelBlock
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodStatusLine
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodWeather
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.aodTint
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.AodNotificationsUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.NotificationContentUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodHeroInfoStyle
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodPanelBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState

@Composable
fun AodHeroInfoPortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
    style: AodHeroInfoStyle,
    hero: @Composable (Modifier) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = style.topMinHeight)
                .padding(horizontal = style.sideMargin),
            contentAlignment = Alignment.BottomStart,
        ) {
            HeroInfoDetails(state = state, style = style, dateSize = style.dateSize, isWeatherAtEnd = true)
        }

        Spacer(modifier = Modifier.weight(style.topSpaceWeight))

        hero(
            Modifier
                .weight(style.heroWeight)
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.weight(style.bottomSpaceWeight))

        HeroInfoPlayer(
            state = state,
            onIntent = onIntent,
            modifier = Modifier.padding(horizontal = style.sideMargin),
        )

        HeroInfoBattery(
            state = state,
            style = style,
            glyphHeight = style.batteryGlyph,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(style.batteryPadding),
        )
    }
}

@Composable
fun AodHeroInfoLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
    style: AodHeroInfoStyle,
    heroHalfWidth: (maxWidth: Dp, maxHeight: Dp) -> Dp,
    hero: @Composable (Modifier) -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val side = (maxWidth / 2 - heroHalfWidth(maxWidth, maxHeight) - style.landscapeGap - style.sideMargin).coerceAtLeast(0.dp)
        val hasMedia = state.media != null && AodComponentValue.MEDIA in state.appearance.theme.value.spec.components

        hero(Modifier.fillMaxSize())

        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = style.sideMargin)
                .width(side),
        ) {
            HeroInfoDetails(state = state, style = style, dateSize = style.landscapeDateSize, isWeatherAtEnd = false)
        }

        Crossfade(
            targetState = hasMedia,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = style.sideMargin)
                .width(side),
            label = "AodHeroInfoLandscape",
        ) { isMediaShown ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (isMediaShown) {
                    HeroInfoPlayer(
                        state = state,
                        onIntent = onIntent,
                        modifier = Modifier.padding(bottom = style.landscapeItemGap),
                    )
                }

                HeroInfoBattery(state = state, style = style, glyphHeight = style.landscapeBatteryGlyph)
            }
        }
    }
}

@Composable
private fun HeroInfoDetails(
    state: AodState,
    style: AodHeroInfoStyle,
    dateSize: TextUnit,
    isWeatherAtEnd: Boolean,
) {
    val isDateShown = state.extras.isDateEnabled
    val isWeatherShown = state.weather != null && AodComponentValue.WEATHER in state.appearance.theme.value.spec.components
    val hasIcons = state.notifications.icons.isNotEmpty()
    val latest = state.notifications.latest

    Column(modifier = Modifier.fillMaxWidth()) {
        AnimatedVisibility(visible = isDateShown || isWeatherShown) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AnimatedVisibility(visible = isDateShown) {
                    HeroInfoDate(state = state, style = style, fontSize = dateSize)
                }

                if (isDateShown && isWeatherShown) {
                    if (isWeatherAtEnd) {
                        Spacer(modifier = Modifier.weight(1f))
                    } else {
                        Spacer(modifier = Modifier.width(style.weatherGap))
                    }
                }

                HeroInfoWeather(state = state)
            }
        }

        AnimatedVisibility(visible = hasIcons) {
            AodStatusLine(
                battery = null,
                isFlashlightOn = false,
                notifications = state.notifications.copy(latest = null),
                color = state.appearance.theme.config[style.notificationsColor],
                glyphHeight = style.iconRow,
                fontFamily = style.font,
                modifier = Modifier.padding(top = if (isDateShown || isWeatherShown) style.detailsGap else 0.dp),
                gapScale = style.iconGapScale,
                brush = state.appearance.theme.config.gradientOf(style.notificationsColor)?.toBrush(),
            )
        }

        AnimatedVisibility(
            visible = latest != null,
            enter = slideInVertically { height -> -height } + fadeIn(),
            exit = fadeOut(),
        ) {
            if (latest != null) {
                val gap = when {
                    hasIcons -> style.latestGap
                    isDateShown || isWeatherShown -> style.detailsGap
                    else -> 0.dp
                }

                HeroInfoLatest(latest = latest, state = state, style = style, modifier = Modifier.padding(top = gap))
            }
        }
    }
}

@Composable
private fun HeroInfoDate(
    state: AodState,
    style: AodHeroInfoStyle,
    fontSize: TextUnit,
) {
    val time = rememberZonedDateTime(state.nowMillis)

    Text(
        text = time.format(rememberFormatter(R.string.aod_date_pattern)),
        style = TextStyle(
            brush = state.appearance.theme.config.brushOf(style.dateColor),
            fontSize = fontSize,
            fontFamily = style.font,
        ),
        maxLines = 1,
    )
}

@Composable
private fun HeroInfoWeather(state: AodState) {
    val components = state.appearance.components

    AnimatedVisibility(visible = state.weather != null && AodComponentValue.WEATHER in state.appearance.theme.value.spec.components) {
        AodWeather(
            weather = state.weather,
            style = components.styleOf(AodComponentValue.WEATHER),
            layout = components.weatherLayout,
        )
    }
}

@Composable
private fun HeroInfoLatest(
    latest: NotificationContentUiModel,
    state: AodState,
    style: AodHeroInfoStyle,
    modifier: Modifier = Modifier,
) {
    val config = state.appearance.theme.config
    val brush = config.brushOf(style.notificationsColor)
    val dimBrush = config.darkBrushOf(style.notificationsColor, style.textDarken)
    val text = if (latest.isHidden) stringResource(R.string.aod_notification_content_hidden) else latest.text

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            bitmap = remember(latest.icon) { latest.icon.asImageBitmap() },
            contentDescription = null,
            modifier = Modifier
                .size(style.latestIcon)
                .aodTint(config.gradientOf(style.notificationsColor)?.toBrush()),
            colorFilter = ColorFilter.tint(config[style.notificationsColor]),
        )

        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(brush = brush)) { append(latest.title) }
                if (text.isNotEmpty()) {
                    append("  ")
                    withStyle(SpanStyle(brush = dimBrush)) { append(text) }
                }
            },
            modifier = Modifier
                .padding(start = style.latestIconGap)
                .basicMarquee(iterations = 1),
            style = TextStyle(fontSize = style.latestSize, fontFamily = style.font),
            maxLines = 1,
        )
    }
}

@Composable
private fun HeroInfoBattery(
    state: AodState,
    style: AodHeroInfoStyle,
    glyphHeight: Dp,
    modifier: Modifier = Modifier,
) {
    val config = state.appearance.theme.config
    val battery = state.battery?.takeIf { state.extras.isBatteryEnabled }
    val alpha by animateFloatAsState(
        targetValue = if (battery?.isCharging == true) 1f else style.idleBatteryAlpha,
        label = "AodHeroInfoBatteryAlpha",
    )

    Box(
        modifier = modifier.heightIn(min = glyphHeight),
        contentAlignment = Alignment.Center,
    ) {
        AodStatusLine(
            battery = battery,
            isFlashlightOn = state.isFlashlightOn,
            notifications = AodNotificationsUiModel(),
            color = config[style.batteryColor],
            glyphHeight = glyphHeight,
            fontFamily = style.font,
            modifier = Modifier.graphicsLayer { this.alpha = alpha },
            brush = config.gradientOf(style.batteryColor)?.toBrush(),
        )
    }
}

@Composable
private fun HeroInfoPlayer(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = state.media != null && AodComponentValue.MEDIA in state.appearance.theme.value.spec.components,
        modifier = modifier,
    ) {
        AodPanelBlock(block = AodPanelBlockValue.MEDIA, state = state, onIntent = onIntent)
    }
}
