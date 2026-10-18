package com.decoutkhanqindev.aodamn.presentation.components.editor

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.onClick
import com.decoutkhanqindev.aodamn.presentation.theme.GreyED
import com.decoutkhanqindev.aodamn.presentation.theme.Mint
import com.decoutkhanqindev.aodamn.presentation.theme.Neutral12
import com.decoutkhanqindev.aodamn.presentation.theme.NeutralVariant30
import com.decoutkhanqindev.aodamn.presentation.theme.Theme

// Nút tròn nổi trên khung xem trước của màn sửa (xoay khung, hướng dẫn): không chiếm chỗ của thanh trên hay thanh tab. isActive: icon mint.
@Composable
fun AppEditorFab(
    @DrawableRes iconRes: Int,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isActive: Boolean = false,
) {
    val tint by animateColorAsState(
        targetValue = if (isActive) Mint else GreyED,
        label = "AppEditorFabTint",
    )

    Box(
        modifier = modifier
            .size(48.dp)
            .onClick(shape = CircleShape, action = onClick)
            .background(color = Neutral12, shape = CircleShape)
            .border(width = 1.dp, color = NeutralVariant30, shape = CircleShape)
            .semantics {
                role = Role.Button
                this.contentDescription = contentDescription
            },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            modifier = Modifier.size(22.dp),
            tint = tint,
        )
    }
}

@Preview
@Composable
private fun AppEditorFabPreview() {
    Theme {
        AppEditorFab(
            iconRes = R.drawable.ic_question,
            contentDescription = stringResource(R.string.customize_show_guide),
            onClick = {},
        )
    }
}
