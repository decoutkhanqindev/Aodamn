package com.decoutkhanqindev.custom_aod.presentation.components.aod.theme.jersey

import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.is24HourFormat
import com.decoutkhanqindev.custom_aod.presentation.components.rememberFormatter
import com.decoutkhanqindev.custom_aod.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.custom_aod.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.jersey.JerseyThemeSpec
import com.decoutkhanqindev.custom_aod.presentation.screens.aod.state.AodState
import com.decoutkhanqindev.custom_aod.presentation.theme.FontFamilyBigShouldersDisplay
import kotlin.math.max
import kotlin.math.min
import android.graphics.Path as AndroidPath

// Chủ đề Số áo, màn dọc: giờ và phút hai hàng chữ số rất cao chỉ vẽ nét viền, ngày xếp dọc ngay bên phải hàng giờ. Mỗi chữ số một ô vừa
// đúng nét của nó (số 1 hẹp không để trống một bên), hai hàng cùng nằm giữa một trục; trục đặt sao cho hàng giờ + ngày là một cụm nằm giữa
// màn hình. Phút đổi chỉ làm số phút trượt lại cho cân, hàng giờ và ngày chỉ dời khi giờ đổi. Mọi kích thước tính theo chiều cao một hàng
// số: 100% là 35% chiều cao chỗ trống như app mẫu, bị giới hạn theo bề ngang để vừa màn hẹp.
@Composable
fun AodJerseyClock(
    nowMillis: Long,
    config: AodThemeConfig,
    isDateEnabled: Boolean,
) {
    val time = rememberJerseyTime(nowMillis)
    val glyphs = rememberJerseyGlyphs()
    val scale = config[JerseyThemeSpec.Size] / 100f

    BoxWithConstraints {
        val rowHeight = minOf(maxHeight * ROW_HEIGHT_RATIO, maxWidth / PORTRAIT_WIDTH_RATIO) * scale
        val rowGap = rowHeight * ROW_GAP_RATIO
        val dateGap = rowHeight * DATE_GAP_RATIO

        Layout(
            content = {
                JerseyDigitRow(
                    text = time.hour,
                    glyphs = glyphs,
                    rowHeight = rowHeight,
                    color = config[JerseyThemeSpec.HourColor],
                    modifier = Modifier.clearAndSetSemantics { contentDescription = time.description },
                )

                JerseyDigitRow(
                    text = time.minute,
                    glyphs = glyphs,
                    rowHeight = rowHeight,
                    color = config[JerseyThemeSpec.MinuteColor],
                    modifier = Modifier.clearAndSetSemantics { },
                )

                // Bọc Box: AnimatedVisibility đã ẩn hẳn thì không có node, Layout đọc con theo thứ tự nên luôn cần đủ 3 con.
                Box {
                    AnimatedVisibility(visible = isDateEnabled, enter = fadeIn(), exit = fadeOut()) {
                        JerseyDate(nowMillis = nowMillis, color = config[JerseyThemeSpec.DateColor], rowHeight = rowHeight)
                    }
                }
            },
            modifier = Modifier.width(maxWidth),
        ) { measurables, constraints ->
            val loose = constraints.copy(minWidth = 0, minHeight = 0)
            val hourRow = measurables[0].measure(loose)
            val minuteRow = measurables[1].measure(loose)
            val date = measurables[2].measure(loose)
            val gap = if (date.width > 0) dateGap.roundToPx() else 0
            val rowGapPx = rowGap.roundToPx()
            // Tâm hàng giờ (cũng là tâm hàng phút): cụm hàng giờ + khoảng cách + ngày nằm giữa, không phụ thuộc bề rộng hàng giờ.
            val axis = (constraints.maxWidth - gap - date.width) / 2

            layout(width = constraints.maxWidth, height = hourRow.height + rowGapPx + minuteRow.height) {
                hourRow.place(x = axis - hourRow.width / 2, y = 0)
                date.place(x = axis + hourRow.width / 2 + gap, y = 0)
                minuteRow.place(x = axis - minuteRow.width / 2, y = hourRow.height + rowGapPx)
            }
        }
    }
}

