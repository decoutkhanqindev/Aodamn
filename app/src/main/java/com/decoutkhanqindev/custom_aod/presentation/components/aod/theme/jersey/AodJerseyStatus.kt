package com.decoutkhanqindev.custom_aod.presentation.components.aod.theme.jersey

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
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
import com.decoutkhanqindev.custom_aod.presentation.model.aod.info.AodNotificationsUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.jersey.JerseyThemeSpec
import com.decoutkhanqindev.custom_aod.presentation.theme.Black
import com.decoutkhanqindev.custom_aod.presentation.theme.Mint
import com.decoutkhanqindev.custom_aod.presentation.theme.Red

// Dòng trạng thái của Số áo dưới đồng hồ: pin (icon pin dọc, đang sạc thì có tia sét) · icon các app đang có thông báo, chung một dòng, màu
// theo màu phút. Đèn pin đang bật thì icon Mint cạnh pin như dòng pin dùng chung. Không có gì để hiện thì không chiếm chỗ.
@Composable
fun AodJerseyStatus(
    battery: BatteryUiModel?,
    isFlashlightOn: Boolean,
    notifications: AodNotificationsUiModel,
    config: AodThemeConfig,
) {
    val color = config[JerseyThemeSpec.MinuteColor]
    val hasPower = battery != null || isFlashlightOn
    val hasIcons = notifications.icons.isNotEmpty()

    AnimatedVisibility(visible = hasPower || hasIcons) {
        Row(
            modifier = Modifier
                .padding(top = 40.dp)
                .animateContentSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (battery != null) {
                val description = stringResource(
                    if (battery.isCharging) R.string.aod_battery_charging else R.string.aod_battery,
                    battery.percent,
                )

                Row(
                    modifier = Modifier.clearAndSetSemantics { contentDescription = description },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    JerseyBatteryGlyph(battery = battery, color = color)

                    Text(
                        text = stringResource(R.string.aod_battery, battery.percent),
                        modifier = Modifier.padding(start = 6.dp),
                        color = color,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = JerseyThemeSpec.Font,
                    )
                }
            }

            if (isFlashlightOn) {
                Icon(
                    painter = painterResource(R.drawable.ic_flashlight_light),
                    contentDescription = stringResource(R.string.aod_flashlight_on),
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(15.dp),
                    tint = Mint,
                )
            }

            if (hasPower && hasIcons) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 14.dp)
                        .size(4.dp)
                        .background(color = color, shape = CircleShape),
                )
            }

            if (hasIcons) {
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    notifications.icons.forEach { icon ->
                        key(icon.packageName) {
                            Image(
                                bitmap = remember(icon.icon) { icon.icon.asImageBitmap() },
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                colorFilter = ColorFilter.tint(color),
                            )
                        }
                    }
                }

                if (notifications.overflowCount > 0) {
                    Text(
                        text = stringResource(R.string.aod_notifications_overflow, notifications.overflowCount),
                        modifier = Modifier.padding(start = 10.dp),
                        color = color,
                        fontSize = 13.sp,
                        fontFamily = JerseyThemeSpec.Font,
                    )
                }
            }
        }
    }
}

// Pin dọc: viền và mức pin tô từ dưới lên (pin yếu tô đỏ); đang sạc thì tô đầy với tia sét khoét đen như app mẫu.
@Composable
private fun JerseyBatteryGlyph(
    battery: BatteryUiModel,
    color: Color,
) {
    val level = battery.percent.coerceIn(0, 100) / 100f
    val fill = if (battery.isLow && !battery.isCharging) Red else color

    Box(
        modifier = Modifier
            .size(width = 10.dp, height = 17.dp)
            .drawWithCache {
                val strokeWidth = 1.4.dp.toPx()
                val capHeight = 2.dp.toPx()
                val capWidth = size.width * 0.44f
                val bodyTop = capHeight
                val bodyHeight = size.height - bodyTop
                val corner = CornerRadius(2.5.dp.toPx())
                val padding = strokeWidth + 1.2.dp.toPx()
                val fillHeight = (bodyHeight - padding * 2f) * level
                val bolt = Path().apply {
                    BoltPoints.forEachIndexed { index, (x, y) ->
                        val pointX = size.width * x
                        val pointY = bodyTop + bodyHeight * y
                        if (index == 0) moveTo(pointX, pointY) else lineTo(pointX, pointY)
                    }
                    close()
                }

                onDrawBehind {
                    drawRoundRect(
                        color = color,
                        topLeft = Offset((size.width - capWidth) / 2f, 0f),
                        size = Size(capWidth, capHeight + strokeWidth / 2f),
                        cornerRadius = CornerRadius(1.dp.toPx()),
                    )
                    if (battery.isCharging) {
                        drawRoundRect(
                            color = color,
                            topLeft = Offset(0f, bodyTop),
                            size = Size(size.width, bodyHeight),
                            cornerRadius = corner,
                        )
                        drawPath(path = bolt, color = Black)
                    } else {
                        drawRoundRect(
                            color = color,
                            topLeft = Offset(strokeWidth / 2f, bodyTop + strokeWidth / 2f),
                            size = Size(size.width - strokeWidth, bodyHeight - strokeWidth),
                            cornerRadius = corner,
                            style = Stroke(width = strokeWidth),
                        )
                        if (fillHeight > 0f) {
                            drawRoundRect(
                                color = fill,
                                topLeft = Offset(padding, size.height - padding - fillHeight),
                                size = Size(size.width - padding * 2f, fillHeight),
                                cornerRadius = CornerRadius(1.dp.toPx()),
                            )
                        }
                    }
                }
            },
    )
}

// Tia sét trong thân pin, toạ độ theo tỉ lệ bề ngang / chiều cao thân pin.
private val BoltPoints = listOf(
    0.62f to 0.12f,
    0.22f to 0.56f,
    0.48f to 0.56f,
    0.38f to 0.90f,
    0.80f to 0.42f,
    0.54f to 0.42f,
)

@Preview
@Composable
private fun AodJerseyStatusPreview() {
    AodJerseyStatus(
        battery = BatteryUiModel(percent = 32, isCharging = true),
        isFlashlightOn = false,
        notifications = AodNotificationsUiModel(),
        config = AodThemeConfig(),
    )
}
