package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.pebble

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodHeroDetails
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodLandscapeLayout
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodPanelBlock
import com.decoutkhanqindev.aodamn.presentation.components.aod.landscapePlan
import com.decoutkhanqindev.aodamn.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodPanelBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.pebble.PebbleThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState

// Màn ngang của Sỏi: bố cục chung theo các khối đang hiện (AodLandscapeLayout) với nét riêng. Chỉ có nhạc thì "kẹp giữa": khối giờ bên trái,
// khối phút bên phải, giữa là ngày, trình phát của Sỏi và pin. Có khối khác thì hai cột (cụm khối | các khối, nhạc xếp dưới), không ba cột
// vì hai khối hình đã rộng. Không có khối nào thì cụm khối to giữa màn hình. Khối hình theo chiều cao màn hình, nhân tuỳ chọn Cỡ.
@Composable
fun AodPebbleLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    val config = state.appearance.theme.config
    val scale = config[PebbleThemeSpec.Size] / 100f

    BoxWithConstraints(contentAlignment = Alignment.Center) {
        val sandwichTile = minOf(maxHeight * 0.42f * scale, (maxWidth - 360.dp) / 2, maxHeight * 0.8f)
        val heroTile = maxHeight * 0.38f
        val panelHeroTile = maxHeight * 0.3f

        Crossfade(targetState = state.landscapePlan(), label = "AodPebbleLandscape") { plan ->
            if (plan.isMediaOnly) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    PebbleSandwich(
                        state = state,
                        onIntent = onIntent,
                        tileSize = sandwichTile,
                        isWeatherShown = plan.isWeatherBesideDate,
                    )
                }
            } else {
                AodLandscapeLayout(
                    plan = plan,
                    state = state,
                    onIntent = onIntent,
                    isThreeColumnAllowed = false,
                ) {
                    PebbleHero(
                        state = state,
                        tileSize = if (plan.isEmpty) heroTile else panelHeroTile,
                        isWeatherShown = plan.isWeatherBesideDate,
                    )
                }
            }
        }
    }
}

// Cụm khối giờ / phút cạnh nhau, ngày, rồi thời tiết (nếu đứng cạnh ngày), icon thông báo và pin.
@Composable
private fun PebbleHero(
    state: AodState,
    tileSize: Dp,
    isWeatherShown: Boolean,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AodPebbleClock(
            nowMillis = state.nowMillis,
            config = state.appearance.theme.config,
            isDateEnabled = state.extras.isDateEnabled,
            tileSize = tileSize,
            isStackable = false,
        )

        AodHeroDetails(state = state, isWeatherShown = isWeatherShown)
    }
}

@Composable
private fun PebbleSandwich(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
    tileSize: Dp,
    isWeatherShown: Boolean,
) {
    val config = state.appearance.theme.config
    val timeDescription = rememberZonedDateTime(state.nowMillis).format(rememberTimeFormatter())

    Row(
        horizontalArrangement = Arrangement.spacedBy(32.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PebbleTimeTile(
            nowMillis = state.nowMillis,
            config = config,
            isHour = true,
            size = tileSize,
            modifier = Modifier.clearAndSetSemantics { contentDescription = timeDescription },
        )

        Column(
            modifier = Modifier.width(296.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AnimatedVisibility(visible = state.extras.isDateEnabled) {
                PebbleDate(nowMillis = state.nowMillis, config = config)
            }

            if (isWeatherShown) {
                AodPanelBlock(block = AodPanelBlockValue.WEATHER, state = state, onIntent = onIntent)
            }

            AodPanelBlock(block = AodPanelBlockValue.MEDIA, state = state, onIntent = onIntent)

            AodHeroDetails(state = state, isWeatherShown = false)
        }

        PebbleTimeTile(
            nowMillis = state.nowMillis,
            config = config,
            isHour = false,
            size = tileSize,
            modifier = Modifier.clearAndSetSemantics { },
        )
    }
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodPebbleLandscapePreview() {
    AodPebbleLandscape(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            battery = BatteryUiModel(percent = 72, isCharging = false),
        ),
        onIntent = {},
    )
}
