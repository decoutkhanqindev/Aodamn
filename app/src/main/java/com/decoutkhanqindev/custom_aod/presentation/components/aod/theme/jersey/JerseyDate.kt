package com.decoutkhanqindev.custom_aod.presentation.components.aod.theme.jersey

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
import androidx.compose.ui.graphics.Color
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
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.rememberFormatter
import com.decoutkhanqindev.custom_aod.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.jersey.JerseyThemeSpec

// Ngày màn dọc: ba dòng "tháng / ngày" (thứ tự theo ngôn ngữ), dưới là viên thuốc chứa thứ in hoa xoay dọc. Cột rộng cố định bằng viên thuốc;
// cỡ chữ, nét và khoảng cách theo chiều cao hàng số (JerseyMetrics).
@Composable
fun JerseyDateVertical(
    nowMillis: Long,
    color: Color,
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
        JerseyText(now.format(rememberFormatter(R.string.aod_jersey_date_first_pattern)), color, fontSize, lineHeight)
        JerseyText("/", color, fontSize, lineHeight)
        JerseyText(now.format(rememberFormatter(R.string.aod_jersey_date_second_pattern)), color, fontSize, lineHeight)

        JerseyWeekdayPill(
            text = now.format(rememberFormatter(R.string.aod_date_weekday_pattern)).uppercase(),
            color = color,
            rowHeight = rowHeight,
            fontSize = fontSize,
            isVertical = true,
            modifier = Modifier
                .padding(top = rowHeight * JerseyMetrics.PILL_GAP)
                .width(width),
        )
    }
}

// Ngày màn ngang: chính ngày màn dọc xoay nằm ngang nhưng chữ đứng thẳng, "tháng / ngày" rồi viên thuốc chứa thứ, cùng cỡ chữ và nét theo
// unit (R của màn dọc).
@Composable
fun JerseyDateHorizontal(
    nowMillis: Long,
    color: Color,
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
        JerseyText("$first / $second", color, fontSize, lineHeight)

        JerseyWeekdayPill(
            text = now.format(rememberFormatter(R.string.aod_date_weekday_pattern)).uppercase(),
            color = color,
            rowHeight = unit,
            fontSize = fontSize,
            isVertical = false,
            modifier = Modifier
                .padding(start = unit * JerseyMetrics.PILL_GAP)
                .height(unit * JerseyMetrics.DATE_WIDTH),
        )
    }
}

// Viên thuốc viền chứa thứ in hoa, bề dày (ngang của viên dọc, cao của viên ngang) do nơi gọi đặt, dài theo chữ + đệm hai đầu. Khung dòng
// của chữ bằng cỡ chữ (chỉ vừa chữ in hoa) để viên thuốc ôm sát như ảnh mẫu.
@Composable
private fun JerseyWeekdayPill(
    text: String,
    color: Color,
    rowHeight: Dp,
    fontSize: TextUnit,
    isVertical: Boolean,
    modifier: Modifier = Modifier,
) {
    val along = rowHeight * JerseyMetrics.PILL_PAD_ALONG

    Box(
        modifier = modifier
            .border(width = maxOf(rowHeight * JerseyMetrics.PILL_BORDER, JerseyMetrics.MinStroke), color = color, shape = CircleShape)
            .padding(
                horizontal = if (isVertical) 0.dp else along,
                vertical = if (isVertical) along else 0.dp,
            ),
        contentAlignment = Alignment.Center,
    ) {
        JerseyText(
            text = text,
            color = color,
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
    color: Color,
    fontSize: TextUnit,
    lineHeight: TextUnit,
    modifier: Modifier = Modifier,
    letterSpacing: TextUnit = TextUnit.Unspecified,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        fontWeight = FontWeight.Bold,
        fontFamily = JerseyThemeSpec.Font,
        letterSpacing = letterSpacing,
        maxLines = 1,
        softWrap = false,
        style = TextStyle(
            lineHeight = lineHeight,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None,
            ),
        ),
    )
}

// Xoay chữ 90° theo chiều kim đồng hồ (đọc từ trên xuống) và đổi chỗ rộng / cao để khung bao đúng chữ đã xoay.
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
