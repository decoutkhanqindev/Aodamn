package com.decoutkhanqindev.aodamn.data.device.pickup

import android.app.Application
import android.hardware.Sensor
import android.hardware.SensorManager
import android.hardware.TriggerEvent
import android.hardware.TriggerEventListener
import com.decoutkhanqindev.aodamn.utils.Tag
import com.decoutkhanqindev.aodamn.utils.recoverCatching
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber

class PickupGestureManager(
    private val app: Application,
) : Tag {
    private val sensorManager = app.getSystemService(SensorManager::class.java)

    private val pickupSensor: Sensor? =
        sensorManager.getSensorList(Sensor.TYPE_ALL).firstOrNull { sensor ->
            sensor.stringType == PICK_UP_GESTURE_TYPE &&
                    sensor.isWakeUpSensor &&
                    sensor.reportingMode == Sensor.REPORTING_MODE_ONE_SHOT
        }

    val isSupported: Boolean
        get() = pickupSensor != null

    val pickups: Flow<Unit> = callbackFlow {
        val sensor = pickupSensor
        val listener = object : TriggerEventListener() {
            override fun onTrigger(event: TriggerEvent?) {
                trySend(Unit)
            }
        }
        if (sensor == null || !sensorManager.requestTriggerSensor(listener, sensor)) {
            close()
            return@callbackFlow
        }
        awaitClose { sensorManager.cancelTriggerSensor(listener, sensor) }
    }
        .recoverCatching { throwable ->
            Timber.tag(tag).e("pickups have error: ${throwable.stackTraceToString()}")
        }

    companion object {
        private const val PICK_UP_GESTURE_TYPE = "android.sensor.pick_up_gesture"
    }
}
