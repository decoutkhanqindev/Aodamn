package com.decoutkhanqindev.aodamn.presentation.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.AppPrimaryButton
import com.decoutkhanqindev.aodamn.presentation.components.sheet.AppSheetHeroHeader
import com.decoutkhanqindev.aodamn.presentation.theme.EdgeGlowOfflineGradient
import com.decoutkhanqindev.aodamn.presentation.theme.Neutral12
import com.decoutkhanqindev.aodamn.presentation.theme.Red
import com.decoutkhanqindev.aodamn.presentation.theme.RoundedCornerShape24dp

@Composable
fun AppNoInternetDialog(onOpenSettings: () -> Unit) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
        ),
    ) {
        AppNoInternetCard(onOpenSettings = onOpenSettings)
    }
}

@Composable
private fun AppNoInternetCard(onOpenSettings: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Neutral12, shape = RoundedCornerShape24dp)
            .border(width = 1.dp, brush = EdgeGlowOfflineGradient, shape = RoundedCornerShape24dp)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AppSheetHeroHeader(
            title = stringResource(R.string.no_internet_title),
            description = stringResource(R.string.no_internet_connection),
            badgeIconRes = R.drawable.ic_wifi_slash,
            badgeColor = Red,
            isOffline = true,
        )

        AppPrimaryButton(
            text = stringResource(R.string.open_settings),
            onClick = onOpenSettings,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            iconRes = R.drawable.ic_caret_right,
        )
    }
}

@Preview(widthDp = 340)
@Composable
private fun AppNoInternetDialogPreview() {
    AppNoInternetCard(onOpenSettings = {})
}
