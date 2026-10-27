package com.decoutkhanqindev.aodamn.presentation.components.aod.block

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.NotificationContentUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBadgeLatestStyle
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.theme.Black

@Composable
fun AodBadgeLatest(
    latest: NotificationContentUiModel,
    config: AodThemeConfig,
    colorOption: AodColorOption,
    style: AodBadgeLatestStyle,
    modifier: Modifier = Modifier,
) {
    val brush = config.brushOf(colorOption)
    val text = if (latest.isHidden) stringResource(R.string.aod_notification_content_hidden) else latest.text

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(style.badgeSize)
                .background(brush = brush, shape = CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                bitmap = remember(latest.icon) { latest.icon.asImageBitmap() },
                contentDescription = null,
                modifier = Modifier.size(style.iconSize),
                colorFilter = ColorFilter.tint(Black),
            )
        }

        Column(
            modifier = Modifier
                .then(if (style.isTextShrinkable) Modifier.weight(1f, fill = false) else Modifier)
                .padding(start = style.textStartGap),
        ) {
            Text(
                text = latest.title,
                modifier = if (style.isTitleMarquee) Modifier.basicMarquee(iterations = 1) else Modifier,
                style = TextStyle(brush = brush, fontSize = style.titleSize, fontFamily = style.font),
                maxLines = 1,
                overflow = if (style.isTitleMarquee) TextOverflow.Clip else TextOverflow.Ellipsis,
            )

            if (text.isNotEmpty()) {
                Text(
                    text = text,
                    modifier = Modifier.padding(top = style.textGap),
                    style = TextStyle(
                        brush = config.darkBrushOf(colorOption, style.textDarken),
                        fontSize = style.textSize,
                        fontWeight = style.textWeight,
                        fontFamily = style.font,
                    ),
                    maxLines = style.textMaxLines,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
