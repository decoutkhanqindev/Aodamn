package com.decoutkhanqindev.aodamn.presentation.aod

import android.os.SystemClock
import com.decoutkhanqindev.aodamn.data.device.proximity.ProximityManager
import java.lang.ref.WeakReference

class AodSession(
    private val proximityManager: ProximityManager,
) {
    private var current: WeakReference<AodActivity>? = null
    private var isSleepRequested = false

    var shownAt: Long = 0L
        private set

    val isShowing: Boolean
        get() = current?.get()?.isFinishing == false

    val isCovered: Boolean
        get() = isShowing && proximityManager.isNear.value

    fun attach(activity: AodActivity) {
        current = WeakReference(activity)
        shownAt = SystemClock.elapsedRealtime()
    }

    fun detach(activity: AodActivity) {
        if (current?.get() === activity) current = null
    }

    fun finish() {
        current?.get()?.close()
        current = null
    }

    fun requestSleep() {
        isSleepRequested = true
    }

    fun clearSleepRequest() {
        isSleepRequested = false
    }

    fun consumeSleepRequest(): Boolean = isSleepRequested.also { isSleepRequested = false }
}
