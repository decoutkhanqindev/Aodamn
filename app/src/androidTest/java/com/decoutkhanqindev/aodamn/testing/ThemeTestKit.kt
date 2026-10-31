package com.decoutkhanqindev.aodamn.testing

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import androidx.core.graphics.createBitmap
import androidx.test.platform.app.InstrumentationRegistry
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.GradientSwatchValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.AodNotificationsUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.CalendarEventUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaProgressUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.NotificationContentUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.NotificationIconUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.WeatherConditionValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.WeatherUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.settings.AodExtrasUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodChoiceOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeChoice
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodToggleOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodBatteryLayoutValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentsUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodDateLayoutValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodEventsLayoutValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodMediaLayoutValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodMemoLayoutValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodNotificationsLayoutValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodWeatherLayoutValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.options
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState
import kotlinx.collections.immutable.toImmutableList
import java.time.ZoneId
import java.time.ZonedDateTime

fun selectedThemeExpectations(): List<ThemeExpectation> {
    val themeId = InstrumentationRegistry.getArguments().getString("themeId")
    return themeId?.let { id ->
        ThemeTestCatalog.expectations.filter { expectation -> expectation.theme.id == id }
    } ?: ThemeTestCatalog.expectations
}

object ThemeTestFixtures {
    fun state(case: ThemeScreenshotCase): AodState = state(
        theme = case.theme,
        profile = case.profile,
        minute = 30,
    )

    fun state(
        theme: AodThemeValue,
        profile: ThemeRenderProfile,
        minute: Int,
    ): AodState {
        val isReference = profile == ThemeRenderProfile.REFERENCE
        val isMinimum = profile == ThemeRenderProfile.MINIMUM
        val isMaximum = profile == ThemeRenderProfile.MAXIMUM
        val hasAllData = isMaximum
        val nowMillis = localMillis(minute)
        val icons = if (isReference) emptyList() else listOf(icon(0), icon(1), icon(2))
        val themeConfig = when {
            isMinimum -> ThemeOptionCases.minimum(theme)
            isMaximum -> ThemeOptionCases.maximum(theme)
            else -> AodThemeConfig()
        }
        val components = if (isMaximum) AodComponentsUiModel(stressComponentConfig()) else AodComponentsUiModel()
        val latest = NotificationContentUiModel(
            key = "sample",
            icon = icon(0),
            title = "Mai gửi một thông báo rất dài",
            text = "Nhớ mang ô và ghé mua đồ trên đường về trước khi trời tối",
            isHidden = false,
        ).takeIf { hasAllData }

        return AodState(
            nowMillis = nowMillis,
            appearance = AodAppearanceUiModel(
                theme = AodThemeUiModel(value = theme, config = themeConfig),
                components = components,
            ),
            extras = AodExtrasUiModel(
                memo = if (hasAllData) "Mua sữa, lấy thuốc, ghé bưu điện và gọi lại cho gia đình trước khi về nhà" else "",
                isCalendarEnabled = hasAllData,
                isWeatherEnabled = hasAllData,
                isDateEnabled = !isReference,
                isBatteryEnabled = !isReference,
            ),
            drawing = drawing().takeIf { hasAllData },
            weather = WeatherUiModel(
                temperature = 24,
                condition = WeatherConditionValue.CLOUDY,
                isDay = true,
                updatedAtMillis = nowMillis,
            ).takeIf { hasAllData },
            events = if (hasAllData) {
                listOf(
                    CalendarEventUiModel(
                        title = "Họp nhóm thiết kế về bản phát hành cuối tháng",
                        beginMillis = nowMillis + 30 * 60_000L,
                        endMillis = nowMillis + 90 * 60_000L,
                        isAllDay = false,
                    ),
                    CalendarEventUiModel(
                        title = "Ăn tối cùng gia đình tại nhà hàng mới",
                        beginMillis = nowMillis + 3 * 60 * 60_000L,
                        endMillis = nowMillis + 4 * 60 * 60_000L,
                        isAllDay = false,
                    ),
                ).toImmutableList()
            } else {
                emptyList<CalendarEventUiModel>().toImmutableList()
            },
            battery = BatteryUiModel(percent = 72, isCharging = hasAllData),
            notifications = AodNotificationsUiModel(
                icons = icons.mapIndexed { index, bitmap ->
                    NotificationIconUiModel(packageName = "sample.$index", icon = bitmap)
                }.toImmutableList(),
                latest = latest,
            ),
            media = MediaUiModel(
                title = "That Cool Song With A Very Long Title",
                artist = "It’s Artist With A Long Name",
                isPlaying = true,
                canSkipToPrevious = true,
                canSkipToNext = true,
                progress = MediaProgressUiModel(
                    durationMillis = 210_000L,
                    positionMillis = 84_000L,
                    updatedAtMillis = 0L,
                    speed = 0f,
                ),
            ).takeIf { hasAllData },
            isHintVisible = false,
        )
    }

