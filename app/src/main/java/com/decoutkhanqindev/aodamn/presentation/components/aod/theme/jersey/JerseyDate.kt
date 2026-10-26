package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.jersey

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.jersey.JerseyThemeSpec

@Composable
fun JerseyDateVertical(
    nowMillis: Long,
    brush: Brush,
    rowHeight: Dp,
    modifier: Modifier = Modifier,
) {
    val now = rememberZonedDateTime(nowMillis)
    val description = now.format(rememberFormatter(R.string.aod_date_full_pattern))
    val density = LocalDensity.current
    val fontSize = with(density) { (rowHeight * JerseyMetrics.DATE_FONT).toSp() }
    val lineHeight = with(density) { (rowHeight * JerseyMetrics.DATE_LINE).toSp() }
    val width = rowHeight * JerseyMetrics.DATE_WIDTH

    Column(
        modifier = modifier
            .width(width)
            .clearAndSetSemantics { contentDescription = description },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        JerseyText(now.format(rememberFormatter(R.string.aod_jersey_date_first_pattern)), brush, fontSize, lineHeight)
        JerseyText("/", brush, fontSize, lineHeight)
        JerseyText(now.format(rememberFormatter(R.string.aod_jersey_date_second_pattern)), brush, fontSize, lineHeight)

        JerseyWeekdayPill(
            text = now.format(rememberFormatter(R.string.aod_date_weekday_pattern)).uppercase(),
            brush = brush,
            rowHeight = rowHeight,
            fontSize = fontSize,
            isVertical = true,
            modifier = Modifier
                .padding(top = rowHeight * JerseyMetrics.PILL_GAP)
                .width(width),
        )
    }
}

@Composable
fun JerseyDateHorizontal(
    nowMillis: Long,
    brush: Brush,
    unit: Dp,
    modifier: Modifier = Modifier,
) {
    val now = rememberZonedDateTime(nowMillis)
    val description = now.format(rememberFormatter(R.string.aod_date_full_pattern))
    val density = LocalDensity.current
    val fontSize = with(density) { (unit * JerseyMetrics.DATE_FONT).toSp() }
    val lineHeight = with(density) { (unit * JerseyMetrics.DATE_LINE).toSp() }
    val first = now.format(rememberFormatter(R.string.aod_jersey_date_first_pattern))
    val second = now.format(rememberFormatter(R.string.aod_jersey_date_second_pattern))

    Row(
        modifier = modifier.clearAndSetSemantics { contentDescription = description },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        JerseyText("$first / $second", brush, fontSize, lineHeight)

        JerseyWeekdayPill(
            text = now.format(rememberFormatter(R.string.aod_date_weekday_pattern)).uppercase(),
            brush = brush,
            rowHeight = unit,
            fontSize = fontSize,
            isVertical = false,
            modifier = Modifier
                .padding(start = unit * JerseyMetrics.PILL_GAP)
                .height(unit * JerseyMetrics.DATE_WIDTH),
        )
    }
}

@Composable
private fun JerseyWeekdayPill(
    text: String,
    brush: Brush,
    rowHeight: Dp,
    fontSize: TextUnit,
    isVertical: Boolean,
    modifier: Modifier = Modifier,
) {
    val along = rowHeight * JerseyMetrics.PILL_PAD_ALONG

    Box(
        modifier = modifier
            .border(width = maxOf(rowHeight * JerseyMetrics.PILL_BORDER, JerseyMetrics.MinStroke), brush = brush, shape = CircleShape)
            .padding(
                horizontal = if (isVertical) 0.dp else along,
                vertical = if (isVertical) along else 0.dp,
            ),
        contentAlignment = Alignment.Center,
    ) {
        JerseyText(
            text = text,
            brush = brush,
            fontSize = fontSize,
            lineHeight = fontSize,
            modifier = if (isVertical) Modifier.verticalText() else Modifier,
            letterSpacing = fontSize * JerseyMetrics.WEEKDAY_TRACKING,
        )
    }
}

@Composable
private fun JerseyText(
    text: String,
    brush: Brush,
    fontSize: TextUnit,
    lineHeight: TextUnit,
    modifier: Modifier = Modifier,
    letterSpacing: TextUnit = TextUnit.Unspecified,
) {
    Text(
        text = text,
        modifier = modifier,
        fontSize = fontSize,
        fontWeight = FontWeight.Bold,
        fontFamily = JerseyThemeSpec.Font,
        letterSpacing = letterSpacing,
        maxLines = 1,
        softWrap = false,
        style = TextStyle(
            brush = brush,
            lineHeight = lineHeight,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None,
            ),
        ),
    )
}

private fun Modifier.verticalText(): Modifier = layout { measurable, _ ->
    val placeable = measurable.measure(Constraints())
    layout(width = placeable.height, height = placeable.width) {
        placeable.placeWithLayer(
            x = -(placeable.width - placeable.height) / 2,
            y = (placeable.width - placeable.height) / 2,
        ) {
            rotationZ = 90f
        }
    }
}
