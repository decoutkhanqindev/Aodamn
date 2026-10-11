package com.decoutkhanqindev.custom_aod.presentation.screens.aodtab.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.aod.AodThumbnail
import com.decoutkhanqindev.custom_aod.presentation.components.aod.rememberAodPreview
import com.decoutkhanqindev.custom_aod.presentation.components.onClick
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.custom_aod.presentation.model.customize.CustomizeDraftUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.customize.withTheme
import com.decoutkhanqindev.custom_aod.presentation.screens.aodtab.state.AodTabIntent
import com.decoutkhanqindev.custom_aod.presentation.theme.Black
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyED
import com.decoutkhanqindev.custom_aod.presentation.theme.Mint
import com.decoutkhanqindev.custom_aod.presentation.theme.NeutralVariant30
import com.decoutkhanqindev.custom_aod.presentation.theme.RoundedCornerShape12dp
import com.decoutkhanqindev.custom_aod.presentation.theme.RoundedCornerShape16dp
import com.decoutkhanqindev.custom_aod.presentation.theme.Theme
import com.decoutkhanqindev.custom_aod.presentation.theme.TitleLarge
import com.decoutkhanqindev.custom_aod.presentation.theme.TitleMedium
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

// Lưới chủ đề, 2 cột: mỗi mục là AOD thu nhỏ của chủ đề đó (cấu hình đã lưu + phần chung đang dùng) với tên căn giữa, không khung
// thẻ; chủ đề đang dùng viền mint và có dấu tích phủ góc trên.
@Composable
fun AodTabThemesSection(
    themes: ImmutableList<AodThemeUiModel>,
    activeTheme: AodThemeValue,
    design: CustomizeDraftUiModel,
    nowMillis: Long,
    onIntent: (AodTabIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.aod_tab_themes_title),
            modifier = Modifier.semantics { heading() },
            color = GreyED,
            fontWeight = FontWeight.SemiBold,
            style = TitleLarge,
        )

        Column(
            modifier = Modifier.padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            themes.chunked(2).forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    row.forEach { theme ->
                        ThemeCard(
                            theme = theme,
                            isActive = theme.value == activeTheme,
                            design = design,
                            nowMillis = nowMillis,
                            onClick = { onIntent(AodTabIntent.OpenTheme(theme.value)) },
                            modifier = Modifier.weight(1f),
                        )
                    }
                    if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun ThemeCard(
    theme: AodThemeUiModel,
    isActive: Boolean,
    design: CustomizeDraftUiModel,
    nowMillis: Long,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val preview = rememberAodPreview(
        draft = design.withTheme(theme),
        nowMillis = nowMillis,
    )

    val borderColor = animateColorAsState(
        targetValue = if (isActive) Mint else NeutralVariant30,
        label = "ThemeCardBorder",
    )

    Column(
        modifier = modifier
            .onClick(shape = RoundedCornerShape16dp, action = onClick)
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box {
            AodThumbnail(
                state = preview,
                height = 240.dp,
                shape = RoundedCornerShape12dp,
                borderColor = borderColor.value,
            )

            ActiveMark(
                isActive = isActive,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
            )
        }

        Text(
            text = stringResource(theme.value.nameRes),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            color = GreyED,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = TitleMedium,
        )
    }
}

@Composable
private fun ActiveMark(
    isActive: Boolean,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = isActive,
        modifier = modifier,
        enter = scaleIn() + fadeIn(),
        exit = scaleOut() + fadeOut(),
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_check_circle_fill),
            contentDescription = stringResource(R.string.theme_in_use),
            modifier = Modifier
                .size(24.dp)
                .background(color = Black, shape = CircleShape),
            tint = Mint,
        )
    }
}

@Preview(widthDp = 360)
@Composable
private fun AodTabThemesSectionPreview() {
    Theme {
        AodTabThemesSection(
            themes = AodThemeValue.entries.map { AodThemeUiModel(value = it) }.toImmutableList(),
            activeTheme = AodThemeValue.CLASSIC,
            design = CustomizeDraftUiModel(),
            nowMillis = System.currentTimeMillis(),
            onIntent = {},
        )
    }
}
