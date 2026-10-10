package com.decoutkhanqindev.custom_aod.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyED
import com.decoutkhanqindev.custom_aod.presentation.theme.HeadlineSmall
import com.decoutkhanqindev.custom_aod.presentation.theme.Theme

@Composable
fun AppTabTopBar(
    title: String,
    onIapClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            modifier = Modifier
                .weight(1f)
                .semantics { heading() },
            color = GreyED,
            fontWeight = FontWeight.SemiBold,
            style = HeadlineSmall,
        )

        Row(modifier = Modifier.offset(x = 12.dp)) {
            IconButton(onClick = onIapClick) {
                Icon(
                    imageVector = Icons.Outlined.WorkspacePremium,
                    contentDescription = stringResource(R.string.iap_title),
                    tint = GreyED,
                )
            }

            IconButton(onClick = onShareClick) {
                Icon(
                    imageVector = Icons.Outlined.Share,
                    contentDescription = stringResource(R.string.share_app),
                    tint = GreyED,
                )
            }
        }
    }
}

@Preview(widthDp = 360)
@Composable
private fun AppTabTopBarPreview() {
    Theme {
        AppTabTopBar(
            title = stringResource(R.string.aod_tab_title),
            onIapClick = {},
            onShareClick = {},
        )
    }
}
