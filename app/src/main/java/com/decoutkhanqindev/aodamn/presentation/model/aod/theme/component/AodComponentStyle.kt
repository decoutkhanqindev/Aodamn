package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.data.local.datastore.DataStoreManager
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodGradient
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.ClockFontValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodChoiceOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodOptionSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeChoice
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.toAodConfigString
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.toAodConfigValues
import com.decoutkhanqindev.aodamn.presentation.theme.GreyB4
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

// Các khối dùng chung trên AOD mà user chỉnh font, cỡ, màu riêng từng khối ở Tùy chỉnh. ownBlock: khối chủ đề có thể tự vẽ thay bản
// mặc định; chủ đề đang dùng tự vẽ khối đó thì kiểu do chủ đề quyết định (chỉnh ở màn chủ đề), kiểu ở đây không áp dụng.
@Immutable
enum class AodComponentValue(
    val key: String,
    @param:DrawableRes val iconRes: Int,
    @param:StringRes val labelRes: Int,
    val hasFont: Boolean = true,
    val ownBlock: AodBlockValue? = null,
) {
    DATE("date", R.drawable.ic_calendar_blank, R.string.customize_tab_date, ownBlock = AodBlockValue.DATE),
    BATTERY("battery", R.drawable.ic_battery_full, R.string.customize_tab_battery, ownBlock = AodBlockValue.STATUS),
    WEATHER("weather", R.drawable.ic_cloud_sun, R.string.customize_tab_weather),
    EVENTS("events", R.drawable.ic_calendar_check, R.string.customize_tab_events, ownBlock = AodBlockValue.EVENTS),
    MEMO("memo", R.drawable.ic_note_pencil, R.string.customize_tab_memo, ownBlock = AodBlockValue.MEMO),
    NOTIFICATIONS("notifications", R.drawable.ic_bell, R.string.customize_tab_notifications, ownBlock = AodBlockValue.STATUS),
    MEDIA("media", R.drawable.ic_music_notes, R.string.customize_tab_media, ownBlock = AodBlockValue.MEDIA),
    DRAWING("drawing", R.drawable.ic_paint_brush, R.string.customize_tab_drawing, hasFont = false),
}

// Thuộc tính của từng khối: kiểu hiển thị (trừ nét vẽ), font (trừ nét vẽ), cỡ và màu; khoá
// "<khối>_layout", "<khối>_font", "<khối>_size", "<khối>_color" trong cùng một AodThemeConfig.
object AodComponentSpec : AodOptionSpec {
    val DateLayout = layoutOption(AodComponentValue.DATE, AodDateLayoutValue.entries, AodDateLayoutValue.SPACED)
    val BatteryLayout = layoutOption(AodComponentValue.BATTERY, AodBatteryLayoutValue.entries, AodBatteryLayoutValue.ICON)
    val WeatherLayout = layoutOption(AodComponentValue.WEATHER, AodWeatherLayoutValue.entries, AodWeatherLayoutValue.INLINE)
    val EventsLayout = layoutOption(AodComponentValue.EVENTS, AodEventsLayoutValue.entries, AodEventsLayoutValue.LIST)
    val MemoLayout = layoutOption(AodComponentValue.MEMO, AodMemoLayoutValue.entries, AodMemoLayoutValue.ICON)
    val NotificationsLayout = layoutOption(
        AodComponentValue.NOTIFICATIONS,
        AodNotificationsLayoutValue.entries,
        AodNotificationsLayoutValue.ICONS,
    )
    val MediaLayout = layoutOption(AodComponentValue.MEDIA, AodMediaLayoutValue.entries, AodMediaLayoutValue.ROW)

