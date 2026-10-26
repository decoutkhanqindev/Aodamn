package com.decoutkhanqindev.aodamn.data.device.notification

import android.graphics.Bitmap
import androidx.annotation.ColorInt

data class ActiveNotification(
    val key: String,
    val packageName: String,
    val icon: Bitmap,
    @param:ColorInt val color: Int,
    val postTime: Long,
    val content: NotificationContent?,
)