// Số áo, màn ngang: bốn chữ số một hàng, ngày nằm ngang ở giữa màn hình kẹp giữa giờ (bên trái) và phút (bên phải), pin + icon thông báo ở
// góc dưới bên trái thẳng mép hàng giờ. Giờ bám mép trái ngày, phút bám mép phải ngày nên phút đổi không làm giờ và ngày dời chỗ.
@Composable
fun AodJerseyLandscape(state: AodState) {
    val config = state.appearance.theme.config
    val time = rememberJerseyTime(state.nowMillis)
    val glyphs = rememberJerseyGlyphs()
    val scale = config[JerseyThemeSpec.Size] / 100f

    BoxWithConstraints {
        val rowHeight = minOf(maxHeight * LANDSCAPE_ROW_HEIGHT_RATIO, maxWidth / LANDSCAPE_WIDTH_RATIO) * scale
        val sideGap = rowHeight * LANDSCAPE_DATE_GAP_RATIO

        Layout(
            content = {
                JerseyDigitRow(
                    text = time.hour,
                    glyphs = glyphs,
                    rowHeight = rowHeight,
                    color = config[JerseyThemeSpec.HourColor],
                    modifier = Modifier.clearAndSetSemantics { contentDescription = time.description },
                )

                JerseyDigitRow(
                    text = time.minute,
                    glyphs = glyphs,
                    rowHeight = rowHeight,
                    color = config[JerseyThemeSpec.MinuteColor],
                    modifier = Modifier.clearAndSetSemantics { },
                )

                // Bọc Box: khối ẩn hẳn (AnimatedVisibility) thì không có node, Layout đọc con theo thứ tự nên luôn cần đủ 4 con.
                Box {
                    AnimatedVisibility(visible = state.extras.isDateEnabled, enter = fadeIn(), exit = fadeOut()) {
                        JerseyDateHorizontal(
                            nowMillis = state.nowMillis,
                            color = config[JerseyThemeSpec.DateColor],
                            rowHeight = rowHeight,
                        )
                    }
                }

                Box {
                    AodJerseyStatus(
                        battery = state.battery?.takeIf { state.extras.isBatteryEnabled },
                        isFlashlightOn = state.isFlashlightOn,
                        notifications = state.notifications,
                        config = config,
                    )
                }
            },
            modifier = Modifier.width(maxWidth),
        ) { measurables, constraints ->
            val loose = constraints.copy(minWidth = 0, minHeight = 0)
            val hourRow = measurables[0].measure(loose)
            val minuteRow = measurables[1].measure(loose)
            val date = measurables[2].measure(loose)
            val status = measurables[3].measure(loose)
            val gap = sideGap.roundToPx()
            val center = constraints.maxWidth / 2
            val dateX = center - date.width / 2
            val hourX = if (date.width > 0) dateX - gap - hourRow.width else center - gap / 2 - hourRow.width
            val minuteX = if (date.width > 0) dateX + date.width + gap else center + gap / 2
            val digitsHeight = max(hourRow.height, minuteRow.height)

            layout(width = constraints.maxWidth, height = digitsHeight + status.height) {
                hourRow.place(x = hourX, y = 0)
                date.place(x = dateX, y = (digitsHeight - date.height) / 2)
                minuteRow.place(x = minuteX, y = 0)
                status.place(x = hourX, y = digitsHeight)
            }
        }
    }
}

@Stable
private class JerseyTime(
    val hour: String,
    val minute: String,
    val description: String,
)

