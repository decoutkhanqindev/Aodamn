package com.decoutkhanqindev.aodamn.di

import com.decoutkhanqindev.aodamn.ads.AdsManager
import com.decoutkhanqindev.aodamn.data.device.audio.AudioStateManager
import com.decoutkhanqindev.aodamn.data.device.battery.BatteryStateManager
import com.decoutkhanqindev.aodamn.data.device.flashlight.FlashlightManager
import com.decoutkhanqindev.aodamn.data.device.light.AmbientLightManager
import com.decoutkhanqindev.aodamn.data.device.location.DeviceLocationManager
import com.decoutkhanqindev.aodamn.data.device.media.MediaStateManager
import com.decoutkhanqindev.aodamn.data.device.notification.NotificationStateManager
import com.decoutkhanqindev.aodamn.data.device.permission.PermissionManager
import com.decoutkhanqindev.aodamn.data.device.pickup.PickupGestureManager
import com.decoutkhanqindev.aodamn.data.device.proximity.ProximityManager
import com.decoutkhanqindev.aodamn.data.device.screen.ScreenStateManager
import com.decoutkhanqindev.aodamn.data.local.datastore.DataStoreManager
import com.decoutkhanqindev.aodamn.data.local.image.AodImageManager
import com.decoutkhanqindev.aodamn.data.local.locale.LanguageManager
import com.decoutkhanqindev.aodamn.data.network.api.OpenMeteoApiService
import com.decoutkhanqindev.aodamn.data.network.connectivity.NetworkManager
import com.decoutkhanqindev.aodamn.data.repository.CalendarRepositoryImpl
import com.decoutkhanqindev.aodamn.data.repository.WeatherRepositoryImpl
import com.decoutkhanqindev.aodamn.domain.repository.CalendarRepository
import com.decoutkhanqindev.aodamn.domain.repository.WeatherRepository
import com.decoutkhanqindev.aodamn.domain.usecase.GetUpcomingEventsUseCase
import com.decoutkhanqindev.aodamn.domain.usecase.ObserveWeatherUseCase
import com.decoutkhanqindev.aodamn.domain.usecase.RefreshWeatherUseCase
import com.decoutkhanqindev.aodamn.presentation.aod.AodSession
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.screens.aod.AodViewModel
import com.decoutkhanqindev.aodamn.presentation.screens.aodtab.AodTabViewModel
import com.decoutkhanqindev.aodamn.presentation.screens.customize.CustomizeViewModel
import com.decoutkhanqindev.aodamn.presentation.screens.language.LanguageViewModel
import com.decoutkhanqindev.aodamn.presentation.screens.permission.PermissionViewModel
import com.decoutkhanqindev.aodamn.presentation.screens.settings.SettingsViewModel
import com.decoutkhanqindev.aodamn.presentation.screens.themeeditor.ThemeEditorViewModel
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

val managerModule = module {
    single { DataStoreManager(androidApplication()) }
    single { LanguageManager(androidApplication()) }
    single { NetworkManager(androidApplication()) }
    single { PermissionManager(androidApplication()) }
    single { ScreenStateManager(androidApplication()) }
    single { BatteryStateManager(androidApplication()) }
    single { AudioStateManager(androidApplication()) }
    single { ProximityManager(androidApplication()) }
    single { NotificationStateManager(androidApplication()) }
    single { MediaStateManager(androidApplication(), get()) }
    single { AodImageManager(androidApplication()) }
    single { FlashlightManager(androidApplication()) }
    single { AmbientLightManager(androidApplication()) }
    single { PickupGestureManager(androidApplication()) }
    single { DeviceLocationManager(androidApplication()) }
}

val networkModule = module {
    // Open-Meteo trả nhiều trường hơn app dùng: bỏ qua trường lạ thay vì lỗi khi đọc.
    single { Json { ignoreUnknownKeys = true } }
    single {
        Retrofit.Builder()
            .baseUrl(OpenMeteoApiService.BASE_URL)
            .addConverterFactory(get<Json>().asConverterFactory("application/json".toMediaType()))
            .build()
    }
    single { get<Retrofit>().create(OpenMeteoApiService::class.java) }
}

val adsModule = module {
    single { AdsManager() }
}

val aodModule = module {
    single { AodSession(get()) }
}

val repositoryModule = module {
    single<WeatherRepository> { WeatherRepositoryImpl(get(), get(), get()) }
    single<CalendarRepository> { CalendarRepositoryImpl(androidApplication()) }
}

val useCaseModule = module {
    factory { ObserveWeatherUseCase(get()) }
    factory { RefreshWeatherUseCase(get()) }
    factory { GetUpcomingEventsUseCase(get()) }
}

val viewModelModule = module {
    viewModel { (isPermissionSkipped: Boolean) -> AodTabViewModel(isPermissionSkipped, get(), get(), get()) }
    viewModel { SettingsViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { (isFirstOpen: Boolean) -> LanguageViewModel(isFirstOpen, get(), get()) }
    viewModel { (isFirstOpen: Boolean) ->
        CustomizeViewModel(
            isFirstOpen,
            get(),
            get(),
            get(),
            get()
        )
    }
    viewModel { PermissionViewModel(get(), get()) }
    viewModel { (themeId: String, focusBlock: AodBlockValue?) -> ThemeEditorViewModel(themeId, focusBlock, get(), get()) }
    viewModel {
        AodViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
}

val appModules = listOf(
    managerModule,
    networkModule,
    adsModule,
    aodModule,
    repositoryModule,
    useCaseModule,
    viewModelModule,
)
