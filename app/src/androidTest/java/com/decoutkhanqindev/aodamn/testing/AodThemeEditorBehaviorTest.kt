package com.decoutkhanqindev.aodamn.testing

import com.decoutkhanqindev.aodamn.data.device.permission.PermissionManager
import com.decoutkhanqindev.aodamn.data.local.datastore.DataStoreManager
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.isDefault
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.isSame
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.options
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.observeAodThemeConfigs
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.saveAodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.screens.themeeditor.ThemeEditorViewModel
import com.decoutkhanqindev.aodamn.presentation.screens.themeeditor.state.ThemeEditorIntent
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.koin.core.context.GlobalContext

@RunWith(Parameterized::class)
class AodThemeEditorBehaviorTest(
    private val expectation: ThemeExpectation,
) {
    @get:Rule
    val deviceRule = ThemeDeviceRule()

    @Test
    fun changesResetsAppliesAndReopensSavedConfig() = runBlocking {
        val dataStoreManager = GlobalContext.get().get<DataStoreManager>()
        val permissionManager = GlobalContext.get().get<PermissionManager>()
        dataStoreManager.saveAodThemeConfig(expectation.theme, AodThemeConfig()).join()
        dataStoreManager.saveAodThemeId(AodThemeValue.DEFAULT.id).join()
        withTimeout(10_000L) {
            dataStoreManager.aodThemeId.filterNotNull().first { id -> id == AodThemeValue.DEFAULT.id }
            dataStoreManager.observeAodThemeConfigs().first { configs ->
                expectation.theme.spec.isDefault(configs.getValue(expectation.theme))
            }
        }

        val viewModel = ThemeEditorViewModel(
            themeId = expectation.theme.id,
            focusBlock = null,
            dataStoreManager = dataStoreManager,
            permissionManager = permissionManager,
        )
        awaitLoaded(viewModel)
        viewModel.onIntent(ThemeEditorIntent.Edit.StartEditing)
        val firstOption = expectation.theme.spec.options.first()
        viewModel.onIntent(ThemeEditorIntent.Option.ChangeOption(ThemeOptionCases.changed(firstOption)))
        assertFalse(firstOption.isDefault(viewModel.state.value.config))
        assertTrue(viewModel.state.value.history.canUndo)

        viewModel.onIntent(ThemeEditorIntent.History.ResetTheme)
        assertTrue(expectation.theme.spec.isDefault(viewModel.state.value.config))
        assertFalse(viewModel.state.value.history.isResetConfirmVisible)

        val changed = ThemeOptionCases.maximum(expectation.theme)
        viewModel.onIntent(ThemeEditorIntent.Option.ChangeOption(changed.values))
        assertTrue(expectation.theme.spec.isSame(changed, viewModel.state.value.config))
        viewModel.onIntent(ThemeEditorIntent.ApplyTheme)
        withTimeout(10_000L) {
            dataStoreManager.aodThemeId.filterNotNull().first { id -> id == expectation.theme.id }
            dataStoreManager.observeAodThemeConfigs().first { configs ->
                expectation.theme.spec.isSame(changed, configs.getValue(expectation.theme))
            }
        }
        val applied = withTimeout(10_000L) {
            viewModel.state.first { state ->
                state.isActive && !state.isApplying && expectation.theme.spec.isSame(changed, state.savedConfig)
            }
        }
        assertFalse(applied.isPermissionSheetVisible)

        val reopened = ThemeEditorViewModel(
            themeId = expectation.theme.id,
            focusBlock = null,
            dataStoreManager = dataStoreManager,
            permissionManager = permissionManager,
        )
        val reopenedState = awaitLoaded(reopened)
        assertTrue(expectation.theme.spec.isSame(changed, reopenedState.config))
        assertTrue(reopenedState.isActive)
    }

    private suspend fun awaitLoaded(viewModel: ThemeEditorViewModel) = withTimeout(10_000L) {
        viewModel.state.first { state -> !state.isLoading }
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun cases(): List<Array<ThemeExpectation>> = selectedThemeExpectations()
            .map { expectation -> arrayOf(expectation) }
    }
}