@Composable
private fun rememberJerseyTime(nowMillis: Long): JerseyTime {
    val now = rememberZonedDateTime(nowMillis)
    val hourFormatter = rememberFormatter(
        if (is24HourFormat()) R.string.aod_hour_pattern_24h else R.string.aod_hour_pattern_12h,
    )
    val minuteFormatter = rememberFormatter(R.string.aod_minute_pattern)
    val timeFormatter = rememberTimeFormatter()
    return remember(now, hourFormatter, minuteFormatter, timeFormatter) {
        JerseyTime(
            hour = now.format(hourFormatter),
            minute = now.format(minuteFormatter),
            description = now.format(timeFormatter),
        )
    }
}

// Một hàng số, các số cách nhau một khoảng cố định.
@Composable
private fun JerseyDigitRow(
    text: String,
    glyphs: JerseyGlyphs,
    rowHeight: Dp,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(rowHeight * DIGIT_GAP_RATIO),
    ) {
        text.forEach { digit ->
            JerseyDigitSlot(digit = digit, glyphs = glyphs, rowHeight = rowHeight, color = color)
        }
    }
}

// Ô rộng đúng nét của chữ số; số đổi thì ô co giãn dần (các số còn lại trượt theo) trong lúc số cũ mờ sang số mới.
@Composable
private fun JerseyDigitSlot(
    digit: Char,
    glyphs: JerseyGlyphs,
    rowHeight: Dp,
    color: Color,
) {
    val strokeWidth = maxOf(rowHeight * STROKE_RATIO, MinStroke)
    val width by animateDpAsState(
        targetValue = rowHeight * glyphs.inkRatio(digit) + strokeWidth,
        animationSpec = tween(durationMillis = DIGIT_CHANGE_MILLIS),
        label = "JerseyDigitWidth",
    )

    Crossfade(
        targetState = digit,
        modifier = Modifier.size(width = width, height = rowHeight),
        animationSpec = tween(durationMillis = DIGIT_CHANGE_MILLIS),
        label = "JerseyDigit",
    ) { target ->
        JerseyDigit(digit = target, glyphs = glyphs, color = color)
    }
}

@Composable
private fun JerseyDigit(
    digit: Char,
    glyphs: JerseyGlyphs,
    color: Color,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawWithCache {
                val strokeWidth = max(size.height * STROKE_RATIO, MinStroke.toPx())
                val outline = glyphs.outline(digit = digit, size = size, inset = strokeWidth / 2f)
                val stroke = Stroke(width = strokeWidth, join = StrokeJoin.Round)

                onDrawBehind {
                    drawPath(path = outline, color = color, style = stroke)
                }
            },
    )
}

// Ngày màn dọc: tháng / ngày xếp dọc (thứ tự theo ngôn ngữ), dưới là thứ in hoa xoay dọc trong viên thuốc viền. Cỡ chữ, nét và khoảng
// cách theo chiều cao hàng số.
@Composable
private fun JerseyDate(
    nowMillis: Long,
    color: Color,
    rowHeight: Dp,
) {
    val now = rememberZonedDateTime(nowMillis)
    val description = now.format(rememberFormatter(R.string.aod_date_full_pattern))
    val density = LocalDensity.current
    val fontSize = with(density) { (rowHeight * DATE_FONT_RATIO).toSp() }
    val weekdaySize = with(density) { (rowHeight * WEEKDAY_FONT_RATIO).toSp() }
    val dateLine = @Composable { text: String ->
        JerseyDateText(text = text, color = color, fontSize = fontSize, lineHeightScale = DATE_LINE_HEIGHT)
    }

    Column(
        modifier = Modifier.clearAndSetSemantics { contentDescription = description },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        dateLine(now.format(rememberFormatter(R.string.aod_jersey_date_first_pattern)))
        dateLine("/")
        dateLine(now.format(rememberFormatter(R.string.aod_jersey_date_second_pattern)))

        JerseyWeekdayPill(
            text = now.format(rememberFormatter(R.string.aod_date_weekday_pattern)).uppercase(),
            color = color,
            fontSize = weekdaySize,
            rowHeight = rowHeight,
            isVertical = true,
            modifier = Modifier.padding(top = rowHeight * PILL_GAP_RATIO),
        )
    }
}

