package com.decoutkhanqindev.custom_aod.presentation.screens.themeeditor.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.custom_aod.presentation.components.AppEditorTab
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.PebbleThemeSpec
import com.decoutkhanqindev.custom_aod.presentation.screens.themeeditor.state.ThemeEditorIntent
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyED
import com.decoutkhanqindev.custom_aod.presentation.theme.Neutral12
import com.decoutkhanqindev.custom_aod.presentation.theme.Theme
import kotlinx.collections.immutable.ImmutableList

// Tab lấy từ AodThemeSpec.tabs của chủ đề; ít tab thì nằm giữa, nhiều thì cuộn ngang. Chạm tab đang mở thì đóng tấm chỉnh.
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
                .navigationBarsPadding()
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
