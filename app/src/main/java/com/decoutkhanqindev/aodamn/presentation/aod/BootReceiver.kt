package com.decoutkhanqindev.aodamn.presentation.aod

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.decoutkhanqindev.aodamn.data.local.datastore.DataStoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BootReceiver : BroadcastReceiver(), KoinComponent {

    private val dataStoreManager: DataStoreManager by inject()

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED && intent.action != Intent.ACTION_MY_PACKAGE_REPLACED) {
            return
        }
        val pendingResult = goAsync()
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            try {
                if (dataStoreManager.isAodEnabled.filterNotNull().first()) AodService.start(context)
            } finally {
                pendingResult.finish()
            }
        }
    }
}
