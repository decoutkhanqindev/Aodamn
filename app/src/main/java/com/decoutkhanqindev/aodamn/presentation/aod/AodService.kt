package com.decoutkhanqindev.aodamn.presentation.aod

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.data.device.audio.AudioStateManager
import com.decoutkhanqindev.aodamn.data.device.battery.BatteryStateManager
import com.decoutkhanqindev.aodamn.data.device.pickup.PickupGestureManager
import com.decoutkhanqindev.aodamn.data.device.screen.ScreenStateManager
import com.decoutkhanqindev.aodamn.data.local.datastore.DataStoreManager
import com.decoutkhanqindev.aodamn.data.local.locale.LanguageManager
import com.decoutkhanqindev.aodamn.presentation.MainActivity
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.WakeResultValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.settings.rules.AodScheduleUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.settings.rules.currentAodRules
import com.decoutkhanqindev.aodamn.presentation.model.language.LanguageValue
import com.decoutkhanqindev.aodamn.utils.Tag
import com.decoutkhanqindev.aodamn.utils.collectCatching
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber

class AodService : Service(), Tag {

    private val dataStoreManager: DataStoreManager by inject()
    private val languageManager: LanguageManager by inject()
    private val screenStateManager: ScreenStateManager by inject()
    private val audioStateManager: AudioStateManager by inject()
    private val batteryStateManager: BatteryStateManager by inject()
    private val pickupGestureManager: PickupGestureManager by inject()
    private val session: AodSession by inject()

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private var launchCheckJob: Job? = null
    private var raiseToWakeJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        scope.launch {
            screenStateManager.events.collectCatching(
                block = { event ->
                    when (event) {
                        Intent.ACTION_SCREEN_OFF -> onScreenOff()
                        Intent.ACTION_USER_PRESENT -> onUserPresent()
                    }
                },
                catch = { e -> Timber.tag(tag).e(e.stackTraceToString()) },
            )
        }
        scope.launch {
            dataStoreManager.selectedLangCode
                .filterNotNull()
                .distinctUntilChanged()
                .collectCatching(
                    block = { startInForeground() },
                    catch = { e -> Timber.tag(tag).e(e.stackTraceToString()) },
                )
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startInForeground()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        scope.cancel()
        session.finish()
        super.onDestroy()
    }

    private fun onScreenOff() {
        launchCheckJob?.cancel()
        raiseToWakeJob?.cancel()
        val wasShowing = session.isShowing
        val wasCovered = session.isCovered
        val isSleepRequested = session.consumeSleepRequest()
        session.finish()
        when {
            isSleepRequested -> if (!wasCovered) armRaiseToWake()
            wasCovered -> Unit
            wasShowing -> screenStateManager.wakeUp(holdMillis = WAKE_HOLD_MILLIS)
            shouldEnter() -> launchAod()
        }
    }

    private fun onUserPresent() {
        session.clearSleepRequest()
        if (!session.isShowing) return

        val isJustShown =
            SystemClock.elapsedRealtime() - session.shownAt < USER_PRESENT_GRACE_MILLIS
        if (isJustShown && !screenStateManager.isDeviceSecure) return

        session.finish()
    }

    private fun shouldEnter(): Boolean {
        if (dataStoreManager.isAodEnabled.value != true) return false
        if (screenStateManager.isInteractive) return false
        if (audioStateManager.isBusyNow()) return false

        return dataStoreManager.currentAodRules().allows(
            batteryPercent = batteryStateManager.readLevelPercent(),
            isCharging = batteryStateManager.readIsCharging() == true,
            isPlugged = batteryStateManager.readIsPlugged(),
            minuteOfDay = AodScheduleUiModel.minuteOfDay(System.currentTimeMillis()),
        )
    }

    private fun launchAod() {
        val intent = Intent(this, AodActivity::class.java).addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NO_ANIMATION or
                    Intent.FLAG_ACTIVITY_NO_USER_ACTION,
        )
        try {
            startActivity(intent)
        } catch (e: RuntimeException) {
            Timber.tag(tag).e("Could not start the AOD: ${e.stackTraceToString()}")
        }
        launchCheckJob?.cancel()
        launchCheckJob = scope.launch {
            delay(LAUNCH_CHECK_MILLIS)
            checkLaunch()
        }
    }

    private fun armRaiseToWake() {
        val isEnabled = dataStoreManager.isAodRaiseToWakeEnabled.value
            ?: DataStoreManager.DEFAULT_IS_AOD_RAISE_TO_WAKE_ENABLED
        if (!isEnabled || !pickupGestureManager.isSupported) return
        raiseToWakeJob = scope.launch {
            pickupGestureManager.pickups.firstOrNull() ?: return@launch
            if (shouldEnter()) launchAod()
        }
    }

    private fun checkLaunch() {
        if (session.isShowing && screenStateManager.isInteractive) {
            dataStoreManager.saveAodLastWake(WakeResultValue.OK.code)
        } else {
            dataStoreManager.saveAodLastWake(WakeResultValue.FAILED.code)
            session.finish()
        }
    }

    private fun startInForeground() {
        val languageCode = LanguageValue.fromCode(dataStoreManager.selectedLangCode.value).code
        val localizedResources =
            languageManager.resourcesFor(languageManager.configurationFor(languageCode))
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(
            NotificationChannel(
                CHANNEL_ID,
                localizedResources.getString(R.string.channel_service),
                NotificationManager.IMPORTANCE_LOW,
            ).apply { setShowBadge(false) },
        )
        val openApp = PendingIntent.getActivity(
            this,
            OPEN_APP_REQUEST_CODE,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE,
        )
        val notification = Notification.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_aod)
            .setContentTitle(localizedResources.getString(R.string.notif_service_title))
            .setContentIntent(openApp)
            .setOngoing(true)
            .build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
            )
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    companion object {
        private const val CHANNEL_ID = "aod_service"
        private const val NOTIFICATION_ID = 1
        private const val OPEN_APP_REQUEST_CODE = 1
        private const val LAUNCH_CHECK_MILLIS = 2_000L
        private const val WAKE_HOLD_MILLIS = 1_000L
        private const val USER_PRESENT_GRACE_MILLIS = 1_500L

        fun start(context: Context): Boolean = try {
            context.startForegroundService(Intent(context, AodService::class.java))
            true
        } catch (e: IllegalStateException) {
            Timber.tag(AodService::class.java.simpleName)
                .e("Could not start the AOD service: ${e.stackTraceToString()}")
            false
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, AodService::class.java))
        }
    }
}
