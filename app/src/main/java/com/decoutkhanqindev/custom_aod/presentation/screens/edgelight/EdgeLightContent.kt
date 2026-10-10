package com.decoutkhanqindev.custom_aod.presentation.screens.edgelight

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.AppBadge
import com.decoutkhanqindev.custom_aod.presentation.components.AppLottie
import com.decoutkhanqindev.custom_aod.presentation.components.AppTabTopBar
import com.decoutkhanqindev.custom_aod.presentation.screens.edgelight.state.EdgeLightIntent
import com.decoutkhanqindev.custom_aod.presentation.theme.BackgroundGradient
import com.decoutkhanqindev.custom_aod.presentation.theme.BodyMedium
import com.decoutkhanqindev.custom_aod.presentation.theme.Grey9A
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyB4
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyED
import com.decoutkhanqindev.custom_aod.presentation.theme.Mint
import com.decoutkhanqindev.custom_aod.presentation.theme.Neutral12
import com.decoutkhanqindev.custom_aod.presentation.theme.RoundedCornerShape16dp
import com.decoutkhanqindev.custom_aod.presentation.theme.Theme
import com.decoutkhanqindev.custom_aod.presentation.theme.TitleLarge

// TODO: Màn Edge Light thật (hiệu ứng viền sáng khi có thông báo / đang phát nhạc) thay cho màn giữ chỗ.
@Composable
fun EdgeLightContent(onIntent: (EdgeLightIntent) -> Unit) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGradient)
            .statusBarsPadding(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .heightIn(min = maxHeight)
                .padding(
                    horizontal = 20.dp,
                    vertical = 12.dp,
                ),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            AppTabTopBar(
                title = stringResource(R.string.edge_light),
                onIapClick = { onIntent(EdgeLightIntent.NavigateToIap) },
                onShareClick = { onIntent(EdgeLightIntent.ShareApp) },
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AppLottie(
                    resId = R.raw.lottie_device_edge_light,
                    modifier = Modifier.size(220.dp),
                )

                Text(
                    text = stringResource(R.string.edge_light_soon_title),
                    color = GreyED,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    style = TitleLarge,
                )

                Text(
                    text = stringResource(R.string.edge_light_soon_desc),
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .widthIn(max = 300.dp),
                    color = Grey9A,
                    textAlign = TextAlign.Center,
                    style = BodyMedium,
                )

                AppBadge(
                    text = stringResource(R.string.edge_light_in_progress),
                    modifier = Modifier.padding(top = 20.dp),
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape16dp)
                    .background(Neutral12)
                    .padding(16.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Mint,
                )

                Text(
                    text = stringResource(R.string.edge_light_glow_hint),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp),
                    color = GreyB4,
                    style = BodyMedium,
                )
            }
        }
    }
}

@Preview(widthDp = 360, heightDp = 720)
@Composable
private fun EdgeLightContentPreview() {
    Theme {
        EdgeLightContent(onIntent = {})
    }
}
