package com.decoutkhanqindev.aodamn.data.device.motion

import android.app.Application
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.decoutkhanqindev.aodamn.utils.Tag
import com.decoutkhanqindev.aodamn.utils.recoverCatching
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import kotlin.math.sqrt

class MotionManager(app: Application) : Tag {
    private val sensorManager = app.getSystemService(SensorManager::class.java)
    private val motionSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)

    val motions: Flow<Unit> = callbackFlow {
        val sensor = motionSensor
        if (sensor == null) {
            close()
            return@callbackFlow
        }
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val (x, y, z) = event.values
                if (sqrt(x * x + y * y + z * z) > MOTION_THRESHOLD) trySend(Unit)
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        awaitClose { sensorManager.unregisterListener(listener) }
    }
        .recoverCatching { throwable ->
            Timber.tag(tag).e("motions have error: ${throwable.stackTraceToString()}")
        }

    companion object {
        private const val MOTION_THRESHOLD = 0.5f
    }
}