// Ngày màn ngang: "tháng / ngày" một dòng, dưới là thứ in hoa nằm ngang trong viên thuốc viền.
@Composable
private fun JerseyDateHorizontal(
    nowMillis: Long,
    color: Color,
    rowHeight: Dp,
) {
    val now = rememberZonedDateTime(nowMillis)
    val description = now.format(rememberFormatter(R.string.aod_date_full_pattern))
    val density = LocalDensity.current
    val fontSize = with(density) { (rowHeight * LANDSCAPE_DATE_FONT_RATIO).toSp() }
    val weekdaySize = with(density) { (rowHeight * LANDSCAPE_WEEKDAY_FONT_RATIO).toSp() }
    val first = now.format(rememberFormatter(R.string.aod_jersey_date_first_pattern))
    val second = now.format(rememberFormatter(R.string.aod_jersey_date_second_pattern))

    Column(
        modifier = Modifier.clearAndSetSemantics { contentDescription = description },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        JerseyDateText(text = "$first / $second", color = color, fontSize = fontSize, lineHeightScale = DATE_LINE_HEIGHT)

        JerseyWeekdayPill(
            text = now.format(rememberFormatter(R.string.aod_date_weekday_pattern)).uppercase(),
            color = color,
            fontSize = weekdaySize,
            rowHeight = rowHeight,
            isVertical = false,
            modifier = Modifier.padding(top = rowHeight * LANDSCAPE_PILL_GAP_RATIO),
        )
    }
}

// Viên thuốc viền chứa thứ in hoa; khung dòng thấp hơn chữ (chỉ vừa chữ in hoa) để viên thuốc ôm sát như app mẫu.
@Composable
private fun JerseyWeekdayPill(
    text: String,
    color: Color,
    fontSize: TextUnit,
    rowHeight: Dp,
    isVertical: Boolean,
    modifier: Modifier = Modifier,
) {
    val across = rowHeight * if (isVertical) PILL_PADDING_ACROSS_RATIO else LANDSCAPE_PILL_PADDING_ACROSS_RATIO
    val along = rowHeight * if (isVertical) PILL_PADDING_ALONG_RATIO else LANDSCAPE_PILL_PADDING_ALONG_RATIO

    Box(
        modifier = modifier
            .border(width = maxOf(rowHeight * STROKE_RATIO, MinStroke), color = color, shape = CircleShape)
            .padding(
                horizontal = if (isVertical) across else along,
                vertical = if (isVertical) along else across,
            ),
    ) {
        JerseyDateText(
            text = text,
            color = color,
            fontSize = fontSize,
            lineHeightScale = WEEKDAY_LINE_HEIGHT,
            modifier = if (isVertical) Modifier.verticalText() else Modifier,
            letterSpacing = fontSize * WEEKDAY_LETTER_SPACING,
        )
    }
}

