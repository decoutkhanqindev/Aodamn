package com.decoutkhanqindev.aodamn.presentation.aod

import android.app.PendingIntent
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.data.device.permission.PermissionManager
import com.decoutkhanqindev.aodamn.data.local.datastore.DataStoreManager
import com.decoutkhanqindev.aodamn.data.local.locale.LanguageManager
import com.decoutkhanqindev.aodamn.presentation.MainActivity
import com.decoutkhanqindev.aodamn.presentation.model.language.LanguageValue
import com.decoutkhanqindev.aodamn.presentation.model.permission.hasRequiredPermissions
import com.decoutkhanqindev.aodamn.presentation.model.permission.requiredAodPermissions
import com.decoutkhanqindev.aodamn.utils.Tag
import com.decoutkhanqindev.aodamn.utils.collectCatching
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber

class AodTileService : TileService(), Tag {

    private val dataStoreManager: DataStoreManager by inject()
    private val permissionManager: PermissionManager by inject()
    private val languageManager: LanguageManager by inject()

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private var listeningJob: Job? = null

    override fun onStartListening() {
        super.onStartListening()
        listeningJob?.cancel()
        listeningJob = scope.launch {
            dataStoreManager.isAodEnabled.filterNotNull().collectCatching(
                block = { isEnabled -> renderTile(isEnabled) },
                catch = { e -> Timber.tag(tag).e(e.stackTraceToString()) },
            )
        }
    }

    override fun onStopListening() {
        listeningJob?.cancel()
        super.onStopListening()
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    override fun onClick() {
        super.onClick()
        if (!permissionManager.requiredAodPermissions().hasRequiredPermissions) return
        val isEnabled = !(dataStoreManager.isAodEnabled.value ?: return)
        dataStoreManager.saveIsAodEnabled(isEnabled)
        renderTile(isEnabled)
        when {
            !isEnabled -> AodService.stop(this)
            !AodService.start(this) -> openApp()
        }
    }

    private fun renderTile(isEnabled: Boolean) {
        val tile = qsTile ?: return
        val isReady = permissionManager.requiredAodPermissions().hasRequiredPermissions
        tile.state = when {
            !isReady -> Tile.STATE_UNAVAILABLE
            isEnabled -> Tile.STATE_ACTIVE
            else -> Tile.STATE_INACTIVE
        }
        tile.subtitle = if (isReady) null else localizedResources().getString(R.string.tile_needs_permissions)
        tile.updateTile()
    }

    private fun localizedResources(): Resources {
        val languageCode = LanguageValue.fromCode(dataStoreManager.selectedLangCode.value).code
        return languageManager.resourcesFor(languageManager.configurationFor(languageCode))
    }

    private fun openApp() {
        val intent = Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val start = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                startActivityAndCollapse(
                    PendingIntent.getActivity(
                        this,
                        OPEN_APP_REQUEST_CODE,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE
                    ),
                )
            } else {
                @Suppress("DEPRECATION", "StartActivityAndCollapseDeprecated")
                startActivityAndCollapse(intent)
            }
        }
        if (isLocked) unlockAndRun { start() } else start()
    }

    companion object {
        private const val OPEN_APP_REQUEST_CODE = 2
    }
}
