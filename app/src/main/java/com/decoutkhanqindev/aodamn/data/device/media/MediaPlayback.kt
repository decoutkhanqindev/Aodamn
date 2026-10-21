package com.decoutkhanqindev.aodamn.data.device.media

import android.graphics.Bitmap

// albumArt: ảnh bìa đã thu nhỏ (cạnh dài ART_SIZE_PX), null nếu app nhạc không gửi.
data class MediaPlayback(
    val title: String,
    val artist: String?,
    val isPlaying: Boolean,
    val canSkipToPrevious: Boolean,
    val canSkipToNext: Boolean,
    val progress: MediaProgress?,
    val albumArt: Bitmap? = null,
)

// Phiên nhạc chỉ báo vị trí lúc trạng thái đổi (phát, dừng, tua): vị trí đo tại updatedAtMillis (mốc SystemClock.elapsedRealtime)
// cùng tốc độ phát để nơi hiển thị tự nội suy; speed = 0 khi không đang phát.
data class MediaProgress(
    val durationMillis: Long,
    val positionMillis: Long,
    val updatedAtMillis: Long,
    val speed: Float,
)
