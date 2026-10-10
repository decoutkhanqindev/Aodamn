package com.decoutkhanqindev.custom_aod.presentation.screens.settings.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.custom_aod.BuildConfig
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.model.language.LanguageUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.language.LanguageValue
import com.decoutkhanqindev.custom_aod.presentation.screens.settings.state.SettingsIntent
import com.decoutkhanqindev.custom_aod.presentation.theme.Black
import com.decoutkhanqindev.custom_aod.presentation.theme.BodySmall
import com.decoutkhanqindev.custom_aod.presentation.theme.Grey9A
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyED
import com.decoutkhanqindev.custom_aod.presentation.theme.NeutralVariant30
import com.decoutkhanqindev.custom_aod.presentation.theme.RoundedCornerShape16dp
import com.decoutkhanqindev.custom_aod.presentation.theme.Theme
import com.decoutkhanqindev.custom_aod.presentation.theme.TitleMedium

@Composable
fun SettingsAppSection(
    language: LanguageUiModel?,
    onIntent: (SettingsIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        SettingsGroup(title = stringResource(R.string.section_app)) {
            SettingsValueItem(
                icon = Icons.Outlined.Language,
                label = stringResource(R.string.language),
                value = language?.label.orEmpty(),
                onClick = { onIntent(SettingsIntent.NavigateToLanguage) },
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 40.dp,
                    bottom = 24.dp,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape16dp)
                    .background(Black)
                    .border(
                        width = 1.dp,
                        color = NeutralVariant30,
                        shape = RoundedCornerShape16dp,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    modifier = Modifier.requiredSize(84.dp),
                )
            }

            Text(
                text = stringResource(R.string.app_name),
                modifier = Modifier.padding(top = 12.dp),
                color = GreyED,
                fontWeight = FontWeight.SemiBold,
                style = TitleMedium,
            )

            Text(
                text = BuildConfig.VERSION_NAME,
                modifier = Modifier.padding(top = 2.dp),
                color = Grey9A,
                style = BodySmall,
            )
        }
    }
}

@Preview(widthDp = 360)
@Composable
private fun SettingsAppSectionPreview() {
    Theme {
        SettingsAppSection(
            language = LanguageUiModel(language = LanguageValue.ENGLISH, displayName = "English"),
            onIntent = {},
        )
    }
}
