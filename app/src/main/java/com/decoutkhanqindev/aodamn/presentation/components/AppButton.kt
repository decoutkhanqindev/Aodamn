package com.decoutkhanqindev.aodamn.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.theme.Black
import com.decoutkhanqindev.aodamn.presentation.theme.Grey3A
import com.decoutkhanqindev.aodamn.presentation.theme.Grey6E
import com.decoutkhanqindev.aodamn.presentation.theme.LabelLarge
import com.decoutkhanqindev.aodamn.presentation.theme.GreyED
import com.decoutkhanqindev.aodamn.presentation.theme.Mint
import com.decoutkhanqindev.aodamn.presentation.theme.Neutral12
import com.decoutkhanqindev.aodamn.presentation.theme.Red
import com.decoutkhanqindev.aodamn.presentation.theme.RedAlpha12
import com.decoutkhanqindev.aodamn.presentation.theme.RedLight
import com.decoutkhanqindev.aodamn.presentation.theme.RoundedCornerShape24dp
import com.decoutkhanqindev.aodamn.presentation.theme.TitleMedium

@Composable
fun AppPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int? = null,
    isEnabled: Boolean = true,
    isCompact: Boolean = false,
) {
    val container by animateColorAsState(if (isEnabled) Mint else Grey3A, label = "AppPrimaryButtonContainer")
    val content by animateColorAsState(if (isEnabled) Black else Grey6E, label = "AppPrimaryButtonContent")

    AppButtonBox(
        text = text,
        onClick = onClick,
        containerColor = container,
        contentColor = content,
        modifier = modifier,
        iconRes = iconRes,
        isEnabled = isEnabled,
        isCompact = isCompact,
    )
}

@Composable
fun AppDangerButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int? = null,
) {
    AppButtonBox(
        text = text,
        onClick = onClick,
        containerColor = RedAlpha12,
        contentColor = RedLight,
        modifier = modifier,
        iconRes = iconRes,
    )
}

@Composable
fun AppTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int? = null,
    contentColor: Color = GreyED,
    isEnabled: Boolean = true,
    isCompact: Boolean = false,
) {
    AppButtonBox(
        text = text,
        onClick = onClick,
        containerColor = Color.Transparent,
        contentColor = if (isEnabled) contentColor else Grey6E,
        modifier = modifier,
        iconRes = iconRes,
        isEnabled = isEnabled,
        isCompact = isCompact,
    )
}

@Composable
private fun AppButtonBox(
    text: String,
    onClick: () -> Unit,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int? = null,
    isEnabled: Boolean = true,
    isCompact: Boolean = false,
) {
    Row(
        modifier = modifier
            .heightIn(min = if (isCompact) 44.dp else 48.dp)
            .clip(RoundedCornerShape24dp)
            .background(containerColor)
            .then(if (isEnabled) Modifier.onClick(shape = RoundedCornerShape24dp, action = onClick) else Modifier)
            .semantics {
                role = Role.Button
                if (!isEnabled) disabled()
            }
            .padding(horizontal = if (isCompact) 16.dp else 20.dp, vertical = if (isCompact) 8.dp else 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (iconRes != null) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = contentColor,
            )
        }

        Text(
            text = text,
            color = contentColor,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = if (isCompact) 1 else Int.MAX_VALUE,
            style = if (isCompact) LabelLarge else TitleMedium,
        )
    }
}

@Preview(widthDp = 360)
@Composable
private fun AppButtonPreview() {
    Column(
        modifier = Modifier
            .background(Neutral12)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        AppPrimaryButton(text = "Áp dụng", onClick = {}, modifier = Modifier.fillMaxWidth(), iconRes = R.drawable.ic_check)
        AppPrimaryButton(text = "Tiếp tục", onClick = {}, modifier = Modifier.fillMaxWidth(), isEnabled = false)
        AppDangerButton(text = "Bỏ thay đổi", onClick = {}, modifier = Modifier.fillMaxWidth())
        AppTextButton(text = "Hủy", onClick = {}, modifier = Modifier.fillMaxWidth(), contentColor = Red)
    }
}
