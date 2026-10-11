package com.decoutkhanqindev.custom_aod.presentation.screens.iap

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.AppBadge
import com.decoutkhanqindev.custom_aod.presentation.components.AppTopBar
import com.decoutkhanqindev.custom_aod.presentation.theme.BodyMedium
import com.decoutkhanqindev.custom_aod.presentation.theme.Grey9A
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyED
import com.decoutkhanqindev.custom_aod.presentation.theme.Orange
import com.decoutkhanqindev.custom_aod.presentation.theme.Theme
import com.decoutkhanqindev.custom_aod.presentation.theme.TitleLarge

// TODO: Danh sách gói và nút mua thay cho màn giữ chỗ.
@Composable
fun IapContent(onNavigateBack: () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppTopBar(
                title = stringResource(R.string.iap_title),
                onNavigateBack = onNavigateBack,
            )
        },
        containerColor = Color.Transparent,
        contentColor = GreyED,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_crown_simple_duotone),
                contentDescription = null,
                modifier = Modifier.size(72.dp),
                tint = Orange,
            )

            Text(
                text = stringResource(R.string.iap_soon_title),
                modifier = Modifier.padding(top = 24.dp),
                color = GreyED,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                style = TitleLarge,
            )

            Text(
                text = stringResource(R.string.iap_soon_desc),
                modifier = Modifier.padding(top = 8.dp),
                color = Grey9A,
                textAlign = TextAlign.Center,
                style = BodyMedium,
            )

            AppBadge(
                text = stringResource(R.string.coming_soon),
                modifier = Modifier.padding(top = 20.dp),
            )
        }
    }
}

@Preview(widthDp = 360, heightDp = 720)
@Composable
private fun IapContentPreview() {
    Theme {
        IapContent(onNavigateBack = {})
    }
}
