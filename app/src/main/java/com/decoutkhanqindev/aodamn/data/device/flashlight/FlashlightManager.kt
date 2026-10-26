package com.decoutkhanqindev.aodamn.data.device.flashlight

import android.app.Application
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import com.decoutkhanqindev.aodamn.utils.Tag
import com.decoutkhanqindev.aodamn.utils.recoverCatching
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber

class FlashlightManager(
    private val app: Application,
) : Tag {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val cameraManager = app.getSystemService(CameraManager::class.java)

    private val torchCameraId: String? by lazy {
        try {
            cameraManager.cameraIdList.firstOrNull { cameraId ->
                cameraManager.getCameraCharacteristics(cameraId)
                    .get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
            }
        } catch (e: CameraAccessException) {
            Timber.tag(tag).e("Could not read the cameras: ${e.stackTraceToString()}")
            null
        }
    }

    val isAvailable: Boolean
        get() = torchCameraId != null

    val isOn: StateFlow<Boolean> = callbackFlow {
        val cameraId = torchCameraId
        val callback = object : CameraManager.TorchCallback() {
            override fun onTorchModeChanged(changedCameraId: String, enabled: Boolean) {
                if (changedCameraId == cameraId) trySend(enabled)
            }
        }
        cameraManager.registerTorchCallback(app.mainExecutor, callback)
        awaitClose { cameraManager.unregisterTorchCallback(callback) }
    }
        .distinctUntilChanged()
        .recoverCatching { throwable ->
            Timber.tag(tag).e("isOn have error: ${throwable.stackTraceToString()}")
            emit(false)
        }
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(replayExpirationMillis = 0),
            initialValue = false,
        )

    fun toggle() {
        val cameraId = torchCameraId ?: return
        try {
            cameraManager.setTorchMode(cameraId, !isOn.value)
        } catch (e: CameraAccessException) {
            Timber.tag(tag).e("Could not toggle the flashlight: ${e.stackTraceToString()}")
        } catch (e: IllegalArgumentException) {
            Timber.tag(tag).e("Could not toggle the flashlight: ${e.stackTraceToString()}")
        }
    }
}
