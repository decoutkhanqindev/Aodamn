package com.decoutkhanqindev.custom_aod.presentation.components.aod

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.model.aod.appearance.ClockFontValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodBatteryLayoutValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodComponentStyle
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyB4
import com.decoutkhanqindev.custom_aod.presentation.theme.Mint
import com.decoutkhanqindev.custom_aod.presentation.theme.Red

// Pin (mức pin theo màu khối pin, sạc thì Mint kèm tia sét, yếu thì đỏ) và đèn pin đang bật (icon Mint); font / cỡ / màu theo khối pin,
// mỗi kiểu một cách vẽ mức pin.
@Composable
fun AodStatus(
    battery: BatteryUiModel?,
    isBatteryEnabled: Boolean,
    isFlashlightOn: Boolean,
    style: AodComponentStyle,
    layout: AodBatteryLayoutValue,
) {
    val shownBattery = battery?.takeIf { isBatteryEnabled }

    AnimatedVisibility(visible = shownBattery != null || isFlashlightOn) {
        Row(
            modifier = Modifier
                .padding(top = 20.dp)
                .animateContentSize(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
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
                    AodBattery(battery = shownBattery, style = style, layout = layout)
                }
            }

            if (isFlashlightOn) {
                Icon(
                    painter = painterResource(R.drawable.ic_flashlight_light),
                    contentDescription = stringResource(R.string.aod_flashlight_on),
                    modifier = Modifier.size(15.dp * style.scale),
                    tint = Mint,
                )
            }
        }
    }
}

@Composable
private fun AodBattery(
    battery: BatteryUiModel,
    style: AodComponentStyle,
    layout: AodBatteryLayoutValue,
) {
    val level = animateFloatAsState(
        targetValue = battery.percent.coerceIn(0, 100) / 100f,
        label = "AodBatteryLevel",
    )
    val fill = when {
        battery.isCharging -> Mint
        battery.isLow -> Red
        else -> style.color
    }
    val percent = stringResource(R.string.aod_battery, battery.percent)

    when (layout) {
        AodBatteryLayoutValue.ICON -> {
            BatteryGlyph(level = level, color = style.color, fill = fill, scale = style.scale)
            BatteryText(text = percent, style = style, modifier = Modifier.padding(start = 6.dp))
            ChargingMark(battery = battery, style = style)
        }

        // Vòng tiến trình, phần trăm ở giữa
        AodBatteryLayoutValue.RING -> {
            Box(
                modifier = Modifier
                    .size(34.dp * style.scale)
                    .drawWithCache {
                        val strokeWidth = 2.5.dp.toPx()
                        val inset = strokeWidth / 2f
                        val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
                        val stroke = Stroke(width = strokeWidth, cap = StrokeCap.Round)

                        onDrawBehind {
                            drawArc(
                                color = style.color,
                                startAngle = 0f,
                                sweepAngle = 360f,
                                useCenter = false,
                                topLeft = Offset(inset, inset),
                                size = arcSize,
                                style = stroke,
                                alpha = 0.25f,
                            )
                            drawArc(
                                color = fill,
                                startAngle = -90f,
                                sweepAngle = 360f * level.value,
                                useCenter = false,
                                topLeft = Offset(inset, inset),
                                size = arcSize,
                                style = stroke,
                            )
                        }
                    },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = battery.percent.toString(),
                    color = style.color,
                    fontSize = 10.sp * style.scale,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = style.fontFamily,
                )
            }
            ChargingMark(battery = battery, style = style)
        }

        // Thanh ngang mảnh, phần trăm bên phải
        AodBatteryLayoutValue.BAR -> {
            Box(
                modifier = Modifier
                    .size(width = 64.dp * style.scale, height = 4.dp * style.scale)
                    .drawWithCache {
                        val radius = CornerRadius(size.height / 2f)

                        onDrawBehind {
                            drawRoundRect(color = style.color, cornerRadius = radius, alpha = 0.25f)
                            drawRoundRect(
                                color = fill,
                                size = Size(size.width * level.value, size.height),
                                cornerRadius = radius,
                            )
                        }
                    },
            )
            BatteryText(text = percent, style = style, modifier = Modifier.padding(start = 8.dp))
            ChargingMark(battery = battery, style = style)
        }

        // Năm chấm, mỗi chấm 20%
        AodBatteryLayoutValue.DOTS -> {
            val filled = (battery.percent + 19) / 20
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp * style.scale)) {
                repeat(5) { index ->
                    Box(
                        modifier = Modifier
                            .size(7.dp * style.scale)
                            .alpha(if (index < filled) 1f else 0.25f)
                            .background(color = if (index < filled) fill else style.color, shape = CircleShape),
                    )
                }
            }
            BatteryText(text = percent, style = style, modifier = Modifier.padding(start = 8.dp))
            ChargingMark(battery = battery, style = style)
        }

        // Chỉ chữ: "72%" hoặc "Đang sạc · 72%"
        AodBatteryLayoutValue.TEXT -> BatteryText(
            text = if (battery.isCharging) stringResource(R.string.aod_battery_charging, battery.percent) else percent,
            style = style,
        )
    }
}

@Composable
private fun BatteryText(
    text: String,
    style: AodComponentStyle,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier,
        color = style.color,
        fontSize = 13.sp * style.scale,
        fontFamily = style.fontFamily,
    )
}

@Composable
private fun ChargingMark(
    battery: BatteryUiModel,
    style: AodComponentStyle,
) {
    if (battery.isCharging) {
        Icon(
            painter = painterResource(R.drawable.ic_lightning_light),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 2.dp)
                .size(13.dp * style.scale),
            tint = Mint,
        )
    }
}

@Composable
private fun BatteryGlyph(
    level: State<Float>,
    color: Color,
    fill: Color,
    scale: Float,
) {
    Box(
        modifier = Modifier
            .size(width = 22.dp * scale, height = 11.dp * scale)
            .drawWithCache {
                val strokeWidth = 1.2.dp.toPx()
                val capWidth = 1.6.dp.toPx()
                val bodyWidth = size.width - capWidth - 1.dp.toPx()
                val padding = strokeWidth + 1.2.dp.toPx()
                val stroke = Stroke(width = strokeWidth)

                onDrawBehind {
                    drawRoundRect(
                        color = color,
                        topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f),
                        size = Size(bodyWidth - strokeWidth, size.height - strokeWidth),
                        cornerRadius = CornerRadius(3.dp.toPx()),
                        style = stroke,
                        alpha = 0.6f,
                    )
                    drawRoundRect(
                        color = color,
                        topLeft = Offset(size.width - capWidth, size.height * 0.3f),
                        size = Size(capWidth, size.height * 0.4f),
                        cornerRadius = CornerRadius(capWidth / 2f),
                        alpha = 0.6f,
                    )
                    val fillWidth = (bodyWidth - padding * 2f) * level.value
                    if (fillWidth > 0f) {
                        drawRoundRect(
                            color = fill,
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
    Column {
        AodBatteryLayoutValue.entries.forEach { layout ->
            AodStatus(
                battery = BatteryUiModel(percent = 72, isCharging = layout == AodBatteryLayoutValue.RING),
                isBatteryEnabled = true,
                isFlashlightOn = false,
                style = AodComponentStyle(font = ClockFontValue.DEFAULT, scale = 1f, color = GreyB4),
                layout = layout,
            )
        }
    }
}
