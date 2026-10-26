package com.decoutkhanqindev.aodamn.data.device.media

import android.graphics.Bitmap

data class MediaPlayback(
    val title: String,
    val artist: String?,
    val isPlaying: Boolean,
    val canSkipToPrevious: Boolean,
    val canSkipToNext: Boolean,
    val progress: MediaProgress?,
    val albumArt: Bitmap? = null,
)

data class MediaProgress(
    val durationMillis: Long,
    val positionMillis: Long,
    val updatedAtMillis: Long,
    val speed: Float,
)
