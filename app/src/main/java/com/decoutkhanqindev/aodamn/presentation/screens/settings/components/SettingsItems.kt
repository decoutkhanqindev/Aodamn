package com.decoutkhanqindev.aodamn.presentation.screens.settings.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.AppIconTile
import com.decoutkhanqindev.aodamn.presentation.components.AppSlider
import com.decoutkhanqindev.aodamn.presentation.components.onClick
import com.decoutkhanqindev.aodamn.presentation.components.settings.AppSectionHeader
import com.decoutkhanqindev.aodamn.presentation.theme.BodyLarge
import com.decoutkhanqindev.aodamn.presentation.theme.BodyMedium
import com.decoutkhanqindev.aodamn.presentation.theme.Grey9A
import com.decoutkhanqindev.aodamn.presentation.theme.GreyB4
import com.decoutkhanqindev.aodamn.presentation.theme.GreyED
import com.decoutkhanqindev.aodamn.presentation.theme.Mint
import com.decoutkhanqindev.aodamn.presentation.theme.Neutral12
import com.decoutkhanqindev.aodamn.presentation.theme.NeutralVariant30
import com.decoutkhanqindev.aodamn.presentation.theme.RoundedCornerShape16dp
import com.decoutkhanqindev.aodamn.presentation.theme.Theme

@Composable
fun SettingsGroup(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        AppSectionHeader(
            title = title,
            modifier = Modifier.padding(start = 16.dp),
        )

        Column(
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape16dp)
                .background(Neutral12),
            content = content,
        )
    }
}

@Composable
fun SettingsDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier.padding(start = 68.dp),
        color = NeutralVariant30,
    )
}

@Composable
fun SettingsSwitchItem(
    @DrawableRes iconRes: Int,
    label: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    isEnabled: Boolean = true,
) {
    val contentAlpha = if (isEnabled) 1f else 0.38f

    ListItem(
        headlineContent = {
            Text(
                text = label,
                modifier = Modifier.alpha(contentAlpha),
            )
        },
        modifier = modifier.toggleable(
            value = isChecked,
            enabled = isEnabled,
            role = Role.Switch,
            onValueChange = onCheckedChange,
        ),
        supportingContent = if (description != null) {
            {
                Text(
                    text = description,
                    modifier = Modifier.alpha(contentAlpha),
                )
            }
        } else {
            null
        },
        leadingContent = {
            AppIconTile(
                iconRes = iconRes,
                modifier = Modifier.alpha(contentAlpha),
            )
        },
        trailingContent = {
            Switch(
                checked = isChecked,
                onCheckedChange = null,
                enabled = isEnabled,
            )
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
    )
}

@Composable
fun SettingsValueItem(
    @DrawableRes iconRes: Int,
    label: String,
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ListItem(
        headlineContent = { Text(text = label) },
        modifier = modifier.onClick(action = onClick),
        supportingContent = {
            Text(
                text = value,
                color = Mint,
            )
        },
        leadingContent = { AppIconTile(iconRes = iconRes) },
        trailingContent = {
            Icon(
                painter = painterResource(R.drawable.ic_caret_right),
                contentDescription = null,
                tint = Grey9A,
            )
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
    )
}

@Composable
fun SettingsSliderItem(
    label: String,
    valueText: String,
    progress: () -> Int,
    valueRange: IntRange,
    step: Int,
    onProgressChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                top = 12.dp,
                end = 16.dp,
                bottom = 4.dp,
            ),
    ) {
        Row(
            modifier = Modifier.heightIn(min = 36.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (iconRes != null) {
                AppIconTile(
                    iconRes = iconRes,
                    modifier = Modifier.padding(end = 16.dp),
                )
            } else {
                Spacer(modifier = Modifier.width(52.dp))
            }

            Text(
                text = label,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp),
                color = GreyED,
                style = BodyLarge,
            )

            Text(
                text = valueText,
                color = Mint,
                fontWeight = FontWeight.Medium,
                style = BodyMedium,
            )
        }

        AppSlider(
            progress = progress,
            valueRange = valueRange,
            step = step,
            onProgressChange = onProgressChange,
            modifier = Modifier.padding(start = 52.dp),
            label = label,
            valueText = valueText,
        )
    }
}

@Composable
fun SettingsSubItem(
    label: String,
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .onClick(action = onClick)
            .padding(
                start = 68.dp,
                end = 16.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp),
            color = GreyB4,
            style = BodyMedium,
        )

        Text(
            text = value,
            color = Mint,
            fontWeight = FontWeight.Medium,
            style = BodyMedium,
        )
    }
}

@Preview(widthDp = 360)
@Composable
private fun SettingsItemsPreview() {
    Theme {
        SettingsGroup(title = stringResource(R.string.section_display)) {
            SettingsSwitchItem(
                iconRes = R.drawable.ic_sun_duotone,
                label = stringResource(R.string.opt_custom_brightness),
                isChecked = true,
                onCheckedChange = {},
            )

            SettingsSliderItem(
                label = stringResource(R.string.opt_brightness),
                valueText = stringResource(R.string.value_percent, 20),
                progress = { 20 },
                valueRange = 1..100,
                step = 1,
                onProgressChange = {},
            )

            SettingsDivider()

            SettingsSliderItem(
                label = stringResource(R.string.opt_timeout),
                valueText = stringResource(R.string.value_minutes, 30),
                progress = { 30 },
                valueRange = 0..60,
                step = 5,
                onProgressChange = {},
                iconRes = R.drawable.ic_timer_duotone,
            )

            SettingsDivider()

            SettingsValueItem(
                iconRes = R.drawable.ic_plugs_connected_duotone,
                label = stringResource(R.string.opt_charging_rule),
                value = stringResource(R.string.charging_rule_always),
                onClick = {},
            )

            SettingsDivider()

            SettingsSwitchItem(
                iconRes = R.drawable.ic_calendar_dots_duotone,
                label = stringResource(R.string.opt_schedule),
                isChecked = true,
                onCheckedChange = {},
                description = stringResource(R.string.opt_auto_dim_desc),
            )
        }
    }
}
