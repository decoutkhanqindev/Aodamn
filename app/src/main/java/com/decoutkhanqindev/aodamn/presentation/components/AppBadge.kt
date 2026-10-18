package com.decoutkhanqindev.aodamn.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.theme.LabelMedium
import com.decoutkhanqindev.aodamn.presentation.theme.Mint
import com.decoutkhanqindev.aodamn.presentation.theme.MintAlpha12
import com.decoutkhanqindev.aodamn.presentation.theme.Theme

@Composable
fun AppBadge(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier
            .clip(CircleShape)
            .background(MintAlpha12)
            .padding(
                horizontal = 10.dp,
                vertical = 3.dp,
            ),
        color = Mint,
        style = LabelMedium,
    )
}

@Preview
@Composable
private fun AppBadgePreview() {
    Theme {
        AppBadge(text = stringResource(R.string.coming_soon))
    }
}
