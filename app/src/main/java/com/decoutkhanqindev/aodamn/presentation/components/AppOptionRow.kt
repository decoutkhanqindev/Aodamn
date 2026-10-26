package com.decoutkhanqindev.aodamn.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.theme.Grey5A
import com.decoutkhanqindev.aodamn.presentation.theme.Grey9A
import com.decoutkhanqindev.aodamn.presentation.theme.GreyED
import com.decoutkhanqindev.aodamn.presentation.theme.Mint
import com.decoutkhanqindev.aodamn.presentation.theme.MintAlpha12
import com.decoutkhanqindev.aodamn.presentation.theme.Neutral12
import com.decoutkhanqindev.aodamn.presentation.theme.Neutral17
import com.decoutkhanqindev.aodamn.presentation.theme.RoundedCornerShape12dp
import com.decoutkhanqindev.aodamn.presentation.theme.RoundedCornerShape16dp
import com.decoutkhanqindev.aodamn.presentation.theme.TitleMedium

@Composable
fun AppOptionRow(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    unselectedColor: Color = Color.Transparent,
    leading: @Composable () -> Unit,
) {
    val container by animateColorAsState(if (isSelected) MintAlpha12 else unselectedColor, label = "AppOptionRowContainer")
    val border by animateColorAsState(if (isSelected) Mint else Color.Transparent, label = "AppOptionRowBorder")

    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .clip(RoundedCornerShape16dp)
            .background(container)
            .border(width = 1.dp, color = border, shape = RoundedCornerShape16dp)
            .selectable(selected = isSelected, role = Role.RadioButton, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        leading()

        Text(
            text = label,
            modifier = Modifier.weight(1f),
            color = GreyED,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
            style = TitleMedium,
        )

        AnimatedContent(
            targetState = isSelected,
            transitionSpec = { (fadeIn() + scaleIn(initialScale = 0.6f)) togetherWith fadeOut() },
            contentAlignment = Alignment.Center,
            label = "AppOptionRowMark",
        ) { selected ->
            if (selected) {
                Icon(
                    painter = painterResource(R.drawable.ic_check_circle_fill),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = Mint,
                )
            } else {
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .size(20.dp)
                        .border(width = 1.5.dp, color = Grey5A, shape = CircleShape),
                )
            }
        }
    }
}

@Composable
fun AppOptionIcon(
    @DrawableRes iconRes: Int,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    val container by animateColorAsState(if (isSelected) MintAlpha12 else Neutral17, label = "AppOptionIconContainer")
    val tint by animateColorAsState(if (isSelected) Mint else Grey9A, label = "AppOptionIconTint")

    Box(
        modifier = modifier
            .size(40.dp)
            .background(color = container, shape = RoundedCornerShape12dp),
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

@Preview(widthDp = 360)
@Composable
private fun AppOptionRowPreview() {
    Column(
        modifier = Modifier
            .background(Neutral12)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        AppOptionRow(label = "Về màn hình khóa", isSelected = true, onClick = {}) {
            AppOptionIcon(iconRes = R.drawable.ic_lock_key, isSelected = true)
        }
        AppOptionRow(label = "Tối màn hình", isSelected = false, onClick = {}) {
            AppOptionIcon(iconRes = R.drawable.ic_moon, isSelected = false)
        }
    }
}
