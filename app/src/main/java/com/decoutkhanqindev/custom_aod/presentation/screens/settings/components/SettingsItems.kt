package com.decoutkhanqindev.custom_aod.presentation.screens.settings.components

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Power
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.AppIconTile
import com.decoutkhanqindev.custom_aod.presentation.components.onClick
import com.decoutkhanqindev.custom_aod.presentation.components.settings.AppSectionHeader
import com.decoutkhanqindev.custom_aod.presentation.theme.BodyLarge
import com.decoutkhanqindev.custom_aod.presentation.theme.BodyMedium
import com.decoutkhanqindev.custom_aod.presentation.theme.Grey9A
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyB4
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyED
import com.decoutkhanqindev.custom_aod.presentation.theme.Mint
import com.decoutkhanqindev.custom_aod.presentation.theme.Neutral12
import com.decoutkhanqindev.custom_aod.presentation.theme.NeutralVariant30
import com.decoutkhanqindev.custom_aod.presentation.theme.RoundedCornerShape16dp
import com.decoutkhanqindev.custom_aod.presentation.theme.Theme
import kotlin.math.roundToInt

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
    icon: ImageVector,
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
                icon = icon,
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
    icon: ImageVector,
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
        leadingContent = { AppIconTile(icon = icon) },
        trailingContent = {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
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
    value: Int,
    valueRange: IntRange,
    step: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
) {
    val stepCount = (valueRange.last - valueRange.first) / step

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
            if (icon != null) {
                AppIconTile(
                    icon = icon,
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

        Slider(
            value = ((value - valueRange.first) / step).toFloat(),
            onValueChange = { onValueChange(valueRange.first + it.roundToInt() * step) },
            modifier = Modifier
                .padding(start = 52.dp)
                .semantics { contentDescription = label },
            valueRange = 0f..stepCount.toFloat(),
            steps = if (step == 1) 0 else stepCount - 1,
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
                icon = Icons.Outlined.LightMode,
                label = stringResource(R.string.opt_custom_brightness),
                isChecked = true,
                onCheckedChange = {},
            )

            SettingsSliderItem(
                label = stringResource(R.string.opt_brightness),
                valueText = stringResource(R.string.value_percent, 20),
                value = 20,
                valueRange = 1..100,
                step = 1,
                onValueChange = {},
            )

            SettingsDivider()

            SettingsSliderItem(
                label = stringResource(R.string.opt_timeout),
                valueText = stringResource(R.string.value_minutes, 30),
                value = 30,
                valueRange = 0..60,
                step = 5,
                onValueChange = {},
                icon = Icons.Outlined.Timer,
            )

            SettingsDivider()

            SettingsValueItem(
                icon = Icons.Outlined.Power,
                label = stringResource(R.string.opt_charging_rule),
                value = stringResource(R.string.charging_rule_always),
                onClick = {},
            )

            SettingsDivider()

            SettingsSwitchItem(
                icon = Icons.Outlined.DateRange,
                label = stringResource(R.string.opt_schedule),
                isChecked = true,
                onCheckedChange = {},
                description = stringResource(R.string.opt_auto_dim_desc),
            )
        }
    }
}
