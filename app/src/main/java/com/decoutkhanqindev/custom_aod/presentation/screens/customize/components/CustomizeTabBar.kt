package com.decoutkhanqindev.custom_aod.presentation.screens.customize.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.editor.AppEditorTab
import com.decoutkhanqindev.custom_aod.presentation.components.editor.AppPreviewOrientationButton
import com.decoutkhanqindev.custom_aod.presentation.components.onClick
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.custom_aod.presentation.model.customize.CustomizeClusterValue
import com.decoutkhanqindev.custom_aod.presentation.model.customize.CustomizeGuideStepValue
import com.decoutkhanqindev.custom_aod.presentation.model.customize.CustomizeTabValue
import com.decoutkhanqindev.custom_aod.presentation.screens.customize.state.CustomizeIntent
import com.decoutkhanqindev.custom_aod.presentation.theme.Grey9A
import com.decoutkhanqindev.custom_aod.presentation.theme.Neutral12
import com.decoutkhanqindev.custom_aod.presentation.theme.NeutralVariant30
import kotlin.math.roundToInt

@Composable
fun CustomizeTabBar(
    selectedTab: CustomizeTabValue?,
    spec: AodThemeSpec,
    isLandscapePreview: Boolean,
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
            modifier = Modifier.padding(vertical = 4.dp),
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

            VerticalDivider(
                modifier = Modifier.height(20.dp),
                color = NeutralVariant30,
            )

            // Xoay khung xem trước và hướng dẫn đứng yên cuối thanh (thanh trên đã có hoàn tác, Áp dụng, Bỏ qua).
            AppPreviewOrientationButton(
                isLandscape = isLandscapePreview,
                onClick = { onIntent(CustomizeIntent.ToggleLandscapePreview) },
                modifier = Modifier.onGloballyPositioned { coordinates ->
                    onClusterPositioned(CustomizeGuideStepValue.ROTATE, coordinates.boundsInWindow())
                },
            )

            IconButton(
                onClick = { onIntent(CustomizeIntent.Guide.ShowGuide) },
                modifier = Modifier.padding(end = 4.dp),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_question),
                    contentDescription = stringResource(R.string.customize_show_guide),
                )
            }
        }
    }
}

