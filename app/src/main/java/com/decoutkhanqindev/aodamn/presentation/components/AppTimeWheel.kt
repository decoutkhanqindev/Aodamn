package com.decoutkhanqindev.aodamn.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.theme.Grey5A
import com.decoutkhanqindev.aodamn.presentation.theme.Grey9A
import com.decoutkhanqindev.aodamn.presentation.theme.GreyED
import com.decoutkhanqindev.aodamn.presentation.theme.Mint
import com.decoutkhanqindev.aodamn.presentation.theme.MintAlpha12
import com.decoutkhanqindev.aodamn.presentation.theme.MintAlpha40
import com.decoutkhanqindev.aodamn.presentation.theme.Neutral12
import com.decoutkhanqindev.aodamn.presentation.theme.RoundedCornerShape12dp
import com.decoutkhanqindev.aodamn.utils.collectCatching
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalTime
import kotlin.math.abs

@Composable
fun AppTimeWheel(
    initialTime: LocalTime,
    is24Hour: Boolean,
    onTimeChange: (LocalTime) -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentOnTimeChange by rememberUpdatedState(onTimeChange)
    var hour by remember { mutableIntStateOf(initialTime.hour) }
    var minute by remember { mutableIntStateOf(initialTime.minute) }
    val am = stringResource(R.string.time_am)
    val pm = stringResource(R.string.time_pm)
    val publish = { currentOnTimeChange(LocalTime.of(hour, minute)) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .height(44.dp)
                .background(color = MintAlpha12, shape = RoundedCornerShape12dp)
                .border(width = 1.dp, color = MintAlpha40, shape = RoundedCornerShape12dp),
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (is24Hour) {
                AppWheel(count = 24, initialIndex = hour, label = { "%02d".format(it) }, onIndexChange = { hour = it; publish() })
            } else {
                AppWheel(
                    count = 12,
                    initialIndex = hour % 12,
                    label = { if (it == 0) "12" else "%02d".format(it) },
                    onIndexChange = { index -> hour = index + if (hour >= 12) 12 else 0; publish() },
                )
            }

            Text(
                text = ":",
                modifier = Modifier.padding(horizontal = 4.dp),
                color = Mint,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
            )

            AppWheel(count = 60, initialIndex = minute, label = { "%02d".format(it) }, onIndexChange = { minute = it; publish() })

            if (!is24Hour) {
                AppWheel(
                    count = 2,
                    initialIndex = if (hour >= 12) 1 else 0,
                    label = { if (it == 0) am else pm },
                    onIndexChange = { index -> hour = hour % 12 + index * 12; publish() },
                    isLooping = false,
                    width = 64.dp,
                )
            }
        }
    }
}

@Composable
private fun AppWheel(
    count: Int,
    initialIndex: Int,
    label: (Int) -> String,
    onIndexChange: (Int) -> Unit,
    isLooping: Boolean = true,
    width: Dp = 72.dp,
) {
    val tag = LocalTag.current
    val scope = rememberCoroutineScope()
    val currentOnIndexChange by rememberUpdatedState(onIndexChange)
    val total = if (isLooping) count * 400 else count
    val state = rememberLazyListState(initialFirstVisibleItemIndex = if (isLooping) count * 200 + initialIndex else initialIndex)
    val centered by remember { derivedStateOf { state.centeredIndex() } }
    var lastIndex by remember { mutableIntStateOf(initialIndex) }

    LaunchedEffect(state) {
        snapshotFlow { centered }.collectCatching(
            block = { index ->
                val value = index % count
                if (value != lastIndex) {
                    lastIndex = value
                    currentOnIndexChange(value)
                }
            },
            catch = { e -> Timber.tag(tag).e(e.stackTraceToString()) },
        )
    }

    LazyColumn(
        modifier = Modifier
            .width(width)
            .height(220.dp),
        state = state,
        contentPadding = PaddingValues(vertical = 88.dp),
        flingBehavior = rememberSnapFlingBehavior(lazyListState = state, snapPosition = SnapPosition.Center),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(count = total) { index ->
            val distance = abs(index - centered)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .onClick(ripple = false) { scope.launch { state.animateScrollToItem(index) } },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = label(index % count),
                    color = when (distance) {
                        0 -> GreyED
                        1 -> Grey9A
                        else -> Grey5A
                    },
                    fontSize = when (distance) {
                        0 -> 30.sp
                        1 -> 22.sp
                        else -> 18.sp
                    },
                    fontWeight = if (distance == 0) FontWeight.Bold else FontWeight.Medium,
                )
            }
        }
    }
}

private fun LazyListState.centeredIndex(): Int {
    val info = layoutInfo
    val center = (info.viewportStartOffset + info.viewportEndOffset) / 2
    return info.visibleItemsInfo.minByOrNull { item -> abs(item.offset + item.size / 2 - center) }?.index ?: firstVisibleItemIndex
}

@Preview(widthDp = 360)
@Composable
private fun AppTimeWheelPreview() {
    Box(modifier = Modifier.background(Neutral12)) {
        AppTimeWheel(initialTime = LocalTime.of(22, 30), is24Hour = true, onTimeChange = {})
    }
}
