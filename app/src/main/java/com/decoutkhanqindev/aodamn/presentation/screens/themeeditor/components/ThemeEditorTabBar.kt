package com.decoutkhanqindev.aodamn.presentation.screens.themeeditor.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.presentation.components.editor.AppEditorTab
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.pebble.PebbleThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.themeeditor.state.ThemeEditorIntent
import com.decoutkhanqindev.aodamn.presentation.theme.GreyED
import com.decoutkhanqindev.aodamn.presentation.theme.Neutral12
import com.decoutkhanqindev.aodamn.presentation.theme.Theme
import kotlinx.collections.immutable.ImmutableList

@Composable
fun ThemeEditorTabBar(
    tabs: ImmutableList<AodThemeTab>,
    selectedTab: AodThemeTab?,
    onIntent: (ThemeEditorIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Neutral12,
        contentColor = GreyED,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
        ) {
            tabs.forEach { tab ->
                AppEditorTab(
                    iconRes = tab.iconRes,
                    label = stringResource(tab.labelRes),
                    isSelected = tab == selectedTab,
                    onClick = { onIntent(ThemeEditorIntent.Edit.SelectTab(tab)) },
                )
            }
        }
    }
}

@Preview(widthDp = 360)
@Composable
private fun ThemeEditorTabBarPreview() {
    Theme {
        ThemeEditorTabBar(
            tabs = PebbleThemeSpec.tabs,
            selectedTab = PebbleThemeSpec.tabs.first(),
            onIntent = {},
        )
    }
}
