package com.decoutkhanqindev.aodamn.presentation.components.aod.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameMillis

@Composable
fun rememberLiveClockMillis(isLive: Boolean): MutableLongState {
    val clockMillis = remember { mutableLongStateOf(System.currentTimeMillis()) }

    if (isLive) {
        LaunchedEffect(Unit) {
            while (true) {
                withFrameMillis { clockMillis.longValue = System.currentTimeMillis() }
            }
        }
    }

    return clockMillis
}
