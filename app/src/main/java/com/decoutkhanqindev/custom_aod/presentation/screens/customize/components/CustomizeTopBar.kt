package com.decoutkhanqindev.custom_aod.presentation.screens.customize.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.AppTextButton
import com.decoutkhanqindev.custom_aod.presentation.components.AppTopBar
import com.decoutkhanqindev.custom_aod.presentation.components.editor.AppPreviewOrientationButton
import com.decoutkhanqindev.custom_aod.presentation.components.onboarding.AppOnboardingTopBar
import com.decoutkhanqindev.custom_aod.presentation.model.customize.CustomizeGuideStepValue
import com.decoutkhanqindev.custom_aod.presentation.model.onboarding.OnboardingStepValue
import com.decoutkhanqindev.custom_aod.presentation.screens.customize.state.CustomizeIntent
import com.decoutkhanqindev.custom_aod.presentation.theme.Theme

@Composable
fun CustomizeTopBar(
    isFirstOpen: Boolean,
    isApplying: Boolean,
    isLandscapePreview: Boolean,
    onIntent: (CustomizeIntent) -> Unit,
    modifier: Modifier = Modifier,
    onGuideTargetPositioned: (CustomizeGuideStepValue, Rect) -> Unit = { _, _ -> },
) {
    val rotateModifier = Modifier.onGloballyPositioned { coordinates ->
        onGuideTargetPositioned(CustomizeGuideStepValue.ROTATE, coordinates.boundsInWindow())
    }

    if (isFirstOpen) {
        AppOnboardingTopBar(
            step = OnboardingStepValue.CUSTOMIZE,
            modifier = modifier,
            onNavigateBack = { onIntent(CustomizeIntent.NavigateBack) },
            actions = {
                AppPreviewOrientationButton(
                    isLandscape = isLandscapePreview,
                    onClick = { onIntent(CustomizeIntent.ToggleLandscapePreview) },
                    modifier = rotateModifier,
                )

                GuideButton(onIntent = onIntent)

                AppTextButton(
                    text = stringResource(R.string.customize_skip),
                    onClick = { onIntent(CustomizeIntent.SkipCustomization) },
                    isEnabled = !isApplying,
                )
            },
        )
    } else {
        AppTopBar(
            title = stringResource(R.string.customize_title),
            modifier = modifier,
            onNavigateBack = { onIntent(CustomizeIntent.NavigateBack) },
            actions = {
                AppPreviewOrientationButton(
                    isLandscape = isLandscapePreview,
                    onClick = { onIntent(CustomizeIntent.ToggleLandscapePreview) },
                    modifier = rotateModifier,
                )

                GuideButton(onIntent = onIntent)
            },
        )
    }
}

@Composable
private fun GuideButton(onIntent: (CustomizeIntent) -> Unit) {
    IconButton(onClick = { onIntent(CustomizeIntent.Guide.ShowGuide) }) {
        Icon(
            painter = painterResource(R.drawable.ic_question),
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
            isLandscapePreview = false,
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
            isLandscapePreview = false,
            onIntent = {},
        )
    }
}
