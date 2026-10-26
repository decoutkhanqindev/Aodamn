package com.decoutkhanqindev.aodamn.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.decoutkhanqindev.aodamn.presentation.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.lang.ref.WeakReference

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Context.packageUri(): Uri = "package:$packageName".toUri()

fun Context.playStoreUrl(): String = "https://play.google.com/store/apps/details?id=$packageName"

fun Context.shareText(text: String, chooserTitle: String) {
    val intent = Intent(Intent.ACTION_SEND)
        .setType("text/plain")
        .putExtra(Intent.EXTRA_TEXT, text)
    try {
        startActivity(Intent.createChooser(intent, chooserTitle))
    } catch (e: ActivityNotFoundException) {
        return
    }
}

fun Context.openSettingsPage(vararg intents: Intent): Boolean {
    for (intent in intents) {
        try {
            startSettingsPage(intent)
            return true
        } catch (e: Exception) {
            continue
        }
    }

    return try {
        startSettingsPage(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageUri()))
        false
    } catch (e: Exception) {
        false
    }
}

private fun Context.startSettingsPage(intent: Intent) {
    val activity: Activity? = findActivity()
    if (activity != null) {
        activity.startActivityForResult(intent, SETTINGS_REQUEST_CODE)
    } else {
        startActivity(intent)
    }
}

private fun Context.openPermissionPage(isGranted: () -> Boolean, open: () -> Boolean) {
    val wasGranted = isGranted()
    if (open() && !wasGranted) returnAppWhen(condition = isGranted)
}

fun Context.openOverlaySettings(isGranted: () -> Boolean) {
    openPermissionPage(isGranted) {
        openSettingsPage(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, packageUri()))
    }
}

fun Context.openAppSettings(isGranted: () -> Boolean) {
    openPermissionPage(isGranted) {
        openSettingsPage(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageUri()))
    }
}

fun Context.openNotificationSettings(isGranted: () -> Boolean) {
    openPermissionPage(isGranted) {
        openSettingsPage(
            Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).putExtra(
                Settings.EXTRA_APP_PACKAGE,
                packageName
            ),
        )
    }
}

fun Context.openNotificationListenerSettings(listener: ComponentName, isGranted: () -> Boolean) {
    openPermissionPage(isGranted) {
        try {
            startSettingsPage(
                Intent(Settings.ACTION_NOTIFICATION_LISTENER_DETAIL_SETTINGS)
                    .putExtra(
                        Settings.EXTRA_NOTIFICATION_LISTENER_COMPONENT_NAME,
                        listener.flattenToString()
                    ),
            )
            true
        } catch (e: ActivityNotFoundException) {
            openSettingsPage(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }
    }
}

fun Context.openMiuiPermissionSettings(isGranted: () -> Boolean) {
    openPermissionPage(isGranted) {
        openSettingsPage(
            Intent(MIUI_PERMISSION_EDITOR_ACTION)
                .setClassName(MIUI_SECURITY_CENTER_PACKAGE, MIUI_PERMISSION_EDITOR_ACTIVITY)
                .putExtra(MIUI_EXTRA_PACKAGE_NAME, packageName),
        )
    }
}

fun Context.openWifiSettings(isConnected: () -> Boolean) {
    val opened = openSettingsPage(
        Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY),
        Intent(Settings.ACTION_WIFI_SETTINGS),
        Intent(Settings.ACTION_WIRELESS_SETTINGS),
        Intent(Settings.ACTION_SETTINGS)
    )

    if (opened) returnAppWhen { isConnected() }
}

@SuppressLint("UnspecifiedRegisterReceiverFlag")
fun Context.registerSystemReceiver(receiver: BroadcastReceiver?, filter: IntentFilter): Intent? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED)
    } else {
        registerReceiver(receiver, filter)
    }

fun Context.returnAppWhen(
    timeoutMs: Long = DEFAULT_TIMEOUT_MS,
    condition: () -> Boolean,
) {
    val appContext = this.applicationContext
    val activity = WeakReference<Activity>(findActivity())
    val process = ProcessLifecycleOwner.get()
    process.lifecycleScope.launch {
        withContextCatching(
            context = Dispatchers.Main,
            block = {
                withTimeoutOrNull(timeoutMs) {
                    delay(FIRST_POLL_DELAY_MS)
                    while (!condition()) {
                        if (process.isInFront()) return@withTimeoutOrNull
                        delay(POLL_INTERVAL_MS)
                    }

                    activity.get()?.finishActivity(SETTINGS_REQUEST_CODE)
                    delay(RETURN_CHECK_MS)
                    while (!process.isInFront()) {
                        appContext.startActivity(
                            Intent(appContext, MainActivity::class.java).addFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP,
                            ),
                        )
                        delay(RETURN_RETRY_MS)
                    }
                }
            },
            catch = {}
        )
    }
}

private fun LifecycleOwner.isInFront(): Boolean = lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)

fun Context.findActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

private const val POLL_INTERVAL_MS = 200L
private const val FIRST_POLL_DELAY_MS = 1_000L
private const val DEFAULT_TIMEOUT_MS = 60_000L
private const val RETURN_CHECK_MS = 400L
private const val RETURN_RETRY_MS = 500L

private const val SETTINGS_REQUEST_CODE = 0x0AD0

private const val MIUI_PERMISSION_EDITOR_ACTION = "miui.intent.action.APP_PERM_EDITOR"
private const val MIUI_SECURITY_CENTER_PACKAGE = "com.miui.securitycenter"
private const val MIUI_PERMISSION_EDITOR_ACTIVITY =
    "com.miui.permcenter.permissions.PermissionsEditorActivity"
private const val MIUI_EXTRA_PACKAGE_NAME = "extra_pkgname"
