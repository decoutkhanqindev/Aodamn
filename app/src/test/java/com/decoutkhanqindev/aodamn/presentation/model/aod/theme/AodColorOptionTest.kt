package com.decoutkhanqindev.aodamn.presentation.model.aod.theme

import androidx.compose.ui.graphics.Color
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodGradientDirection
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.GradientSwatchValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentsUiModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

// Gradient lưu bằng khoá phụ cạnh khoá màu cũ: màu đã lưu trước khi có gradient phải đọc ra đúng như cũ, đặt lại phải xoá cả khoá phụ.
class AodColorOptionTest {

    private val solid = AodColorOption(key = "digit_color", labelRes = R.string.opt_clock_color, default = Color.Red, isGradientAllowed = true)
    private val sunset = GradientSwatchValue.DEFAULT.gradient
    private val gradientDefault = AodColorOption(
        key = "line_color",
        labelRes = R.string.opt_clock_color,
        default = sunset.start,
        defaultGradient = sunset,
    )

    @Test
    fun `stored color without extra keys stays a solid color`() {
        val config = AodThemeConfig().withRaw(solid.key, solid.encode(Color.Blue))

        assertEquals(Color.Blue, config[solid])
        assertNull(config.gradientOf(solid))
    }

    @Test
    fun `gradient round trips and keeps the start color in the main key`() {
        val gradient = GradientSwatchValue.LAVA.gradient.copy(direction = AodGradientDirection.DIAGONAL_UP)
        val config = AodThemeConfig().withRaws(solid.encodeGradient(gradient))

        assertEquals(gradient, config.gradientOf(solid))
        assertEquals(gradient.start, config[solid])
    }

    @Test
    fun `switching back to solid removes the extra keys`() {
        val config = AodThemeConfig()
            .withRaws(solid.encodeGradient(sunset))
            .withRaws(solid.encodeSolid(Color.Green))

        assertNull(config.gradientOf(solid))
        assertEquals(setOf(solid.key), config.values.keys)
    }

    @Test
    fun `default gradient applies until the user picks a color`() {
        assertEquals(sunset, AodThemeConfig().gradientOf(gradientDefault))
        assertTrue(gradientDefault.isDefault(AodThemeConfig()))

        val picked = AodThemeConfig().withRaws(gradientDefault.encodeSolid(sunset.start))
        assertNull(picked.gradientOf(gradientDefault))
        assertFalse(gradientDefault.isDefault(picked))
    }

    @Test
    fun `reset removes the extra keys too`() {
        val config = AodThemeConfig().withRaws(solid.encodeGradient(sunset)).without(listOf(solid))

        assertTrue(config.values.isEmpty())
    }

    @Test
    fun `a direction change alone is a change`() {
        val first = AodThemeConfig().withRaws(solid.encodeGradient(sunset))
        val second = AodThemeConfig().withRaws(solid.encodeGradient(sunset.copy(direction = AodGradientDirection.VERTICAL)))

        assertFalse(solid.isSame(first, second))
    }

    @Test
    fun `shared blocks except drawing allow a gradient and carry it to their style`() {
        AodComponentValue.entries.forEach { component ->
            val option = AodComponentSpec.optionsOf(component).color
            assertEquals(component.name, component != AodComponentValue.DRAWING, option.isGradientAllowed)
        }

        val date = AodComponentSpec.optionsOf(AodComponentValue.DATE).color
        val components = AodComponentsUiModel(AodThemeConfig().withRaws(date.encodeGradient(sunset)))
        assertEquals(sunset, components.styleOf(AodComponentValue.DATE).gradient)
        assertNull(components.styleOf(AodComponentValue.MEMO).gradient)
    }
}
