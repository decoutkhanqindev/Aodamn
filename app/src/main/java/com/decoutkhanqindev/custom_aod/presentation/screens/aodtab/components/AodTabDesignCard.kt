package com.decoutkhanqindev.custom_aod.presentation.screens.aodtab.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.AppIconTile
import com.decoutkhanqindev.custom_aod.presentation.components.aod.AodContent
import com.decoutkhanqindev.custom_aod.presentation.components.aod.rememberAodPreview
import com.decoutkhanqindev.custom_aod.presentation.components.onClick
import com.decoutkhanqindev.custom_aod.presentation.model.customize.CustomizeDraftUiModel
import com.decoutkhanqindev.custom_aod.presentation.screens.aod.state.AodState
import com.decoutkhanqindev.custom_aod.presentation.screens.aodtab.state.AodTabIntent
import com.decoutkhanqindev.custom_aod.presentation.theme.BodyMedium
import com.decoutkhanqindev.custom_aod.presentation.theme.Grey9A
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyED
import com.decoutkhanqindev.custom_aod.presentation.theme.Neutral12
import com.decoutkhanqindev.custom_aod.presentation.theme.NeutralVariant30
import com.decoutkhanqindev.custom_aod.presentation.theme.RoundedCornerShape16dp
import com.decoutkhanqindev.custom_aod.presentation.theme.RoundedCornerShape8dp
import com.decoutkhanqindev.custom_aod.presentation.theme.Theme
import com.decoutkhanqindev.custom_aod.presentation.theme.TitleMedium

@Composable
fun AodTabDesignCard(
    design: CustomizeDraftUiModel,
    nowMillis: Long,
    isAodEnabled: Boolean,
    onIntent: (AodTabIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val preview = rememberAodPreview(
        draft = design,
        nowMillis = nowMillis,
    )
    val previewAlpha by animateFloatAsState(
        targetValue = if (isAodEnabled) 1f else 0.4f,
        label = "DesignPreviewAlpha",
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .onClick(shape = RoundedCornerShape16dp) { onIntent(AodTabIntent.NavigateToCustomize) }
            .background(Neutral12)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DesignThumbnail(
            state = preview,
            modifier = Modifier.alpha(previewAlpha),
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
        ) {
            Text(
                text = stringResource(R.string.aod_tab_design_title),
                color = GreyED,
                fontWeight = FontWeight.SemiBold,
                style = TitleMedium,
            )

            Text(
                text = stringResource(R.string.aod_tab_design_default),
                modifier = Modifier.padding(top = 4.dp),
                color = Grey9A,
                style = BodyMedium,
            )
        }

        val customizeLabel = stringResource(R.string.customize_title)

        AppIconTile(
            icon = Icons.Outlined.Edit,
            modifier = Modifier.semantics { contentDescription = customizeLabel },
        )
    }
}

@Composable
private fun DesignThumbnail(
    state: AodState,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val windowSize = LocalWindowInfo.current.containerSize
    val screenWidth = with(density) { windowSize.width.coerceAtLeast(1).toDp() }
    val screenHeight = with(density) { windowSize.height.coerceAtLeast(1).toDp() }
    val thumbnailHeight = 64.dp
    val scale = thumbnailHeight / screenHeight

    Box(
        modifier = modifier
            .size(
                width = screenWidth * scale,
                height = thumbnailHeight,
            )
            .clip(RoundedCornerShape8dp)
            .border(
                width = 1.dp,
                color = NeutralVariant30,
                shape = RoundedCornerShape8dp,
            )
            .clearAndSetSemantics { },
        contentAlignment = Alignment.Center,
    ) {
        AodContent(
            state = state,
            onIntent = {},
            modifier = Modifier
                .requiredSize(
                    width = screenWidth,
                    height = screenHeight,
                )
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                },
            isInteractive = false,
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .pointerInput(Unit) { },
        )
    }
}

@Preview(widthDp = 360)
@Composable
private fun AodTabDesignCardPreview() {
    Theme {
        AodTabDesignCard(
            design = CustomizeDraftUiModel(),
            nowMillis = System.currentTimeMillis(),
            isAodEnabled = true,
            onIntent = {},
        )
    }
}