    private fun stressComponentConfig(): AodThemeConfig {
        var config = AodThemeConfig()
        AodComponentValue.entries.forEach { component ->
            val options = AodComponentSpec.optionsOf(component)
            config = config
                .with(options.size, options.size.range.last)
                .withRaws(options.color.encodeGradient(GradientSwatchValue.LILAC_SEA.gradient))
            options.font?.let { font -> config = config.withRaw(font.key, font.entries.last().code) }
        }
        return config
            .with(AodComponentSpec.DateLayout, AodDateLayoutValue.CALENDAR)
            .with(AodComponentSpec.BatteryLayout, AodBatteryLayoutValue.RING)
            .with(AodComponentSpec.WeatherLayout, AodWeatherLayoutValue.STACKED)
            .with(AodComponentSpec.EventsLayout, AodEventsLayoutValue.TIMELINE)
            .with(AodComponentSpec.MemoLayout, AodMemoLayoutValue.QUOTE)
            .with(AodComponentSpec.NotificationsLayout, AodNotificationsLayoutValue.STACK)
            .with(AodComponentSpec.MediaLayout, AodMediaLayoutValue.CENTER)
    }

    private fun localMillis(minute: Int): Long = ZonedDateTime.of(
        2026,
        10,
        13,
        8,
        minute,
        0,
        0,
        ZoneId.systemDefault(),
    ).toInstant().toEpochMilli()

    private fun icon(index: Int): Bitmap = createBitmap(48, 48).apply {
        val canvas = Canvas(this)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = when (index % 3) {
                0 -> Color.rgb(66, 133, 244)
                1 -> Color.rgb(234, 67, 53)
                else -> Color.rgb(52, 168, 83)
            }
        }
        when (index % 3) {
            0 -> canvas.drawCircle(24f, 24f, 18f, paint)
            1 -> canvas.drawRoundRect(7f, 9f, 41f, 39f, 7f, 7f, paint)
            else -> canvas.drawPath(
                Path().apply {
                    moveTo(24f, 5f)
                    lineTo(44f, 41f)
                    lineTo(4f, 41f)
                    close()
                },
                paint,
            )
        }
    }

    private fun drawing(): Bitmap = createBitmap(120, 120).apply {
        val canvas = Canvas(this)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            style = Paint.Style.STROKE
            strokeWidth = 5f
            strokeCap = Paint.Cap.ROUND
        }
        val path = Path().apply {
            moveTo(60f, 100f)
            cubicTo(12f, 72f, 12f, 26f, 38f, 26f)
            cubicTo(52f, 26f, 60f, 38f, 60f, 48f)
            cubicTo(60f, 38f, 68f, 26f, 82f, 26f)
            cubicTo(108f, 26f, 108f, 72f, 60f, 100f)
        }
        canvas.drawPath(path, paint)
    }
}

data class SharedBlockCase(
    val component: AodComponentValue,
    val layout: AodThemeChoice?,
) {
    val name: String
        get() = "${component.key}_${layout?.code ?: 0}"

    override fun toString(): String = name
}

object SharedBlockTestCatalog {
    val cases: List<SharedBlockCase> = buildList {
        addAll(AodDateLayoutValue.entries.map { layout -> SharedBlockCase(AodComponentValue.DATE, layout) })
        addAll(AodBatteryLayoutValue.entries.map { layout -> SharedBlockCase(AodComponentValue.BATTERY, layout) })
        addAll(AodWeatherLayoutValue.entries.map { layout -> SharedBlockCase(AodComponentValue.WEATHER, layout) })
        addAll(AodEventsLayoutValue.entries.map { layout -> SharedBlockCase(AodComponentValue.EVENTS, layout) })
        addAll(AodMemoLayoutValue.entries.map { layout -> SharedBlockCase(AodComponentValue.MEMO, layout) })
        addAll(AodNotificationsLayoutValue.entries.map { layout -> SharedBlockCase(AodComponentValue.NOTIFICATIONS, layout) })
        addAll(AodMediaLayoutValue.entries.map { layout -> SharedBlockCase(AodComponentValue.MEDIA, layout) })
        add(SharedBlockCase(AodComponentValue.DRAWING, null))
    }
}