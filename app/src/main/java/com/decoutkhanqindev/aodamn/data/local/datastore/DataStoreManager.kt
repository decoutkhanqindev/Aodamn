package com.decoutkhanqindev.aodamn.data.local.datastore

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.decoutkhanqindev.aodamn.data.local.datastore.model.AodAppearancePrefs
import com.decoutkhanqindev.aodamn.data.local.datastore.model.AodExtrasPrefs
import com.decoutkhanqindev.aodamn.data.local.datastore.model.AodInteractionPrefs
import com.decoutkhanqindev.aodamn.data.local.datastore.model.AodNotificationOptionsPrefs
import com.decoutkhanqindev.aodamn.data.local.datastore.model.AodOptionsPrefs
import com.decoutkhanqindev.aodamn.data.local.datastore.model.AodRulesPrefs
import com.decoutkhanqindev.aodamn.data.local.datastore.model.AppShellPrefs
import com.decoutkhanqindev.aodamn.data.local.datastore.model.WeatherCachePrefs
import com.decoutkhanqindev.aodamn.utils.Tag
import com.decoutkhanqindev.aodamn.utils.recoverCatching
import com.decoutkhanqindev.aodamn.utils.withContextCatching
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import timber.log.Timber

class DataStoreManager(
    private val app: Application,
) : Tag {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val Context.prefs: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)

    private val appShell = JsonPrefs(APP_SHELL_KEY, AppShellPrefs.serializer(), AppShellPrefs.DEFAULT)
    private val aodOptionsPrefs = JsonPrefs(AOD_OPTIONS_KEY, AodOptionsPrefs.serializer(), AodOptionsPrefs.DEFAULT)
    private val aodRulesPrefs = JsonPrefs(AOD_RULES_KEY, AodRulesPrefs.serializer(), AodRulesPrefs.DEFAULT)
    private val aodNotificationOptionsPrefs = JsonPrefs(
        AOD_NOTIFICATION_OPTIONS_KEY,
        AodNotificationOptionsPrefs.serializer(),
        AodNotificationOptionsPrefs.DEFAULT,
    )
    private val aodInteractionPrefs =
        JsonPrefs(AOD_INTERACTION_KEY, AodInteractionPrefs.serializer(), AodInteractionPrefs.DEFAULT)
    private val aodExtrasPrefs = JsonPrefs(AOD_EXTRAS_KEY, AodExtrasPrefs.serializer(), AodExtrasPrefs.DEFAULT)
    private val aodAppearancePrefs =
        JsonPrefs(AOD_APPEARANCE_KEY, AodAppearancePrefs.serializer(), AodAppearancePrefs.DEFAULT)
    private val weatherCachePrefs = JsonPrefs(WEATHER_CACHE_KEY, WeatherCachePrefs.serializer(), WeatherCachePrefs())

    val selectedLangCode: StateFlow<String?> = appShell.field { it.selectedLangCode }
    val isFirstOpen: StateFlow<Boolean?> = appShell.field { it.isFirstOpen }
    val isCustomizeGuideShown: StateFlow<Boolean?> = appShell.field { it.isCustomizeGuideShown }
    val aodLastWake: StateFlow<Int?> = appShell.field { it.lastWakeCode }

    val aodOptions: StateFlow<AodOptionsPrefs?> = aodOptionsPrefs.flow
    val aodRules: StateFlow<AodRulesPrefs?> = aodRulesPrefs.flow
    val aodNotificationOptions: StateFlow<AodNotificationOptionsPrefs?> = aodNotificationOptionsPrefs.flow
    val aodInteraction: StateFlow<AodInteractionPrefs?> = aodInteractionPrefs.flow
    val aodExtras: StateFlow<AodExtrasPrefs?> = aodExtrasPrefs.flow
    val aodAppearance: StateFlow<AodAppearancePrefs?> = aodAppearancePrefs.flow
    val weatherCache: StateFlow<WeatherCachePrefs?> = weatherCachePrefs.flow

    fun saveSelectedLangCode(value: String): Job = appShell.update { it.copy(selectedLangCode = value) }

    fun saveIsFirstOpen(value: Boolean): Job = appShell.update { it.copy(isFirstOpen = value) }

    fun saveIsCustomizeGuideShown(value: Boolean): Job = appShell.update { it.copy(isCustomizeGuideShown = value) }

    fun saveAodLastWake(value: Int): Job = appShell.update { it.copy(lastWakeCode = value) }

    fun updateAodOptions(transform: (AodOptionsPrefs) -> AodOptionsPrefs): Job = aodOptionsPrefs.update(transform)

    fun updateAodRules(transform: (AodRulesPrefs) -> AodRulesPrefs): Job = aodRulesPrefs.update(transform)

    fun updateAodNotificationOptions(transform: (AodNotificationOptionsPrefs) -> AodNotificationOptionsPrefs): Job =
        aodNotificationOptionsPrefs.update(transform)

    fun updateAodInteraction(transform: (AodInteractionPrefs) -> AodInteractionPrefs): Job =
        aodInteractionPrefs.update(transform)

    fun updateAodExtras(transform: (AodExtrasPrefs) -> AodExtrasPrefs): Job = aodExtrasPrefs.update(transform)

    fun updateAodAppearance(transform: (AodAppearancePrefs) -> AodAppearancePrefs): Job =
        aodAppearancePrefs.update(transform)

    fun saveWeather(temperatureCelsius: Double, code: Int, isDay: Boolean, updatedAtMillis: Long): Job =
        weatherCachePrefs.update {
            WeatherCachePrefs(
                temperatureCelsius = temperatureCelsius,
                weatherCode = code,
                isDay = isDay,
                updatedAtMillis = updatedAtMillis,
            )
        }

    private fun edit(transform: (MutablePreferences) -> Unit): Job =
        scope.launch {
            withContextCatching(
                block = { app.prefs.edit(transform) },
                catch = { throwable ->
                    Timber.tag(tag).e("DataStore edit failed: ${throwable.stackTraceToString()}")
                },
            )
        }

    private inner class JsonPrefs<P>(
        name: String,
        private val serializer: KSerializer<P>,
        private val default: P,
    ) {
        private val key: Preferences.Key<String> = stringPreferencesKey(name)

        val flow: StateFlow<P?> = field { it }

        fun <R> field(select: (P) -> R): StateFlow<R?> = app.prefs.data
            .map { prefs -> select(read(prefs)) }
            .recoverCatching { throwable ->
                Timber.tag(tag).e("DataStore read ${key.name} failed, falling back to defaults: ${throwable.stackTraceToString()}")
                emit(select(default))
            }
            .stateIn(scope = scope, started = SharingStarted.Eagerly, initialValue = null)

        fun update(transform: (P) -> P): Job = edit { prefs ->
            prefs[key] = PrefsJson.encodeToString(serializer, transform(read(prefs)))
        }

        private fun read(prefs: Preferences): P {
            val raw = prefs[key] ?: return default
            return PrefsJson.decodeOrNull(serializer, raw) ?: default.also {
                Timber.tag(tag).w("DataStore ${key.name} could not be decoded, using defaults")
            }
        }
    }

    companion object {
        private const val DATA_STORE_NAME = "aodamn_prefs"
        private const val APP_SHELL_KEY = "app_shell"
        private const val AOD_OPTIONS_KEY = "aod_options"
        private const val AOD_RULES_KEY = "aod_rules"
        private const val AOD_NOTIFICATION_OPTIONS_KEY = "aod_notification_options"
        private const val AOD_INTERACTION_KEY = "aod_interaction"
        private const val AOD_EXTRAS_KEY = "aod_extras"
        private const val AOD_APPEARANCE_KEY = "aod_appearance"
        private const val WEATHER_CACHE_KEY = "weather_cache"
    }
}
