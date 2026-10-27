package com.decoutkhanqindev.aodamn.presentation.components.aod.block

import android.graphics.Bitmap
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.NotificationContentUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodLatestLineStyle

@Composable
fun AodLatestLine(
    text: String,
    icon: Bitmap,
    color: Color,
    brush: Brush?,
    style: AodLatestLineStyle,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            bitmap = remember(icon) { icon.asImageBitmap() },
            contentDescription = null,
            modifier = Modifier
                .size(style.iconSize)
                .aodTint(brush),
            colorFilter = ColorFilter.tint(color),
        )

        Text(
            text = if (style.isUppercase) text.uppercase() else text,
            modifier = Modifier
                .padding(start = style.iconGap)
                .basicMarquee(iterations = 1)
                .aodTint(brush),
            color = color,
            fontSize = style.textSize,
            fontFamily = style.font,
            letterSpacing = style.letterSpacing,
            maxLines = 1,
        )
    }
}

@Composable
fun NotificationContentUiModel.lineText(@StringRes formatRes: Int): String = when {
    isHidden -> stringResource(R.string.aod_notification_content_hidden)
    text.isEmpty() -> title
    else -> stringResource(formatRes, title, text)
}
