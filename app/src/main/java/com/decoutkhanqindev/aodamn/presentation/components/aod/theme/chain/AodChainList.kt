package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.chain

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodWeather
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.aodTint
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.AodNotificationsUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.CalendarEventUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.chain.ChainThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState
import com.decoutkhanqindev.aodamn.presentation.theme.Mint
import kotlinx.collections.immutable.ImmutableList
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

// Chuỗi dòng có chấm của Chuỗi như layout app mẫu: mỗi dòng một chấm, vạch mảnh nối xuống chấm dòng sau, dòng cuối không có vạch. isStatusShown:
// ngày (thời tiết cạnh ngày), pin, thông báo; isExtrasShown: ghi nhớ, sự kiện (màn ngang tách ra cột riêng, cũng là chuỗi). Dòng nào không có
// gì để hiện thì ẩn và chuỗi tự nối lại.
@Composable
fun AodChainList(
    state: AodState,
    isStatusShown: Boolean,
    isExtrasShown: Boolean,
    modifier: Modifier = Modifier,
) {
    val config = state.appearance.theme.config
    val components = state.appearance.theme.value.spec.components
    val isWeatherShown = AodComponentValue.WEATHER in components && state.weather != null
    val battery = state.battery?.takeIf { state.extras.isBatteryEnabled }
    val isDateShown = isStatusShown && (state.extras.isDateEnabled || isWeatherShown)
    val isBatteryShown = isStatusShown && (battery != null || state.isFlashlightOn)
    val isNotificationsShown = isStatusShown && (state.notifications.latest != null || state.notifications.icons.isNotEmpty())
    val isMemoShown = isExtrasShown && state.hasChainMemo()
    val isEventsShown = isExtrasShown && state.hasChainEvents()
    val last = listOf(isDateShown, isBatteryShown, isNotificationsShown, isMemoShown, isEventsShown).lastIndexOf(true)
    val titleCenter = rememberTitleDotCenter()

    Column(modifier = modifier) {
        val dateColor = config[ChainThemeSpec.DateColor]
        val dateBrush = config.gradientOf(ChainThemeSpec.DateColor)?.toBrush()
        ChainRow(isVisible = isDateShown, isLast = last == 0, color = dateColor, brush = dateBrush, dotCenter = titleCenter) {
            ChainDate(state = state, color = dateColor, brush = dateBrush, isWeatherShown = isWeatherShown)
        }

        val batteryColor = config[ChainThemeSpec.BatteryColor]
        val batteryBrush = config.gradientOf(ChainThemeSpec.BatteryColor)?.toBrush()
        ChainRow(isVisible = isBatteryShown, isLast = last == 1, color = batteryColor, brush = batteryBrush, dotCenter = titleCenter) {
            ChainBattery(battery = battery, isFlashlightOn = state.isFlashlightOn, color = batteryColor, brush = batteryBrush)
        }

        val notificationsColor = config[ChainThemeSpec.NotificationsColor]
        val notificationsBrush = config.gradientOf(ChainThemeSpec.NotificationsColor)?.toBrush()
        ChainRow(
            isVisible = isNotificationsShown,
            isLast = last == 2,
            color = notificationsColor,
            brush = notificationsBrush,
            dotCenter = if (state.notifications.latest != null) titleCenter else ChainMetrics.IconSize / 2,
        ) {
            ChainNotifications(notifications = state.notifications, color = notificationsColor, brush = notificationsBrush)
        }

        val memoColor = config[ChainThemeSpec.MemoColor]
        val memoBrush = config.gradientOf(ChainThemeSpec.MemoColor)?.toBrush()
        ChainRow(isVisible = isMemoShown, isLast = last == 3, color = memoColor, brush = memoBrush, dotCenter = rememberIconRowDotCenter()) {
            ChainIconBlock(
                iconRes = R.drawable.ic_note_pencil_fill,
                title = stringResource(R.string.aod_memo_label),
                lines = remember(state.extras.memo) { listOf(state.extras.memo.trim()) },
                maxLines = 3,
                color = memoColor,
                brush = memoBrush,
            )
        }

        val eventsColor = config[ChainThemeSpec.EventsColor]
        val eventsBrush = config.gradientOf(ChainThemeSpec.EventsColor)?.toBrush()
        ChainRow(isVisible = isEventsShown, isLast = last == 4, color = eventsColor, brush = eventsBrush, dotCenter = rememberIconRowDotCenter()) {
            ChainEvents(events = state.events, color = eventsColor, brush = eventsBrush)
        }
    }
}

