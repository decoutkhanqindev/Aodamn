package com.decoutkhanqindev.aodamn.presentation.components.onboarding

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.AppPrimaryButton
import com.decoutkhanqindev.aodamn.presentation.components.AppTopBar
import com.decoutkhanqindev.aodamn.presentation.model.onboarding.OnboardingStepValue
import com.decoutkhanqindev.aodamn.presentation.theme.BodyLarge
import com.decoutkhanqindev.aodamn.presentation.theme.BodySmall
import com.decoutkhanqindev.aodamn.presentation.theme.Grey9A
import com.decoutkhanqindev.aodamn.presentation.theme.GreyED
import com.decoutkhanqindev.aodamn.presentation.theme.HeadlineMedium
import com.decoutkhanqindev.aodamn.presentation.theme.LabelLarge
import com.decoutkhanqindev.aodamn.presentation.theme.Mint
import com.decoutkhanqindev.aodamn.presentation.theme.MintAlpha12
import com.decoutkhanqindev.aodamn.presentation.theme.Theme

@Composable
fun AppOnboardingTopBar(
    step: OnboardingStepValue,
    modifier: Modifier = Modifier,
    onNavigateBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    AppTopBar(
        title = {
            Text(
                text = stringResource(
                    R.string.onboarding_step,
                    step.number,
                    OnboardingStepValue.entries.size
                ),
                modifier = Modifier
                    .background(
                        color = MintAlpha12,
                        shape = CircleShape,
                    )
                    .padding(
                        horizontal = 12.dp,
                        vertical = 4.dp,
                    ),
                color = Mint,
                style = LabelLarge,
            )
        },
        modifier = modifier,
        onNavigateBack = onNavigateBack,
        actions = actions,
    )
}

@Composable
fun AppOnboardingHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            modifier = Modifier.semantics { heading() },
            color = GreyED,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            style = HeadlineMedium,
        )

        Text(
            text = subtitle,
            modifier = Modifier.padding(top = 8.dp),
            color = Grey9A,
            textAlign = TextAlign.Center,
            style = BodyLarge,
        )
    }
}

@Composable
fun AppOnboardingFooter(
    actionLabel: String,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
    isActionEnabled: Boolean = true,
    hint: String? = null,
    @DrawableRes actionIconRes: Int? = null,
    ad: @Composable () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AnimatedVisibility(visible = hint != null) {
                Text(
                    text = hint.orEmpty(),
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = Grey9A,
                    textAlign = TextAlign.Center,
                    style = BodySmall,
                )
            }

            AppPrimaryButton(
                text = actionLabel,
                onClick = onAction,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp),
                iconRes = actionIconRes,
                isEnabled = isActionEnabled,
            )
        }

        ad()
    }
}

@Preview(widthDp = 360)
@Composable
private fun AppOnboardingTopBarPreview() {
    Theme {
        AppOnboardingTopBar(
            step = OnboardingStepValue.PERMISSION,
            onNavigateBack = {},
        )
    }
}

@Preview(widthDp = 360)
@Composable
private fun AppOnboardingHeaderPreview() {
    Theme {
        AppOnboardingHeader(
            title = "Choose your language",
            subtitle = "You can change it later on the home screen.",
        )
    }
}

@Preview(widthDp = 360)
@Composable
private fun AppOnboardingFooterDisabledPreview() {
    Theme {
        AppOnboardingFooter(
            actionLabel = "Allow next permission",
            onAction = {},
            hint = "2 permissions left, then you are in",
        )
    }
}
