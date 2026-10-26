package com.decoutkhanqindev.aodamn.presentation.components.aod

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.classic.AodClassicClock
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodLandscapePlan
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodPanelBlockValue
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState
import com.decoutkhanqindev.aodamn.presentation.theme.Grey3A
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun AodLandscapeLayout(
    plan: AodLandscapePlan,
    state: AodState,
    onIntent: (AodIntent) -> Unit,
    modifier: Modifier = Modifier,
    isThreeColumnAllowed: Boolean = true,
    hero: @Composable () -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        when {
            plan.isEmpty -> hero()

            plan.hasMediaWithOthers && isThreeColumnAllowed -> AodThreeColumns(
                start = {
                    if (plan.info.isNotEmpty()) {
                        AodPanelColumn(blocks = plan.info, state = state, onIntent = onIntent)
                    } else {
                        AodPanelBlock(block = AodPanelBlockValue.DRAWING, state = state, onIntent = onIntent)
                    }
                },
                center = hero,
                end = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (plan.info.isNotEmpty() && plan.hasDrawing) {
                            AodPanelBlock(block = AodPanelBlockValue.DRAWING, state = state, onIntent = onIntent)
                        }

                        AodPanelBlock(block = AodPanelBlockValue.MEDIA, state = state, onIntent = onIntent)
                    }
                },
            )

            else -> AodSplitRow(
                start = hero,
                end = { AodLandscapePanel(plan = plan, state = state, onIntent = onIntent) },
            )
        }
    }
}

@Composable
fun AodLandscapePanel(
    plan: AodLandscapePlan,
    state: AodState,
    onIntent: (AodIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (plan.hasDrawing && plan.info.isNotEmpty()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AodPanelBlock(block = AodPanelBlockValue.DRAWING, state = state, onIntent = onIntent)

                AodPanelColumn(blocks = plan.info, state = state, onIntent = onIntent)
            }
        } else {
            AodPanelColumn(blocks = plan.info, state = state, onIntent = onIntent)

            if (plan.hasDrawing) {
                AodPanelBlock(block = AodPanelBlockValue.DRAWING, state = state, onIntent = onIntent)
            }
        }

        if (plan.hasMedia) {
            AodPanelBlock(block = AodPanelBlockValue.MEDIA, state = state, onIntent = onIntent)
        }
    }
}

@Composable
fun AodPanelColumn(
    blocks: ImmutableList<AodPanelBlockValue>,
    state: AodState,
    onIntent: (AodIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        blocks.forEach { block ->
            key(block) {
                AodPanelBlock(block = block, state = state, onIntent = onIntent)
            }
        }
    }
}

@Composable
fun AodSplitRow(
    start: @Composable () -> Unit,
    end: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        start()

        AodColumnDivider(modifier = Modifier.padding(horizontal = 32.dp))

        Box(modifier = Modifier.weight(1f, fill = false)) {
            end()
        }
    }
}

@Composable
fun AodThreeColumns(
    start: @Composable () -> Unit,
    center: @Composable () -> Unit,
    end: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Layout(
        content = {
            Box { start() }
            Box { center() }
            Box { end() }
            AodColumnDivider()
            AodColumnDivider()
        },
        modifier = modifier,
    ) { measurables, constraints ->
        val loose = constraints.copy(minWidth = 0, minHeight = 0)
        val centerColumn = measurables[1].measure(loose)
        val startDivider = measurables[3].measure(loose)
        val endDivider = measurables[4].measure(loose)
        val gap = 28.dp.roundToPx()
        val sideWidth = ((constraints.maxWidth - centerColumn.width) / 2 - gap * 2 - startDivider.width - 16.dp.roundToPx())
            .coerceIn(0, 280.dp.roundToPx())
        val sideConstraints = loose.copy(maxWidth = sideWidth)
        val startColumn = measurables[0].measure(sideConstraints)
        val endColumn = measurables[2].measure(sideConstraints)
        val height = maxOf(startColumn.height, centerColumn.height, endColumn.height, startDivider.height)

        layout(width = constraints.maxWidth, height = height) {
            val centerX = (constraints.maxWidth - centerColumn.width) / 2
            val startDividerX = centerX - gap - startDivider.width
            val endDividerX = centerX + centerColumn.width + gap
            val startSlotX = startDividerX - gap - sideWidth
            val endSlotX = endDividerX + endDivider.width + gap

            centerColumn.place(x = centerX, y = (height - centerColumn.height) / 2)
            startDivider.place(x = startDividerX, y = (height - startDivider.height) / 2)
            endDivider.place(x = endDividerX, y = (height - endDivider.height) / 2)
            startColumn.place(x = startSlotX + (sideWidth - startColumn.width) / 2, y = (height - startColumn.height) / 2)
            endColumn.place(x = endSlotX + (sideWidth - endColumn.width) / 2, y = (height - endColumn.height) / 2)
        }
    }
}

@Composable
private fun AodColumnDivider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(width = 1.dp, height = 160.dp)
            .background(Grey3A),
    )
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodLandscapeLayoutPreview() {
    val state = AodState(
        nowMillis = System.currentTimeMillis(),
        battery = BatteryUiModel(percent = 72, isCharging = false),
    )

    AodLandscapeLayout(
        plan = AodLandscapePlan(info = persistentListOf(AodPanelBlockValue.MEMO), hasMedia = true),
        state = state,
        onIntent = {},
    ) {
        AodClassicClock(nowMillis = state.nowMillis, config = state.appearance.theme.config)
    }
}
