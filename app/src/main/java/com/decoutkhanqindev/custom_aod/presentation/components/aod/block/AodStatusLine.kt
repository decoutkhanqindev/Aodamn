package com.decoutkhanqindev.custom_aod.presentation.components.aod.block

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.model.aod.info.AodNotificationsUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.custom_aod.presentation.theme.Black
import com.decoutkhanqindev.custom_aod.presentation.theme.FontFamilyPlusJakartaSans
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyED
import com.decoutkhanqindev.custom_aod.presentation.theme.Mint
import com.decoutkhanqindev.custom_aod.presentation.theme.Red

// Dòng trạng thái của chủ đề tự vẽ pin + thông báo (AodBlockValue.STATUS: Số áo, Viên nang): pin (icon pin dọc, đang sạc thì có tia sét) ·
// icon các app đang có thông báo, chung một dòng. Đèn pin đang bật thì icon Mint cạnh pin. Mọi kích thước theo glyphHeight (chiều cao icon pin,
// tỉ lệ đo trên ảnh mẫu Số áo); gapScale thu khoảng cách cho chủ đề có dòng chặt hơn (Viên nang). Không có gì để hiện thì không chiếm chỗ.
@Composable
fun AodStatusLine(
    battery: BatteryUiModel?,
    isFlashlightOn: Boolean,
    notifications: AodNotificationsUiModel,
    color: Color,
    glyphHeight: Dp,
    fontFamily: FontFamily,
    modifier: Modifier = Modifier,
    gapScale: Float = 1f,
) {
    val hasPower = battery != null || isFlashlightOn
    val hasIcons = notifications.icons.isNotEmpty()
    val fontSize = with(LocalDensity.current) { (glyphHeight * 0.915f).toSp() }

    AnimatedVisibility(
        visible = hasPower || hasIcons,
        modifier = modifier,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Row(
            modifier = Modifier.heightIn(min = glyphHeight),
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
                    AodBatteryGlyph(
                        battery = battery,
                        color = color,
                        modifier = Modifier.size(
                            width = glyphHeight * 0.522f,
                            height = glyphHeight,
                        ),
                    )

                    StatusText(
                        text = stringResource(R.string.aod_battery, battery.percent),
                        color = color,
                        fontSize = fontSize,
                        fontWeight = FontWeight.Medium,
                        fontFamily = fontFamily,
                        modifier = Modifier.padding(start = glyphHeight * 0.717f * gapScale),
                    )
                }
            }

            if (isFlashlightOn) {
                Icon(
                    painter = painterResource(R.drawable.ic_flashlight_light),
                    contentDescription = stringResource(R.string.aod_flashlight_on),
                    modifier = Modifier
                        .padding(start = if (battery != null) glyphHeight * 1.13f * gapScale else 0.dp)
                        .size(glyphHeight * 0.978f),
                    tint = Mint,
                )
            }

            if (hasPower && hasIcons) {
                Spacer(modifier = Modifier.width(glyphHeight * 1.13f * gapScale))

                Box(
                    modifier = Modifier
                        .size(glyphHeight * 0.228f)
                        .background(color = color, shape = CircleShape),
                )

                Spacer(modifier = Modifier.width(glyphHeight * 1.065f * gapScale))
            }

            if (hasIcons) {
                notifications.icons.forEachIndexed { index, icon ->
                    key(icon.packageName) {
                        Image(
                            bitmap = remember(icon.icon) { icon.icon.asImageBitmap() },
                            contentDescription = null,
                            modifier = Modifier
                                .padding(start = if (index > 0) glyphHeight * 1.13f * gapScale else 0.dp)
                                .size(glyphHeight * 0.978f),
                            colorFilter = ColorFilter.tint(color),
                        )
                    }
                }

                if (notifications.overflowCount > 0) {
                    StatusText(
                        text = stringResource(R.string.aod_notifications_overflow, notifications.overflowCount),
                        color = color,
                        fontSize = fontSize,
                        fontWeight = FontWeight.Normal,
                        fontFamily = fontFamily,
                        modifier = Modifier.padding(start = glyphHeight * 1.13f * gapScale),
                    )
                }
            }
        }
    }
}

// Chữ của dòng trạng thái: chiều cao dòng đúng bằng cỡ chữ (font cao tự nhiên 1,26 lần cỡ chữ, nếu không chốt thì dòng cao hơn icon và bị cắt khi hàng
// bị khống chế chiều cao); số căn giữa theo dòng, nét chữ tràn ra ngoài khung dòng không bị cắt.
@Composable
private fun StatusText(
    text: String,
    color: Color,
    fontSize: TextUnit,
    fontWeight: FontWeight,
    fontFamily: FontFamily,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        maxLines = 1,
        softWrap = false,
        style = TextStyle(
            lineHeight = fontSize,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None,
            ),
        ),
    )
}

// Pin dọc: viền và mức pin tô từ dưới lên (pin yếu tô đỏ); đang sạc thì tô đầy với tia sét khoét đen như ảnh mẫu. Kích thước do nơi gọi đặt,
// nét và bo góc theo bề rộng.
@Composable
private fun AodBatteryGlyph(
    battery: BatteryUiModel,
    color: Color,
    modifier: Modifier = Modifier,
) {
    val level = battery.percent.coerceIn(0, 100) / 100f
    val fill = if (battery.isLow && !battery.isCharging) Red else color

    Box(
        modifier = modifier.drawWithCache {
            val strokeWidth = size.width * 0.14f
            val capHeight = size.height * 0.1f
            val capWidth = size.width * 0.44f
            val bodyTop = capHeight
            val bodyHeight = size.height - bodyTop
            val corner = CornerRadius(size.width * 0.25f)
            val padding = strokeWidth * 1.6f
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
                    cornerRadius = CornerRadius(strokeWidth / 2f),
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
                            cornerRadius = CornerRadius(strokeWidth / 2f),
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
private fun AodStatusLinePreview() {
    AodStatusLine(
        battery = BatteryUiModel(percent = 32, isCharging = true),
        isFlashlightOn = false,
        notifications = AodNotificationsUiModel(),
        color = GreyED,
        glyphHeight = 13.dp,
        fontFamily = FontFamilyPlusJakartaSans,
    )
}