    class Options(val component: AodComponentValue, val layout: AodChoiceOption<*>?) {
        val font: AodChoiceOption<ClockFontValue>? = if (component.hasFont) {
            AodChoiceOption(
                key = "${component.key}_font",
                labelRes = R.string.opt_clock_font,
                entries = ClockFontValue.entries.toImmutableList(),
                default = ClockFontValue.DEFAULT,
            )
        } else {
            null
        }
        val size = AodRangeOption(
            key = "${component.key}_size",
            labelRes = R.string.opt_size_value,
            range = 60..150,
            step = 10,
            default = 100,
        )
        // Gradient tô lên chữ / icon / nét vẽ của khối (Modifier.aodTint).
        val color = AodColorOption(
            key = "${component.key}_color",
            labelRes = R.string.opt_clock_color,
            default = GreyB4,
            isGradientAllowed = true,
        )
        val tab = AodThemeTab(
            iconRes = component.iconRes,
            labelRes = component.labelRes,
            options = listOfNotNull<AodThemeOption<*>>(layout, font, size, color).toImmutableList(),
        )
    }

    private val byComponent = AodComponentValue.entries.associateWith { component ->
        Options(
            component = component,
            layout = when (component) {
                AodComponentValue.DATE -> DateLayout
                AodComponentValue.BATTERY -> BatteryLayout
                AodComponentValue.WEATHER -> WeatherLayout
                AodComponentValue.EVENTS -> EventsLayout
                AodComponentValue.MEMO -> MemoLayout
                AodComponentValue.NOTIFICATIONS -> NotificationsLayout
                AodComponentValue.MEDIA -> MediaLayout
                AodComponentValue.DRAWING -> null
            },
        )
    }

    fun optionsOf(component: AodComponentValue): Options = byComponent.getValue(component)

    override val tabs = byComponent.values.map { options -> options.tab }.toImmutableList()

    private fun <E : AodThemeChoice> layoutOption(component: AodComponentValue, entries: List<E>, default: E) = AodChoiceOption(
        key = "${component.key}_layout",
        labelRes = R.string.opt_layout,
        entries = entries.toImmutableList(),
        default = default,
    )
}

@Immutable
data class AodComponentStyle(
    val font: ClockFontValue,
    val scale: Float,
    val color: Color,
    val gradient: AodGradient? = null,
) {
    val fontFamily: FontFamily
        get() = font.fontFamily
}

@Immutable
data class AodComponentsUiModel(val config: AodThemeConfig = AodThemeConfig()) {
    val dateLayout: AodDateLayoutValue
        get() = config[AodComponentSpec.DateLayout]

    val batteryLayout: AodBatteryLayoutValue
        get() = config[AodComponentSpec.BatteryLayout]

    val weatherLayout: AodWeatherLayoutValue
        get() = config[AodComponentSpec.WeatherLayout]

    val eventsLayout: AodEventsLayoutValue
        get() = config[AodComponentSpec.EventsLayout]

    val memoLayout: AodMemoLayoutValue
        get() = config[AodComponentSpec.MemoLayout]

    val notificationsLayout: AodNotificationsLayoutValue
        get() = config[AodComponentSpec.NotificationsLayout]

    val mediaLayout: AodMediaLayoutValue
        get() = config[AodComponentSpec.MediaLayout]

    fun styleOf(component: AodComponentValue): AodComponentStyle {
        val options = AodComponentSpec.optionsOf(component)
        return AodComponentStyle(
            font = options.font?.let { font -> config[font] } ?: ClockFontValue.DEFAULT,
            scale = config[options.size] / 100f,
            color = config[options.color],
            gradient = config.gradientOf(options.color),
        )
    }
}

private fun String?.toComponents(): AodComponentsUiModel =
    AodComponentsUiModel(AodThemeConfig(toAodConfigValues().toPersistentMap()))

fun DataStoreManager.currentAodComponents(): AodComponentsUiModel = aodComponentStyles.value.toComponents()

fun DataStoreManager.observeAodComponents(): Flow<AodComponentsUiModel> =
    aodComponentStyles.filterNotNull().map { raw -> raw.toComponents() }

fun DataStoreManager.saveAodComponents(components: AodComponentsUiModel) {
    saveAodComponentStyles(components.config.values.toAodConfigString())
}
