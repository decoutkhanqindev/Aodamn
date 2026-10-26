package com.decoutkhanqindev.aodamn.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.presentation.theme.Neutral12
import com.decoutkhanqindev.aodamn.presentation.theme.Neutral17

@Composable
fun AppBottomAdSlot(
    modifier: Modifier = Modifier,
    isHidden: Boolean = false,
    background: Color = Neutral12,
    content: @Composable () -> Unit,
) {
    val hiddenFraction by animateFloatAsState(targetValue = if (isHidden) 1f else 0f, label = "AppBottomAdSlot")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer { translationY = size.height * hiddenFraction }
            .background(background)
            .navigationBarsPadding(),
    ) {
        content()
    }
}

@Preview(widthDp = 360)
@Composable
private fun AppBottomAdSlotPreview() {
    AppBottomAdSlot {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Neutral17),
        )
    }
}
