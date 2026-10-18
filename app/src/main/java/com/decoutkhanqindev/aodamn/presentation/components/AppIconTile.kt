package com.decoutkhanqindev.aodamn.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.theme.Mint
import com.decoutkhanqindev.aodamn.presentation.theme.MintAlpha12
import com.decoutkhanqindev.aodamn.presentation.theme.RoundedCornerShape12dp
import com.decoutkhanqindev.aodamn.presentation.theme.Theme

@Composable
fun AppIconTile(
    @DrawableRes iconRes: Int,
    modifier: Modifier = Modifier,
    tint: Color = Mint,
    containerColor: Color = MintAlpha12,
) {
    Box(
        modifier = modifier
            .size(36.dp)
            .clip(RoundedCornerShape12dp)
            .background(containerColor),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = tint,
        )
    }
}

@Preview
@Composable
private fun AppIconTilePreview() {
    Theme {
        AppIconTile(iconRes = R.drawable.ic_sparkle_duotone)
    }
}
