package com.decoutkhanqindev.aodamn.testing

import android.Manifest
import android.os.ParcelFileDescriptor
import androidx.test.platform.app.InstrumentationRegistry
import com.decoutkhanqindev.aodamn.BuildConfig
import com.decoutkhanqindev.aodamn.data.local.datastore.DataStoreManager
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.koin.core.context.GlobalContext

class ThemeDeviceRule : TestRule {
    override fun apply(base: Statement, description: Description): Statement = object : Statement() {
        override fun evaluate() {
            prepareDeviceOnce()
            base.evaluate()
        }
    }

    private fun prepareDeviceOnce() {
        if (isPrepared) return
        synchronized(lock) {
            if (isPrepared) return
            prepareDevice()
            isPrepared = true
        }
    }

    private fun prepareDevice() {
        val packageName = BuildConfig.APPLICATION_ID
        listOf(
            "settings put system time_12_24 24",
            "settings put system font_scale 1.0",
            "settings put global window_animation_scale 1.0",
            "settings put global transition_animation_scale 1.0",
            "settings put global animator_duration_scale 1.0",
            "pm grant $packageName ${Manifest.permission.POST_NOTIFICATIONS}",
            "pm grant $packageName ${Manifest.permission.READ_CALENDAR}",
            "pm grant $packageName ${Manifest.permission.ACCESS_COARSE_LOCATION}",
            "appops set $packageName SYSTEM_ALERT_WINDOW allow",
            "cmd notification allow_listener $packageName/com.decoutkhanqindev.aodamn.presentation.aod.AodNotificationListener",
        ).forEach(::shell)

        val dataStoreManager = GlobalContext.get().get<DataStoreManager>()
        dataStoreManager.saveIsFirstOpen(false)
        dataStoreManager.saveIsAodEnabled(false)
        runBlocking {
            withTimeout(5_000L) {
                dataStoreManager.isFirstOpen.filterNotNull().first { isFirstOpen -> !isFirstOpen }
                dataStoreManager.isAodEnabled.filterNotNull().first { isEnabled -> !isEnabled }
            }
        }
    }

    private fun shell(command: String) {
        val descriptor = InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand(command)
        ParcelFileDescriptor.AutoCloseInputStream(descriptor).use { stream -> stream.readBytes() }
    }

    private companion object {
        val lock = Any()

        @Volatile
        var isPrepared = false
    }
}