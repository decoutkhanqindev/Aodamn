package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.gentle

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.decoutkhanqindev.aodamn.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.gentle.GentleThemeSpec

@Composable
fun AodGentleClock(
    nowMillis: Long,
    config: AodThemeConfig,
    modifier: Modifier = Modifier,
) {
    val time = rememberZonedDateTime(nowMillis)
    val hours = "%02d".format(time.hour)
    val minutes = "%02d".format(time.minute)
    val description = time.format(rememberTimeFormatter())

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .clearAndSetSemantics { contentDescription = description },
        contentAlignment = Alignment.Center,
    ) {
        val scale = config[GentleThemeSpec.Size] / 100f
        val fontSize = min(maxWidth, maxHeight) * GentleMetrics.CLOCK_SIZE * scale
        val overlap = fontSize * GentleMetrics.CLOCK_OVERLAP
        val density = LocalDensity.current
        val style = TextStyle(
            fontSize = with(density) { fontSize.toSp() },
            fontFamily = GentleThemeSpec.Font,
            fontWeight = config[GentleThemeSpec.Weight].fontWeight,
            textAlign = TextAlign.Center,
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            GentleDigits(text = hours, style = style, config = config, isHour = true)
            GentleDigits(
                text = minutes,
                style = style,
                config = config,
                isHour = false,
                modifier = Modifier.offset(y = -overlap),
            )
        }
    }
}

@Composable
private fun GentleDigits(
    text: String,
    style: TextStyle,
    config: AodThemeConfig,
    isHour: Boolean,
    modifier: Modifier = Modifier,
) {
    AnimatedContent(
        targetState = text,
        modifier = modifier,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "GentleDigits",
    ) { digits ->
        androidx.compose.material3.Text(
            text = digits,
            style = style.copy(
                brush = config.brushOf(if (isHour) GentleThemeSpec.HourColor else GentleThemeSpec.MinuteColor),
            ),
            maxLines = 1,
        )
    }
}
