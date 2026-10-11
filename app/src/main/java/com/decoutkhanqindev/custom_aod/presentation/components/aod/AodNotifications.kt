package com.decoutkhanqindev.custom_aod.presentation.components.aod

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.model.aod.info.AodNotificationsUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.info.NotificationContentUiModel
import com.decoutkhanqindev.custom_aod.presentation.theme.FontFamilyPlusJakartaSans
import com.decoutkhanqindev.custom_aod.presentation.theme.Grey6E
import com.decoutkhanqindev.custom_aod.presentation.theme.Grey8A
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyB4

// Mỗi app một vòng tròn viền mảnh; app có thông báo mới nhất (đứng đầu) viền gradient của app.
@Composable
fun AodNotificationIcons(notifications: AodNotificationsUiModel) {
    AnimatedContent(
        targetState = notifications,
        contentKey = { it.icons.isEmpty() },
        label = "AodNotificationIcons",
    ) { target ->
        if (target.icons.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .animateContentSize(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                target.icons.forEachIndexed { index, icon ->
                    key(icon.packageName) {
                        AodIconRing(isHighlighted = index == 0) {
                            Image(
                                bitmap = remember(icon.icon) { icon.icon.asImageBitmap() },
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                colorFilter = ColorFilter.tint(if (index == 0) GreyB4 else Grey8A),
                            )
                        }
                    }
                }

                if (target.overflowCount > 0) {
                    AodIconRing {
                        Text(
                            text = stringResource(R.string.aod_notifications_overflow, target.overflowCount),
                            color = Grey6E,
                            fontSize = 11.sp,
                            fontFamily = FontFamilyPlusJakartaSans,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AodNotificationContent(content: NotificationContentUiModel?) {
    AnimatedContent(
        targetState = content,
        contentKey = { it?.key },
        label = "AodNotificationContent",
    ) { target ->
        if (target != null) {
            AodCard(modifier = Modifier.padding(top = 12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AodIconRing(size = 32.dp) {
                        Image(
                            bitmap = remember(target.icon) { target.icon.asImageBitmap() },
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            colorFilter = ColorFilter.tint(GreyB4),
                        )
                    }

                    Column(modifier = Modifier.padding(start = 12.dp)) {
                        Text(
                            text = if (target.isHidden) {
                                stringResource(R.string.aod_notification_content_hidden)
                            } else {
                                target.title
                            },
                            color = GreyB4,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamilyPlusJakartaSans,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )

                        if (target.text.isNotEmpty()) {
                            Text(
                                text = target.text,
                                modifier = Modifier.padding(top = 2.dp),
                                color = Grey8A,
                                fontSize = 13.sp,
                                fontFamily = FontFamilyPlusJakartaSans,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
            }
        }
    }
}
