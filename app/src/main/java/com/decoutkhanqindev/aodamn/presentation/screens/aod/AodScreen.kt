package com.decoutkhanqindev.aodamn.presentation.screens.aod

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.aodamn.presentation.components.LocalTag
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodContent
import com.decoutkhanqindev.aodamn.presentation.effects.LaunchedWithLifecycleEffect
import com.decoutkhanqindev.aodamn.presentation.model.aod.settings.interaction.AodGestureValue
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodEffect
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.utils.collectCatching
import kotlinx.coroutines.flow.filterIsInstance
import org.koin.compose.viewmodel.koinViewModel
import timber.log.Timber

@Composable
fun AodScreen(
    onDarkChange: (Boolean) -> Unit,
    onDimChange: (Boolean) -> Unit,
    onClose: () -> Unit,
) {
    val tag = LocalTag.current
    val viewModel: AodViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedWithLifecycleEffect {
        viewModel.effect.filterIsInstance<AodEffect.CloseAod>().collectCatching(
            block = { onClose() },
            catch = { e -> Timber.tag(tag).e(e.stackTraceToString()) },
        )
    }

    SideEffect(state.isDark) {
        onDarkChange(state.isDark)
    }

    SideEffect(state.isDimmed) {
        onDimChange(state.isDimmed)
    }

    BackHandler {
        viewModel.onIntent(AodIntent.PerformGesture(AodGestureValue.BACK))
    }

    AodContent(state = state, onIntent = viewModel::onIntent)
}
