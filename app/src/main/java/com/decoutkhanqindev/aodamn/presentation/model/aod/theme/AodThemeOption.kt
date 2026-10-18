package com.decoutkhanqindev.aodamn.presentation.model.aod.theme

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.toStoredClockColor
import kotlinx.collections.immutable.ImmutableList

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

@Immutable
class AodColorOption(
    override val key: String,
    @param:StringRes override val labelRes: Int,
    override val default: Color,
) : AodThemeOption<Color> {
    override fun encode(value: Color): Int = value.toStoredClockColor()

    override fun decode(raw: Int?): Color = raw?.let { Color(it) } ?: default
}

// labelRes nhận giá trị hiện tại làm tham số, vd "Cỡ đồng hồ: %1$d%%".
@Immutable
class AodRangeOption(
    override val key: String,
    @param:StringRes override val labelRes: Int,
    val range: IntRange,
    val step: Int,
    override val default: Int,
) : AodThemeOption<Int> {
    override fun encode(value: Int): Int = value

    override fun decode(raw: Int?): Int = raw?.coerceIn(range) ?: default
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
