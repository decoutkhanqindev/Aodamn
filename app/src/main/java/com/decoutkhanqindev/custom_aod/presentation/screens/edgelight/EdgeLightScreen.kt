package com.decoutkhanqindev.custom_aod.presentation.screens.edgelight

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.LocalTag
import com.decoutkhanqindev.custom_aod.presentation.effects.LaunchedWithLifecycleEffect
import com.decoutkhanqindev.custom_aod.presentation.navigation.IapDestination
import com.decoutkhanqindev.custom_aod.presentation.screens.edgelight.state.EdgeLightEffect
import com.decoutkhanqindev.custom_aod.utils.collectCatching
import com.decoutkhanqindev.custom_aod.utils.playStoreUrl
import com.decoutkhanqindev.custom_aod.utils.shareText
import org.koin.compose.viewmodel.koinViewModel
import timber.log.Timber

@Composable
fun EdgeLightScreen(backStack: NavBackStack<NavKey>) {
    val context = LocalContext.current
    val resources = LocalResources.current
    val tag = LocalTag.current
    val viewModel: EdgeLightViewModel = koinViewModel()

    LaunchedWithLifecycleEffect {
        viewModel.effect.collectCatching(
            block = { effect ->
                when (effect) {
                    is EdgeLightEffect.NavigateToIap -> backStack.add(IapDestination)
                    is EdgeLightEffect.ShareApp -> context.shareText(
                        text = resources.getString(R.string.share_app_text, context.playStoreUrl()),
                        chooserTitle = resources.getString(R.string.share_app),
                    )
                }
            },
            catch = { e -> Timber.tag(tag).e(e.stackTraceToString()) },
        )
    }

    EdgeLightContent(onIntent = viewModel::onIntent)
}