@Composable
private fun JerseyDateText(
    text: String,
    color: Color,
    fontSize: TextUnit,
    lineHeightScale: Float,
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
            lineHeight = fontSize * lineHeightScale,
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

@Composable
private fun rememberJerseyGlyphs(): JerseyGlyphs {
    val typeface = LocalFontFamilyResolver.current
        .resolve(fontFamily = FontFamilyBigShouldersDisplay, fontWeight = FontWeight.Black)
        .value as? Typeface ?: Typeface.DEFAULT_BOLD
    return remember(typeface) { JerseyGlyphs(typeface) }
}

// Đường viền 10 chữ số lấy một lần từ font (Paint.getTextPath, gốc ở đường chân chữ). Lúc vẽ, đường viền được kéo cao (co ngang theo
// CONDENSE) cho vừa ô rồi mới vẽ nét, nên nét dày đều chứ không dày ngang mỏng dọc như khi phóng cả chữ đã vẽ.
@Stable
private class JerseyGlyphs(typeface: Typeface) {
    private val outlines: Map<Char, AndroidPath>
    private val widths: Map<Char, Float>
    private val top: Float
    private val bottom: Float

    init {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.typeface = typeface
            textSize = GLYPH_TEXT_SIZE
        }
        outlines = DIGITS.associateWith { digit ->
            AndroidPath().also { path -> paint.getTextPath(digit.toString(), 0, 1, 0f, 0f, path) }
        }

        // Cao chung của mọi chữ số (gồm phần tràn của số tròn) để các số cùng chân, cùng đỉnh.
        val bounds = RectF()
        var minTop = 0f
        var maxBottom = 0f
        outlines.values.forEach { path ->
            path.computeBounds(bounds, true)
            minTop = min(minTop, bounds.top)
            maxBottom = max(maxBottom, bounds.bottom)
        }
        top = minTop
        bottom = maxBottom
        widths = outlines.mapValues { (_, path) -> RectF().also { path.computeBounds(it, true) }.width() }
    }

    // Bề rộng nét của chữ số khi vẽ, tính theo chiều cao hàng số.
    fun inkRatio(digit: Char): Float = (widths[digit] ?: 0f) * CONDENSE / (bottom - top)

    fun outline(digit: Char, size: Size, inset: Float): Path {
        val source = outlines[digit] ?: return Path()
        val bounds = RectF().also { source.computeBounds(it, true) }
        val scaleY = (size.height - inset * 2f) / (bottom - top)
        val scaleX = scaleY * CONDENSE
        val matrix = Matrix().apply {
            setScale(scaleX, scaleY)
            postTranslate(size.width / 2f - bounds.centerX() * scaleX, inset - top * scaleY)
        }
        return AndroidPath(source).apply { transform(matrix) }.asComposePath()
    }
}

private const val DIGITS = "0123456789"
private const val GLYPH_TEXT_SIZE = 1000f
private const val DIGIT_CHANGE_MILLIS = 500

// Tỉ lệ lấy từ app mẫu, theo chiều cao một hàng số.
private const val ROW_HEIGHT_RATIO = 0.35f
private const val PORTRAIT_WIDTH_RATIO = 1.25f
private const val ROW_GAP_RATIO = 0.06f
private const val DIGIT_GAP_RATIO = 0.046f
private const val CONDENSE = 0.68f
private const val STROKE_RATIO = 0.0066f
private const val DATE_GAP_RATIO = 0.08f
private const val DATE_FONT_RATIO = 0.066f
private const val DATE_LINE_HEIGHT = 0.95f
private const val WEEKDAY_FONT_RATIO = 0.058f
private const val WEEKDAY_LINE_HEIGHT = 0.8f
private const val WEEKDAY_LETTER_SPACING = 0.06f
private const val PILL_GAP_RATIO = 0.07f
private const val PILL_PADDING_ACROSS_RATIO = 0.014f
private const val PILL_PADDING_ALONG_RATIO = 0.036f

// Màn ngang: số cao 60% chiều cao màn hình (vẫn chừa chỗ cho dòng pin), cả hàng gồm ngày vừa bề ngang.
private const val LANDSCAPE_ROW_HEIGHT_RATIO = 0.6f
private const val LANDSCAPE_WIDTH_RATIO = 2.6f
private const val LANDSCAPE_DATE_GAP_RATIO = 0.1f
private const val LANDSCAPE_DATE_FONT_RATIO = 0.085f
private const val LANDSCAPE_WEEKDAY_FONT_RATIO = 0.07f
private const val LANDSCAPE_PILL_GAP_RATIO = 0.05f
private const val LANDSCAPE_PILL_PADDING_ACROSS_RATIO = 0.02f
private const val LANDSCAPE_PILL_PADDING_ALONG_RATIO = 0.06f
private val MinStroke = 1.5.dp

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodJerseyClockPreview() {
    AodJerseyClock(
        nowMillis = System.currentTimeMillis(),
        config = AodThemeConfig(),
        isDateEnabled = true,
    )
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodJerseyLandscapePreview() {
    AodJerseyLandscape(state = AodState(nowMillis = System.currentTimeMillis()))
}