// Ghi nhớ, sự kiện của chuỗi có gì để hiện (màn ngang dùng để chọn bố cục).
fun AodState.hasChainMemo(): Boolean =
    AodComponentValue.MEMO in appearance.theme.value.spec.components && extras.memo.isNotBlank()

fun AodState.hasChainEvents(): Boolean =
    AodComponentValue.EVENTS in appearance.theme.value.spec.components && events.isNotEmpty()

// Một dòng của chuỗi: chấm ở giữa chữ hoa của tiêu đề (dotCenter tính từ đỉnh dòng), vạch từ dưới chấm tới chân dòng. Màu dòng là gradient
// (brush) thì chấm và vạch tô theo khung dòng, chữ / icon theo khung của từng phần. Dòng hiện / ẩn, thành dòng
// cuối hay thôi đều có animation (khoảng cách dưới dòng và vạch mờ dần).
@Composable
private fun ChainRow(
    isVisible: Boolean,
    isLast: Boolean,
    color: Color,
    brush: Brush?,
    dotCenter: Dp,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically(),
    ) {
        val gap by animateDpAsState(targetValue = if (isLast) 0.dp else ChainMetrics.RowGap, label = "ChainRowGap")
        val lineAlpha by animateFloatAsState(targetValue = if (isLast) 0f else 1f, label = "ChainLineAlpha")

        Box(
            modifier = Modifier
                .heightIn(min = if (isLast) ChainMetrics.LastRowMinHeight else ChainMetrics.RowMinHeight)
                .drawBehind {
                    val radius = ChainMetrics.Dot.toPx() / 2f
                    val center = Offset(radius, dotCenter.toPx())
                    if (brush != null) {
                        drawCircle(brush = brush, radius = radius, center = center)
                    } else {
                        drawCircle(color = color, radius = radius, center = center)
                    }

                    val lineTop = center.y + radius + ChainMetrics.LineTop.toPx()
                    val lineWidth = ChainMetrics.Line.toPx()
                    if (lineAlpha > 0f && size.height > lineTop) {
                        val topLeft = Offset(center.x - lineWidth / 2f, lineTop)
                        val lineSize = Size(lineWidth, size.height - lineTop)
                        if (brush != null) {
                            drawRect(brush = brush, topLeft = topLeft, size = lineSize, alpha = lineAlpha)
                        } else {
                            drawRect(color = color, topLeft = topLeft, size = lineSize, alpha = lineAlpha)
                        }
                    }
                }
                .padding(start = ChainMetrics.RailWidth, bottom = gap),
        ) {
            content()
        }
    }
}

// Ngày dài theo ngôn ngữ ("17 THÁNG 10, 2026", "OCTOBER 17, 2026") dưới là thứ; thời tiết (kiểu ở Tùy chỉnh) đứng cạnh ngày.
@Composable
private fun ChainDate(
    state: AodState,
    color: Color,
    brush: Brush?,
    isWeatherShown: Boolean,
) {
    val now = rememberZonedDateTime(state.nowMillis)
    val locale = LocalConfiguration.current.locales[0]
    val dateFormatter = remember(locale) { DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(locale) }
    val weekday = now.format(rememberFormatter(R.string.aod_date_weekday_pattern))
    val components = state.appearance.components
    val isDateEnabled = state.extras.isDateEnabled

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isDateEnabled) {
                ChainTitle(text = now.format(dateFormatter), color = color, brush = brush, modifier = Modifier.weight(1f, fill = false))
            }

            if (isWeatherShown) {
                AodWeather(
                    weather = state.weather,
                    style = components.styleOf(AodComponentValue.WEATHER),
                    layout = components.weatherLayout,
                )
            }
        }

        if (isDateEnabled) {
            ChainSubtitle(text = weekday, color = color, brush = brush, modifier = Modifier.padding(top = ChainMetrics.SubtitleGap))
        }
    }
}

