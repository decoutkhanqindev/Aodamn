package com.decoutkhanqindev.custom_aod.presentation.components.aod.block

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.model.aod.info.AodNotificationsUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.info.NotificationContentUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.component.AodComponentStyle
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.component.AodNotificationsLayoutValue
import com.decoutkhanqindev.custom_aod.presentation.theme.Black

// Icon của các app đang có thông báo, font / cỡ / màu theo khối thông báo; mỗi kiểu một bố cục.
@Composable
fun AodNotificationIcons(
    notifications: AodNotificationsUiModel,
    style: AodComponentStyle,
    layout: AodNotificationsLayoutValue,
) {
    AnimatedContent(
        targetState = notifications,
        contentKey = { it.icons.isEmpty() },
        label = "AodNotificationIcons",
    ) { target ->
        if (target.icons.isNotEmpty()) {
            val count = target.icons.size + target.overflowCount
            val overflow = @Composable {
                if (target.overflowCount > 0) {
                    NotificationText(
                        text = stringResource(R.string.aod_notifications_overflow, target.overflowCount),
                        style = style,
                        fontSize = 12,
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }
            }

            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .animateContentSize(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                when (layout) {
                    AodNotificationsLayoutValue.ICONS -> {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp * style.scale)) {
                            target.icons.forEach { icon ->
                                key(icon.packageName) {
                                    Image(
                                        bitmap = remember(icon.icon) { icon.icon.asImageBitmap() },
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp * style.scale),
                                        colorFilter = ColorFilter.tint(style.color),
                                    )
                                }
                            }
                        }
                        overflow()
                    }

                    // Icon chồng lên nhau như ảnh đại diện; nền đen dưới mỗi icon tách nó khỏi icon trước
                    AodNotificationsLayoutValue.STACK -> {
                        Row(horizontalArrangement = Arrangement.spacedBy((-6).dp * style.scale)) {
                            target.icons.forEach { icon ->
                                key(icon.packageName) {
                                    Box(
                                        modifier = Modifier
                                            .size(26.dp * style.scale)
                                            .background(color = Black, shape = CircleShape),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Image(
                                            bitmap = remember(icon.icon) { icon.icon.asImageBitmap() },
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp * style.scale),
                                            colorFilter = ColorFilter.tint(style.color),
                                        )
                                    }
                                }
                            }
                        }
                        overflow()
                    }

                    // Chuông và số thông báo
                    AodNotificationsLayoutValue.BADGE -> {
                        Icon(
                            painter = painterResource(R.drawable.ic_bell_light),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp * style.scale),
                            tint = style.color,
                        )

                        NotificationText(
                            text = stringResource(R.string.aod_notifications_count, count),
                            style = style,
                            fontSize = 14,
                            isBold = true,
                            modifier = Modifier.padding(start = 6.dp),
                        )
                    }

                    // Mỗi app một chấm nhỏ
                    AodNotificationsLayoutValue.DOTS -> {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp * style.scale)) {
                            repeat(target.icons.size) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp * style.scale)
                                        .background(color = style.color, shape = CircleShape),
                                )
                            }
                        }
                        overflow()
                    }
                }
            }
        }
    }
}

// Thông báo mới nhất: kiểu Hàng icon / Xếp lớp thì icon + tiêu đề + nội dung, kiểu Chuông / Chấm thì một dòng gọn "tiêu đề  nội dung".
@Composable
fun AodNotificationContent(
    content: NotificationContentUiModel?,
    style: AodComponentStyle,
    layout: AodNotificationsLayoutValue,
) {
    AnimatedContent(
        targetState = content,
        contentKey = { it?.key },
        label = "AodNotificationContent",
    ) { target ->
        if (target != null) {
            val title = if (target.isHidden) stringResource(R.string.aod_notification_content_hidden) else target.title

            when (layout) {
                AodNotificationsLayoutValue.ICONS, AodNotificationsLayoutValue.STACK ->
                    AodBlock(modifier = Modifier.padding(top = 12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                bitmap = remember(target.icon) { target.icon.asImageBitmap() },
                                contentDescription = null,
                                modifier = Modifier.size(16.dp * style.scale),
                                colorFilter = ColorFilter.tint(style.color),
                            )

                            Column(modifier = Modifier.padding(start = 12.dp)) {
                                NotificationText(text = title, style = style, fontSize = 14, isBold = true)

                                if (target.text.isNotEmpty()) {
                                    NotificationText(
                                        text = target.text,
                                        style = style,
                                        fontSize = 13,
                                        maxLines = 2,
                                        modifier = Modifier.padding(top = 2.dp),
                                    )
                                }
                            }
                        }
                    }

                AodNotificationsLayoutValue.BADGE, AodNotificationsLayoutValue.DOTS -> Row(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .widthIn(max = 280.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    NotificationText(text = title, style = style, fontSize = 13, isBold = true)

                    if (target.text.isNotEmpty()) {
                        NotificationText(
                            text = target.text,
                            style = style,
                            fontSize = 13,
                            modifier = Modifier
                                .weight(1f, fill = false)
                                .padding(start = 8.dp)
                                .alpha(0.7f),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationText(
    text: String,
    style: AodComponentStyle,
    fontSize: Int,
    modifier: Modifier = Modifier,
    isBold: Boolean = false,
    maxLines: Int = 1,
) {
    Text(
        text = text,
        modifier = modifier,
        color = style.color,
        fontSize = fontSize.sp * style.scale,
        fontWeight = if (isBold) FontWeight.SemiBold else FontWeight.Normal,
        fontFamily = style.fontFamily,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
    )
}
