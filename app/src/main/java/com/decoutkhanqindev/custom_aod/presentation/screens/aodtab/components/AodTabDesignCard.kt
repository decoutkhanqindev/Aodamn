package com.decoutkhanqindev.custom_aod.presentation.screens.aodtab.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.AppIconTile
import com.decoutkhanqindev.custom_aod.presentation.components.aod.AodThumbnail
import com.decoutkhanqindev.custom_aod.presentation.components.aod.rememberAodPreview
import com.decoutkhanqindev.custom_aod.presentation.components.onClick
import com.decoutkhanqindev.custom_aod.presentation.model.customize.CustomizeDraftUiModel
import com.decoutkhanqindev.custom_aod.presentation.screens.aodtab.state.AodTabIntent
import com.decoutkhanqindev.custom_aod.presentation.theme.BodyMedium
import com.decoutkhanqindev.custom_aod.presentation.theme.Grey9A
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyED
import com.decoutkhanqindev.custom_aod.presentation.theme.Neutral12
import com.decoutkhanqindev.custom_aod.presentation.theme.RoundedCornerShape16dp
import com.decoutkhanqindev.custom_aod.presentation.theme.Theme
import com.decoutkhanqindev.custom_aod.presentation.theme.TitleMedium

@Composable
fun AodTabDesignCard(
    design: CustomizeDraftUiModel,
    isAodEnabled: Boolean,
    onIntent: (AodTabIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val preview = rememberAodPreview(draft = design)
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
        AodThumbnail(
            state = preview,
            height = 64.dp,
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
                text = stringResource(design.appearance.clock.theme.value.nameRes),
                modifier = Modifier.padding(top = 4.dp),
                color = Grey9A,
                style = BodyMedium,
            )
        }

        val customizeLabel = stringResource(R.string.customize_title)

        AppIconTile(
            iconRes = R.drawable.ic_pencil_simple_duotone,
            modifier = Modifier.semantics { contentDescription = customizeLabel },
        )
    }
}

@Preview(widthDp = 360)
@Composable
private fun AodTabDesignCardPreview() {
    Theme {
        AodTabDesignCard(
            design = CustomizeDraftUiModel(),
            isAodEnabled = true,
            onIntent = {},
        )
    }
}