// "PIN 72%" dưới là trạng thái sạc như app mẫu (không sạc / đang sạc / đã sạc đầy); đèn pin đang bật thì icon Mint cạnh pin.
@Composable
private fun ChainBattery(
    battery: BatteryUiModel?,
    isFlashlightOn: Boolean,
    color: Color,
    brush: Brush?,
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (battery != null) {
                ChainTitle(text = stringResource(R.string.aod_chain_battery, battery.percent), color = color, brush = brush)
            }

            if (isFlashlightOn) {
                Icon(
                    painter = painterResource(R.drawable.ic_flashlight_light),
                    contentDescription = stringResource(R.string.aod_flashlight_on),
                    modifier = Modifier
                        .padding(start = if (battery != null) ChainMetrics.IconGap else 0.dp)
                        .size(ChainMetrics.RowIconSize),
                    tint = Mint,
                )
            }
        }

        if (battery != null) {
            val state = when {
                battery.isCharging && battery.percent >= 100 -> R.string.aod_chain_battery_full
                battery.isCharging -> R.string.aod_chain_battery_charging
                else -> R.string.aod_chain_battery_idle
            }

            ChainSubtitle(text = stringResource(state), color = color, brush = brush, modifier = Modifier.padding(top = ChainMetrics.SubtitleGap))
        }
    }
}

// Bật nội dung thông báo thì thông báo mới nhất như notify_lt của app mẫu (icon + tiêu đề, dưới là nội dung tối đa hai dòng), không thì icon
// các app đang có thông báo.
@Composable
private fun ChainNotifications(
    notifications: AodNotificationsUiModel,
    color: Color,
    brush: Brush?,
) {
    val latest = notifications.latest

    if (latest != null) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    bitmap = remember(latest.icon) { latest.icon.asImageBitmap() },
                    contentDescription = null,
                    modifier = Modifier
                        .size(ChainMetrics.IconSize)
                        .aodTint(brush),
                    colorFilter = ColorFilter.tint(color),
                )

                ChainTitle(
                    text = if (latest.isHidden) stringResource(R.string.aod_notification_content_hidden) else latest.title,
                    color = color,
                    brush = brush,
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .padding(start = ChainMetrics.RowIconGap),
                )
            }

            if (!latest.isHidden && latest.text.isNotEmpty()) {
                ChainSubtitle(
                    text = latest.text,
                    color = color,
                    brush = brush,
                    maxLines = 2,
                    modifier = Modifier.padding(top = ChainMetrics.SubtitleGap),
                )
            }
        }
    } else {
        Row(
            horizontalArrangement = Arrangement.spacedBy(ChainMetrics.IconGap),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            notifications.icons.forEach { icon ->
                key(icon.packageName) {
                    Image(
                        bitmap = remember(icon.icon) { icon.icon.asImageBitmap() },
                        contentDescription = null,
                        modifier = Modifier
                            .size(ChainMetrics.IconSize)
                            .aodTint(brush),
                        colorFilter = ColorFilter.tint(color),
                    )
                }
            }

            if (notifications.overflowCount > 0) {
                ChainSubtitle(text = stringResource(R.string.aod_notifications_overflow, notifications.overflowCount), color = color, brush = brush)
            }
        }
    }
}

