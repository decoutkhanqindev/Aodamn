package com.decoutkhanqindev.aodamn.presentation.components.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.presentation.components.AppSlider
import com.decoutkhanqindev.aodamn.presentation.components.onClick
import com.decoutkhanqindev.aodamn.presentation.theme.BodyLarge
import com.decoutkhanqindev.aodamn.presentation.theme.BodyMedium
import com.decoutkhanqindev.aodamn.presentation.theme.BodySmall
import com.decoutkhanqindev.aodamn.presentation.theme.Grey9A
import com.decoutkhanqindev.aodamn.presentation.theme.LabelMedium
import com.decoutkhanqindev.aodamn.presentation.theme.Mint
import com.decoutkhanqindev.aodamn.presentation.theme.RoundedCornerShape8dp

@Composable
fun AppSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title.uppercase(),
        modifier = modifier.padding(
            top = 28.dp,
            bottom = 4.dp,
        ),
        color = Mint,
        style = LabelMedium,
    )
}

@Composable
fun AppSectionLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier.padding(
            top = 12.dp,
            bottom = 4.dp,
        ),
        color = Grey9A,
        style = BodyMedium,
    )
}

@Composable
fun AppSwitchRow(
    label: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    isEnabled: Boolean = true,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .toggleable(
                value = isChecked,
                enabled = isEnabled,
                role = Role.Switch,
                onValueChange = onCheckedChange
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)
                .alpha(if (isEnabled) 1f else 0.38f),
        ) {
            Text(
                text = label,
                style = BodyLarge,
            )

            description?.let { text ->
                Text(
                    text = text,
                    modifier = Modifier.padding(top = 2.dp),
                    color = Grey9A,
                    style = BodySmall,
                )
            }
        }

        Switch(checked = isChecked, onCheckedChange = null, enabled = isEnabled)
    }
}

@Composable
fun AppValueRow(
    label: String,
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .onClick(shape = RoundedCornerShape8dp, action = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp),
        ) {
            Text(
                text = label,
                style = BodyLarge,
            )

            description?.let { text ->
                Text(
                    text = text,
                    modifier = Modifier.padding(top = 2.dp),
                    color = Grey9A,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = BodySmall,
                )
            }
        }

        Text(
            text = value,
            color = Mint,
            style = BodyLarge,
        )
    }
}

@Composable
fun AppSliderRow(
    label: String,
    progress: () -> Int,
    valueRange: IntRange,
    step: Int,
    onProgressChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(top = 12.dp)) {
        Text(
            text = label,
            style = BodyLarge,
        )

        AppSlider(
            progress = progress,
            valueRange = valueRange,
            step = step,
            onProgressChange = onProgressChange,
            label = label,
            valueText = label,
        )
    }
}
