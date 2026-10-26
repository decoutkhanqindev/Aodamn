package com.decoutkhanqindev.aodamn.presentation.model.customize

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Path
import androidx.compose.runtime.Immutable
import androidx.core.graphics.applyCanvas
import androidx.core.graphics.createBitmap
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.AodNotificationsUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.CalendarEventUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaProgressUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.NotificationContentUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.NotificationIconUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.WeatherConditionValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.WeatherUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.settings.interaction.AodActionValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.settings.interaction.AodGestureValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.settings.interaction.AodInteractionUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import java.time.LocalDate
import java.time.ZoneId

@Immutable
data class CustomizePreviewSamples(
    val notificationTitle: String,
    val notificationText: String,
    val eventTitle: String,
    val mediaTitle: String,
    val mediaArtist: String,
    val memo: String,
    val icons: ImmutableList<Bitmap> = sampleIcons(),
    val nowMillis: Long = previewMillis(),
) {
    private enum class IconShape { CIRCLE, SQUARE, TRIANGLE }

    companion object {
        private const val ICON_SIZE = 48
        private const val PREVIEW_HOUR = 8
        private const val PREVIEW_MINUTE = 30
        private const val BATTERY_PERCENT = 72
        private const val TEMPERATURE_C = 28
        private const val TEMPERATURE_F = 82
        private const val HOUR_MILLIS = 60 * 60_000L
        private const val MEDIA_DURATION_MILLIS = 210_000L
        private const val MEDIA_POSITION_MILLIS = 130_000L

        private val PreviewInteraction = AodInteractionUiModel(
            actions = AodGestureValue.entries.associateWith { AodActionValue.NONE }
                .toImmutableMap(),
            isAutoDimEnabled = false,
            isRaiseToWakeEnabled = false,
        )

        private fun previewMillis(): Long =
            LocalDate.now().atTime(PREVIEW_HOUR, PREVIEW_MINUTE).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        private fun sampleIcons(): ImmutableList<Bitmap> = IconShape.entries.map { shape ->
            createBitmap(ICON_SIZE, ICON_SIZE).applyCanvas {
                val paint =
                    Paint(Paint.ANTI_ALIAS_FLAG).apply { color = android.graphics.Color.WHITE }
                val size = ICON_SIZE.toFloat()
                val inset = size / 6
                when (shape) {
                    IconShape.CIRCLE -> drawCircle(size / 2, size / 2, size / 2 - inset, paint)
                    IconShape.SQUARE -> drawRoundRect(
                        inset,
                        inset,
                        size - inset,
                        size - inset,
                        inset,
                        inset,
                        paint
                    )

                    IconShape.TRIANGLE -> drawPath(
                        Path().apply {
                            moveTo(size / 2, inset)
                            lineTo(size - inset, size - inset)
                            lineTo(inset, size - inset)
                            close()
                        },
                        paint,
                    )
                }
            }
        }.toImmutableList()

        private fun CustomizeDraftUiModel.isThemePreviewMemo(): Boolean =
            isThemePreview && AodBlockValue.MEMO in appearance.clock.theme.value.spec.ownBlocks

        fun CustomizeDraftUiModel.toPreviewAodState(
            isGlowing: Boolean,
            samples: CustomizePreviewSamples,
            shownComponent: AodComponentValue? = null,
            isStill: Boolean = false,
        ): AodState = AodState(
            nowMillis = samples.nowMillis,
            appearance = appearance.clock.copy(isLandscape = false),
            interaction = PreviewInteraction,
            isPreview = true,
            isStill = isStill,
            extras = extras.copy(
                memo = if (extras.memo.isBlank() && (shownComponent == AodComponentValue.MEMO || isThemePreviewMemo())) {
                    samples.memo
                } else {
                    extras.memo
                },
                isDateEnabled = extras.isDateEnabled || shownComponent == AodComponentValue.DATE,
                isBatteryEnabled = extras.isBatteryEnabled || shownComponent == AodComponentValue.BATTERY,
            ),
            wallpaper = decor.wallpaper,
            background = decor.background,
            drawing = decor.drawing ?: samples.icons.first().takeIf { shownComponent == AodComponentValue.DRAWING },
            weather = WeatherUiModel(
                temperature = if (info.isWeatherFahrenheit) TEMPERATURE_F else TEMPERATURE_C,
                condition = WeatherConditionValue.CLEAR,
                isDay = true,
                updatedAtMillis = samples.nowMillis,
            ).takeIf { info.isWeatherEnabled || shownComponent == AodComponentValue.WEATHER },
            events = if (info.isCalendarEnabled || shownComponent == AodComponentValue.EVENTS) {
                persistentListOf(
                    CalendarEventUiModel(
                        title = samples.eventTitle,
                        beginMillis = samples.nowMillis + HOUR_MILLIS,
                        endMillis = samples.nowMillis + 2 * HOUR_MILLIS,
                        isAllDay = false,
                    ),
                )
            } else {
                persistentListOf()
            },
            battery = BatteryUiModel(percent = BATTERY_PERCENT, isCharging = false),
            notifications = AodNotificationsUiModel(
                icons = if (info.isNotificationIconsEnabled || shownComponent == AodComponentValue.NOTIFICATIONS) {
                    samples.icons
                        .mapIndexed { index, icon ->
                            NotificationIconUiModel(
                                packageName = "sample.$index",
                                icon = icon
                            )
                        }
                        .toImmutableList()
                } else {
                    persistentListOf()
                },
                latest = NotificationContentUiModel(
                    key = "sample",
                    icon = samples.icons.first(),
                    title = samples.notificationTitle,
                    text = samples.notificationText,
                    isHidden = false,
                ).takeIf { info.isNotificationContentEnabled || shownComponent == AodComponentValue.NOTIFICATIONS },
            ),
            media = MediaUiModel(
                title = samples.mediaTitle,
                artist = samples.mediaArtist,
                isPlaying = true,
                canSkipToPrevious = true,
                canSkipToNext = true,
                progress = MediaProgressUiModel(
                    durationMillis = MEDIA_DURATION_MILLIS,
                    positionMillis = MEDIA_POSITION_MILLIS,
                    updatedAtMillis = 0L,
                    speed = 0f,
                ),
            ).takeIf { info.isMediaControlsEnabled || shownComponent == AodComponentValue.MEDIA },
            isHintVisible = false,
            isGlowing = isGlowing,
        )
    }
}