// Sự kiện gần nhất làm tiêu đề ("09:00  HỌP NHÓM"), các sự kiện sau mỗi cái một dòng phụ.
@Composable
private fun ChainEvents(
    events: ImmutableList<CalendarEventUiModel>,
    color: Color,
    brush: Brush?,
) {
    val timeFormatter = rememberTimeFormatter()
    val allDay = stringResource(R.string.aod_event_all_day)
    val lines = remember(events, timeFormatter, allDay) {
        events.map { event ->
            val time = if (event.isAllDay) {
                allDay
            } else {
                Instant.ofEpochMilli(event.beginMillis).atZone(ZoneId.systemDefault()).format(timeFormatter)
            }
            "$time  ${event.title}"
        }
    }

    ChainIconBlock(
        iconRes = R.drawable.ic_calendar_check_fill,
        title = lines.firstOrNull().orEmpty(),
        lines = lines.drop(1),
        maxLines = 1,
        color = color,
        brush = brush,
    )
}

// Icon đặc sáng như icon thông báo + tiêu đề in hoa đậm như ngày / pin, dưới là các dòng phụ.
@Composable
private fun ChainIconBlock(
    @DrawableRes iconRes: Int,
    title: String,
    lines: List<String>,
    maxLines: Int,
    color: Color,
    brush: Brush?,
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier
                    .size(ChainMetrics.RowIconSize)
                    .aodTint(brush),
                tint = color,
            )

            ChainTitle(
                text = title,
                color = color,
                brush = brush,
                modifier = Modifier
                    .weight(1f, fill = false)
                    .padding(start = ChainMetrics.RowIconGap),
            )
        }

        lines.forEachIndexed { index, line ->
            ChainSubtitle(
                text = line,
                color = color,
                brush = brush,
                maxLines = maxLines,
                modifier = Modifier.padding(top = if (index == 0) ChainMetrics.SubtitleGap else 0.dp),
            )
        }
    }
}

// Tiêu đề in hoa đậm, chiều cao dòng bằng cỡ chữ (như NoPadTextView của app mẫu) để chấm và khoảng cách đúng số đo.
@Composable
private fun ChainTitle(
    text: String,
    color: Color,
    brush: Brush?,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text.uppercase(),
        modifier = modifier.aodTint(brush),
        color = color,
        fontSize = ChainMetrics.TitleSize,
        fontFamily = ChainThemeSpec.TitleFont,
        letterSpacing = ChainMetrics.TITLE_TRACKING.em,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = TextStyle(
            lineHeight = ChainMetrics.TitleSize,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None,
            ),
        ),
    )
}

@Composable
private fun ChainSubtitle(
    text: String,
    color: Color,
    brush: Brush?,
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
) {
    Text(
        text = text.uppercase(),
        modifier = modifier
            .alpha(ChainMetrics.SUBTITLE_ALPHA)
            .aodTint(brush),
        color = color,
        fontSize = ChainMetrics.SubtitleSize,
        fontFamily = ChainThemeSpec.Font,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
    )
}

// Giữa chữ hoa của tiêu đề, tính từ đỉnh dòng.
@Composable
private fun rememberTitleDotCenter(): Dp =
    with(LocalDensity.current) { (ChainMetrics.TitleSize * ChainMetrics.TITLE_DOT_CENTER).toDp() }

// Dòng icon + tiêu đề: hàng cao bằng phần lớn hơn (icon hay dòng tiêu đề), tiêu đề nằm giữa hàng.
@Composable
private fun rememberIconRowDotCenter(): Dp {
    val density = LocalDensity.current
    val titleLine = with(density) { ChainMetrics.TitleSize.toDp() }
    val offset = ((ChainMetrics.RowIconSize - titleLine) / 2).coerceAtLeast(0.dp)
    return offset + rememberTitleDotCenter()
}

@Preview(widthDp = 393, heightDp = 420)
@Composable
private fun AodChainListPreview() {
    AodChainList(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.CHAIN)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
        ),
        isStatusShown = true,
        isExtrasShown = true,
    )
}
