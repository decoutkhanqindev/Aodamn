package com.decoutkhanqindev.aodamn.presentation.screens.splash

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.decoutkhanqindev.aodamn.ads.AdsManager
import com.decoutkhanqindev.aodamn.ads.ad_unit.AdUnitState
import com.decoutkhanqindev.aodamn.ads.composables.BannerAdView
import com.decoutkhanqindev.aodamn.data.local.datastore.DataStoreManager
import com.decoutkhanqindev.aodamn.data.network.connectivity.NetworkManager
import com.decoutkhanqindev.aodamn.presentation.navigation.MainDestination
import com.decoutkhanqindev.aodamn.presentation.navigation.OnboardingDestination
import com.decoutkhanqindev.aodamn.utils.navigateTo
import org.koin.compose.koinInject

@Composable
fun SplashScreen(backStack: NavBackStack<NavKey>) {
    val context = LocalContext.current
    val activity = LocalActivity.current
    val dataStoreManager: DataStoreManager = koinInject()
    val networkManager: NetworkManager = koinInject()
    val adsManager: AdsManager = koinInject()
    val isNetworkAvailable by networkManager.isAvailable.collectAsStateWithLifecycle()
    val isConsentGathered by adsManager.isConsentGathered.collectAsStateWithLifecycle()
    val interSplash = adsManager.interSplash
    val interSplashState by interSplash.state.collectAsStateWithLifecycle()
    val handleNext = {
        if (dataStoreManager.isFirstOpen.value == true) {
            backStack.navigateTo(OnboardingDestination, preserveState = false)
        } else {
            backStack.navigateTo(MainDestination(isPermissionSkipped = false), preserveState = false)
        }
    }

    SideEffect(isConsentGathered, isNetworkAvailable) {
        when {
            !isConsentGathered || !isNetworkAvailable -> Unit
            !adsManager.canRequestAds -> handleNext()
            else -> interSplash.load(context)
        }
    }

    LifecycleResumeEffect(interSplashState, isNetworkAvailable) {
        if (isNetworkAvailable) {
            when (interSplashState) {
                AdUnitState.LOADED -> activity?.let {
                    interSplash.show(
                        activity = it,
                        onAdShowed = handleNext,
                        onAdFailedToShow = handleNext,
                    )
                } ?: handleNext()

                AdUnitState.FAILED -> handleNext()

                else -> Unit
            }
        }

        onPauseOrDispose { }
    }

    BackHandler { }

    SplashContent(
        bannerAd = { if (isConsentGathered) BannerAdView(adUnit = { adsManager.bannerSplash }) },
    )
}
