package com.decoutkhanqindev.custom_aod.presentation.components.aod

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.custom_aod.presentation.theme.AccentGradient
import com.decoutkhanqindev.custom_aod.presentation.theme.FontFamilyPlusJakartaSans
import com.decoutkhanqindev.custom_aod.presentation.theme.Grey6E
import com.decoutkhanqindev.custom_aod.presentation.theme.Grey8A
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyB4
import com.decoutkhanqindev.custom_aod.presentation.theme.Mint
import com.decoutkhanqindev.custom_aod.presentation.theme.Red

// Viên trạng thái (AodPill): pin tự vẽ (mức pin xám, sạc thì gradient của app, yếu thì đỏ) + phần trăm, đèn pin đang bật thì thêm icon.
@Composable
fun AodStatus(
    battery: BatteryUiModel?,
    isBatteryEnabled: Boolean,
    isFlashlightOn: Boolean,
) {
    val shownBattery = battery?.takeIf { isBatteryEnabled }

    AnimatedVisibility(visible = shownBattery != null || isFlashlightOn) {
        AodPill(
            modifier = Modifier
                .padding(top = 20.dp)
                .animateContentSize(),
        ) {
            if (shownBattery != null) {
                val description = stringResource(
                    if (shownBattery.isCharging) R.string.aod_battery_charging else R.string.aod_battery,
                    shownBattery.percent,
                )

                Row(
                    modifier = Modifier.clearAndSetSemantics { contentDescription = description },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    BatteryGlyph(battery = shownBattery)

                    Text(
                        text = stringResource(R.string.aod_battery, shownBattery.percent),
                        modifier = Modifier.padding(start = 6.dp),
                        color = Grey8A,
                        fontSize = 13.sp,
                        fontFamily = FontFamilyPlusJakartaSans,
                    )

                    if (shownBattery.isCharging) {
                        Icon(
                            painter = painterResource(R.drawable.ic_lightning_light),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(start = 2.dp)
                                .size(13.dp),
                            tint = Mint,
                        )
                    }
                }
            }

            if (isFlashlightOn) {
                Icon(
                    painter = painterResource(R.drawable.ic_flashlight_light),
                    contentDescription = stringResource(R.string.aod_flashlight_on),
                    modifier = Modifier.size(15.dp),
                    tint = Mint,
                )
            }
        }
    }
}

@Composable
private fun BatteryGlyph(battery: BatteryUiModel) {
    val level = animateFloatAsState(
        targetValue = battery.percent.coerceIn(0, 100) / 100f,
        label = "AodBatteryLevel",
    )
    val fill = when {
        battery.isCharging -> AccentGradient
        battery.isLow -> SolidColor(Red)
        else -> SolidColor(GreyB4)
    }

    Box(
        modifier = Modifier
            .size(width = 22.dp, height = 11.dp)
            .drawWithCache {
                val strokeWidth = 1.2.dp.toPx()
                val capWidth = 1.6.dp.toPx()
                val bodyWidth = size.width - capWidth - 1.dp.toPx()
                val padding = strokeWidth + 1.2.dp.toPx()
                val stroke = Stroke(width = strokeWidth)

                onDrawBehind {
                    drawRoundRect(
                        color = Grey6E,
                        topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f),
                        size = Size(bodyWidth - strokeWidth, size.height - strokeWidth),
                        cornerRadius = CornerRadius(3.dp.toPx()),
                        style = stroke,
                    )
                    drawRoundRect(
                        color = Grey6E,
                        topLeft = Offset(size.width - capWidth, size.height * 0.3f),
                        size = Size(capWidth, size.height * 0.4f),
                        cornerRadius = CornerRadius(capWidth / 2f),
                    )
                    val fillWidth = (bodyWidth - padding * 2f) * level.value
                    if (fillWidth > 0f) {
                        drawRoundRect(
                            brush = fill,
                            topLeft = Offset(padding, padding),
                            size = Size(fillWidth, size.height - padding * 2f),
                            cornerRadius = CornerRadius(1.5.dp.toPx()),
                        )
                    }
                }
            },
    )
}

@Preview
@Composable
private fun AodStatusPreview() {
    AodStatus(
        battery = BatteryUiModel(percent = 72, isCharging = true),
        isBatteryEnabled = true,
        isFlashlightOn = true,
    )
}
