package com.decoutkhanqindev.aodamn.presentation.model.aod.info

import android.graphics.Bitmap
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.data.device.media.MediaPlayback
import com.decoutkhanqindev.aodamn.data.device.media.MediaProgress

@Immutable
data class MediaUiModel(
    val title: String,
    val artist: String?,
    val isPlaying: Boolean,
    val canSkipToPrevious: Boolean,
    val canSkipToNext: Boolean,
    val progress: MediaProgressUiModel? = null,
    val albumArt: Bitmap? = null,
)

@Immutable
data class MediaProgressUiModel(
    val durationMillis: Long,
    val positionMillis: Long,
    val updatedAtMillis: Long,
    val speed: Float,
) {
    val isAdvancing: Boolean
        get() = speed > 0f

    fun permilleAt(elapsedMillis: Long): Int {
        val positionMillis = positionMillis + ((elapsedMillis - updatedAtMillis) * speed).toLong()
        return (positionMillis * PERMILLE / durationMillis).toInt().coerceIn(0, PERMILLE)
    }

    companion object {
        const val PERMILLE = 1000
    }
}

fun MediaPlayback.toUiModel(): MediaUiModel = MediaUiModel(
    title = title,
    artist = artist,
    isPlaying = isPlaying,
    canSkipToPrevious = canSkipToPrevious,
    canSkipToNext = canSkipToNext,
    progress = progress?.toUiModel(),
    albumArt = albumArt,
)

private fun MediaProgress.toUiModel(): MediaProgressUiModel = MediaProgressUiModel(
    durationMillis = durationMillis,
    positionMillis = positionMillis,
    updatedAtMillis = updatedAtMillis,
    speed = speed,
)
