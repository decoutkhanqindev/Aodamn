package com.decoutkhanqindev.custom_aod.presentation.screens.language

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.AppOptionRow
import com.decoutkhanqindev.custom_aod.presentation.components.AppTopBar
import com.decoutkhanqindev.custom_aod.presentation.components.onboarding.AppOnboardingFooter
import com.decoutkhanqindev.custom_aod.presentation.components.onboarding.AppOnboardingHeader
import com.decoutkhanqindev.custom_aod.presentation.components.onboarding.AppOnboardingTopBar
import com.decoutkhanqindev.custom_aod.presentation.model.language.LanguageUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.language.LanguageValue
import com.decoutkhanqindev.custom_aod.presentation.model.onboarding.OnboardingStepValue
import com.decoutkhanqindev.custom_aod.presentation.screens.language.state.LanguageIntent
import com.decoutkhanqindev.custom_aod.presentation.screens.language.state.LanguageState
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyED
import com.decoutkhanqindev.custom_aod.presentation.theme.HeadlineSmall
import com.decoutkhanqindev.custom_aod.presentation.theme.Neutral12
import com.decoutkhanqindev.custom_aod.presentation.theme.Theme
import kotlinx.collections.immutable.persistentListOf

@Composable
fun LanguageContent(
    state: LanguageState,
    onIntent: (LanguageIntent) -> Unit,
    bannerAd: @Composable () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (state.isFirstOpen) {
                AppOnboardingTopBar(step = OnboardingStepValue.LANGUAGE)
            } else {
                AppTopBar(
                    title = stringResource(R.string.language),
                    onNavigateBack = { onIntent(LanguageIntent.NavigateBack) },
                )
            }
        },
        containerColor = Color.Transparent,
        contentColor = GreyED,
        bottomBar = {
            AppOnboardingFooter(
                actionLabel = stringResource(if (state.isFirstOpen) R.string.action_continue else R.string.action_done),
                isActionEnabled = state.isConfirmEnabled,
                onAction = { onIntent(LanguageIntent.ConfirmLanguage) },
                ad = bannerAd,
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .selectableGroup(),
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (state.isFirstOpen) {
                item(key = OnboardingStepValue.LANGUAGE.name) {
                    AppOnboardingHeader(
                        title = stringResource(R.string.language_title),
                        subtitle = stringResource(R.string.language_subtitle),
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(
                                top = 8.dp,
                                bottom = 12.dp,
                            ),
                    )
                }
            }

            items(items = state.languages, key = { it.language.name }) { language ->
                LanguageOption(
                    language = language,
                    isSelected = language.language == state.selectedLanguage,
                    onClick = { onIntent(LanguageIntent.SelectLanguage(language.language)) },
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }
        }
    }
}

@Composable
private fun LanguageOption(
    language: LanguageUiModel,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppOptionRow(
        label = language.displayName,
        isSelected = isSelected,
        onClick = onClick,
        modifier = modifier,
        unselectedColor = Neutral12,
    ) {
        Box(
            modifier = Modifier.size(40.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = language.language.flag,
                style = HeadlineSmall,
            )
        }
    }
}

@Preview(widthDp = 360, heightDp = 720)
@Composable
private fun LanguageContentFirstOpenPreview() {
    Theme {
        LanguageContent(
            state = LanguageState(
                isFirstOpen = true,
                languages = persistentListOf(
                    LanguageUiModel(
                        language = LanguageValue.VIETNAMESE,
                        displayName = "Tiếng Việt"
                    ),
                    LanguageUiModel(language = LanguageValue.ENGLISH, displayName = "English"),
                ),
                selectedLanguage = LanguageValue.VIETNAMESE,
            ),
            onIntent = {},
            bannerAd = {},
        )
    }
}

@Preview(widthDp = 360, heightDp = 640)
@Composable
private fun LanguageContentFromMainPreview() {
    Theme {
        LanguageContent(
            state = LanguageState(
                languages = persistentListOf(
                    LanguageUiModel(
                        language = LanguageValue.VIETNAMESE,
                        displayName = "Tiếng Việt"
                    ),
                    LanguageUiModel(language = LanguageValue.ENGLISH, displayName = "English"),
                ),
                appliedLanguage = LanguageValue.ENGLISH,
                selectedLanguage = LanguageValue.VIETNAMESE,
            ),
            onIntent = {},
            bannerAd = {},
        )
    }
}
