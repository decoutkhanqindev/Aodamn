package com.decoutkhanqindev.custom_aod.presentation.screens.customize.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.AppTopBar
import com.decoutkhanqindev.custom_aod.presentation.components.onboarding.AppOnboardingTopBar
import com.decoutkhanqindev.custom_aod.presentation.model.onboarding.OnboardingStepValue
import com.decoutkhanqindev.custom_aod.presentation.screens.customize.state.CustomizeIntent
import com.decoutkhanqindev.custom_aod.presentation.theme.Theme

@Composable
fun CustomizeTopBar(
    isFirstOpen: Boolean,
    isApplying: Boolean,
    onIntent: (CustomizeIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (isFirstOpen) {
        AppOnboardingTopBar(
            step = OnboardingStepValue.CUSTOMIZE,
            modifier = modifier,
            onNavigateBack = { onIntent(CustomizeIntent.NavigateBack) },
            actions = {
                GuideButton(onIntent = onIntent)

                TextButton(
                    onClick = { onIntent(CustomizeIntent.SkipCustomization) },
                    enabled = !isApplying,
                ) {
                    Text(text = stringResource(R.string.customize_skip))
                }
            },
        )
    } else {
        AppTopBar(
            title = stringResource(R.string.customize_title),
            modifier = modifier,
            onNavigateBack = { onIntent(CustomizeIntent.NavigateBack) },
            actions = { GuideButton(onIntent = onIntent) },
        )
    }
}

@Composable
private fun GuideButton(onIntent: (CustomizeIntent) -> Unit) {
    IconButton(onClick = { onIntent(CustomizeIntent.Guide.ShowGuide) }) {
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.HelpOutline,
            contentDescription = stringResource(R.string.customize_show_guide),
        )
    }
}

@Preview(widthDp = 360)
@Composable
private fun CustomizeTopBarPreview() {
    Theme {
        CustomizeTopBar(
            isFirstOpen = false,
            isApplying = false,
            onIntent = {},
        )
    }
}

@Preview(widthDp = 360)
@Composable
private fun CustomizeTopBarFirstOpenPreview() {
    Theme {
        CustomizeTopBar(
            isFirstOpen = true,
            isApplying = false,
            onIntent = {},
        )
    }
}
