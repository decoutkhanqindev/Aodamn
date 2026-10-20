package com.decoutkhanqindev.aodamn.presentation.model.aod.theme

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodGradient
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodGradientDirection
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.toStoredClockColor
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

// Một mục của thuộc tính dạng danh sách (hình, kiểu khối, font, hiệu ứng): mã lưu và nhãn hiển thị.
interface AodThemeChoice {
    val code: Int

    @get:StringRes
    val labelRes: Int
}

// Một thuộc tính sửa được của chủ đề. Giá trị lưu thành Int trong AodThemeConfig (mã mục, ARGB, 0 / 1, số) nên thêm thuộc tính
// mới không phải đổi cách lưu; màn sửa chủ đề dựng control theo loại thuộc tính, chủ đề chỉ khai báo danh sách thuộc tính.
@Immutable
sealed interface AodThemeOption<T> {
    val key: String

    @get:StringRes
    val labelRes: Int
    val default: T

    fun encode(value: T): Int

    fun decode(raw: Int?): T
}

// Mục là AodShapeValue thì màn sửa vẽ hình thu nhỏ, là ClockFontValue thì chip viết bằng font đó, còn lại là chip chữ.
@Immutable
class AodChoiceOption<E : AodThemeChoice>(
    override val key: String,
    @param:StringRes override val labelRes: Int,
    val entries: ImmutableList<E>,
    override val default: E,
) : AodThemeOption<E> {
    override fun encode(value: E): Int = value.code

    override fun decode(raw: Int?): E = entries.find { it.code == raw } ?: default
}

// Khoá chính giữ màu một màu (= màu Đầu của gradient) nên code chỉ đọc Color không phải sửa. Gradient ghi thêm khoá phụ (gradientKeys);
// không có khoá phụ là một màu, nên màu user đã lưu trước khi có gradient giữ nguyên. isGradientAllowed tắt thì bộ chọn chỉ có tab Một
// màu (bật ở chỗ đã tô được gradient). defaultGradient khác null thì mặc định là gradient đó, default phải là màu Đầu của nó.
@Immutable
class AodColorOption(
    override val key: String,
    @param:StringRes override val labelRes: Int,
    override val default: Color,
    val defaultGradient: AodGradient? = null,
    isGradientAllowed: Boolean = false,
) : AodThemeOption<Color> {
    val isGradientAllowed: Boolean = isGradientAllowed || defaultGradient != null

    val middleKey: String = "${key}_gradient_2"
    val endKey: String = "${key}_gradient_3"
    val directionKey: String = "${key}_gradient_direction"

    val gradientKeys: List<String> = listOf(middleKey, endKey, directionKey)

    init {
        require(defaultGradient == null || defaultGradient.start == default) { "default must be the first color of defaultGradient" }
    }

    override fun encode(value: Color): Int = value.toStoredClockColor()

    override fun decode(raw: Int?): Color = raw?.let { Color(it) } ?: default

    // Khoá chính chưa ghi (chưa chỉnh lần nào) thì theo mặc định; đã ghi mà không có khoá phụ là user chọn một màu.
    fun decodeGradient(values: Map<String, Int>): AodGradient? {
        if (key !in values) return defaultGradient
        val middle = values[middleKey] ?: return null
        val end = values[endKey] ?: return null
        val direction = AodGradientDirection.fromCode(values[directionKey]) ?: return null
        return AodGradient(persistentListOf(decode(values[key]), Color(middle), Color(end)), direction)
    }

    // Giá trị ghi cho một lần đổi (null = xoá khoá): một màu xoá khoá phụ, gradient ghi đủ 4 khoá.
    fun encodeSolid(color: Color): Map<String, Int?> =
        mapOf(key to encode(color)) + gradientKeys.associateWith { null }

    fun encodeGradient(gradient: AodGradient): Map<String, Int?> = mapOf(
        key to encode(gradient.colors[0]),
        middleKey to encode(gradient.colors[1]),
        endKey to encode(gradient.colors[2]),
        directionKey to gradient.direction.code,
    )
}

// labelRes nhận giá trị hiện tại làm tham số, vd "Cỡ đồng hồ: %1$d%%". autoLabelRes khác null: thuộc tính có chế độ tự động (giá trị AUTO,
// ngoài range, vd vị trí theo giờ của Dải màu), màn sửa có công tắc tự động, tắt thì thanh trượt ghim một giá trị trong range.
@Immutable
class AodRangeOption(
    override val key: String,
    @param:StringRes override val labelRes: Int,
    val range: IntRange,
    val step: Int,
    override val default: Int,
    @param:StringRes val autoLabelRes: Int? = null,
) : AodThemeOption<Int> {
    override fun encode(value: Int): Int = value

    override fun decode(raw: Int?): Int = when {
        raw == null -> default
        autoLabelRes != null && raw == AUTO -> AUTO
        else -> raw.coerceIn(range)
    }

    companion object {
        const val AUTO = -1
    }
}

@Immutable
class AodToggleOption(
    override val key: String,
    @param:StringRes override val labelRes: Int,
    override val default: Boolean,
) : AodThemeOption<Boolean> {
    override fun encode(value: Boolean): Int = if (value) 1 else 0

    override fun decode(raw: Int?): Boolean = raw?.let { it != 0 } ?: default
}
