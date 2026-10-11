package com.decoutkhanqindev.custom_aod.presentation.screens.themeeditor

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.decoutkhanqindev.custom_aod.presentation.components.LocalTag
import com.decoutkhanqindev.custom_aod.presentation.effects.LaunchedWithLifecycleEffect
import com.decoutkhanqindev.custom_aod.presentation.navigation.CustomizeDestination
import com.decoutkhanqindev.custom_aod.presentation.screens.themeeditor.state.ThemeEditorEffect
import com.decoutkhanqindev.custom_aod.presentation.screens.themeeditor.state.ThemeEditorIntent
import com.decoutkhanqindev.custom_aod.utils.collectCatching
import com.decoutkhanqindev.custom_aod.utils.navigateBack
import com.decoutkhanqindev.custom_aod.utils.showToast
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

@Composable
fun ThemeEditorScreen(
    backStack: NavBackStack<NavKey>,
    themeId: String,
) {
    val context = LocalContext.current
    val resources = LocalResources.current
    val tag = LocalTag.current
    val viewModel: ThemeEditorViewModel = koinViewModel { parametersOf(themeId) }
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedWithLifecycleEffect {
        viewModel.effect.collectCatching(
            block = { effect ->
                when (effect) {
                    is ThemeEditorEffect.ShowMessage -> context.showToast(resources.getString(effect.messageRes))
                    is ThemeEditorEffect.NavigateToCustomize -> backStack.add(CustomizeDestination(isFirstOpen = false))
                    is ThemeEditorEffect.NavigateBack -> backStack.navigateBack()
                }
            },
            catch = { e -> Timber.tag(tag).e(e.stackTraceToString()) },
        )
    }

    LifecycleResumeEffect(Unit) {
        viewModel.onIntent(ThemeEditorIntent.RefreshBase)
        onPauseOrDispose { }
    }

    // Đang sửa thì Back thoát chế độ sửa (hỏi trước nếu còn thay đổi chưa lưu), không rời màn.
    BackHandler {
        viewModel.onIntent(ThemeEditorIntent.NavigateBack)
    }

    ThemeEditorContent(state = state, onIntent = viewModel::onIntent)
}
