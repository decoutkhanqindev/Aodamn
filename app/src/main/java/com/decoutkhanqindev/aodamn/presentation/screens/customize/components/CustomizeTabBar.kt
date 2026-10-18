package com.decoutkhanqindev.aodamn.presentation.screens.customize.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Surface
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.decoutkhanqindev.aodamn.presentation.components.editor.AppEditorTab
import com.decoutkhanqindev.aodamn.presentation.components.onClick
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.customize.CustomizeClusterValue
import com.decoutkhanqindev.aodamn.presentation.model.customize.CustomizeGuideStepValue
import com.decoutkhanqindev.aodamn.presentation.model.customize.CustomizeTabValue
import com.decoutkhanqindev.aodamn.presentation.screens.customize.state.CustomizeIntent
import com.decoutkhanqindev.aodamn.presentation.theme.Grey9A
import com.decoutkhanqindev.aodamn.presentation.theme.Neutral12
import com.decoutkhanqindev.aodamn.presentation.theme.NeutralVariant30
import kotlin.math.roundToInt

@Composable
fun CustomizeTabBar(
    selectedTab: CustomizeTabValue?,
    spec: AodThemeSpec,
    guideStep: CustomizeGuideStepValue?,
    onIntent: (CustomizeIntent) -> Unit,
    onClusterPositioned: (CustomizeGuideStepValue, Rect) -> Unit,
) {
    val density = LocalDensity.current
    val scrollState = rememberScrollState()
    val clusterStarts = remember { mutableMapOf<CustomizeClusterValue, Float>() }
    val viewportStart = remember { floatArrayOf(0f) }

    LaunchedEffect(guideStep) {
        if (guideStep == CustomizeGuideStepValue.entries.first()) {
            scrollState.animateScrollTo(0)
            return@LaunchedEffect
        }
        val start =
            guideStep?.cluster?.let { cluster -> clusterStarts[cluster] } ?: return@LaunchedEffect
        val padding = with(density) { 8.dp.toPx() }
        scrollState.animateScrollTo(
            (scrollState.value + start - viewportStart[0] - padding).roundToInt().coerceAtLeast(0)
        )
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Neutral12,
        contentColor = Grey9A,
    ) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .onGloballyPositioned { coordinates ->
                        viewportStart[0] = coordinates.positionInWindow().x
                    }
                    .horizontalScroll(scrollState)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CustomizeClusterValue.entries.forEachIndexed { index, cluster ->
                    if (index > 0) {
                        VerticalDivider(
                            modifier = Modifier
                                .height(20.dp)
                                .padding(horizontal = 4.dp),
                            color = NeutralVariant30,
                        )
                    }

                    val step = CustomizeGuideStepValue.entries.first { it.cluster == cluster }
                    Row(
                        modifier = Modifier.onGloballyPositioned { coordinates ->
                            val position = coordinates.positionInWindow()
                            clusterStarts[cluster] = position.x
                            onClusterPositioned(step, Rect(position, coordinates.size.toSize()))
                        },
                    ) {
                        CustomizeTabValue.entries.filter { it.cluster == cluster && it.isShownIn(spec) }.forEach { tab ->
                            AppEditorTab(
                                iconRes = tab.iconRes,
                                label = stringResource(tab.labelRes),
                                isSelected = tab == selectedTab,
                                onClick = { onIntent(CustomizeIntent.Panel.SelectTab(tab)) },
                            )
                        }
                    }
                }
            }
        }
    }
}

