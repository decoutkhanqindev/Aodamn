package com.decoutkhanqindev.custom_aod.presentation.screens.aodtab.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
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
import com.decoutkhanqindev.custom_aod.presentation.components.AppBadge
import com.decoutkhanqindev.custom_aod.presentation.components.aod.AodThumbnail
import com.decoutkhanqindev.custom_aod.presentation.components.aod.rememberAodPreview
import com.decoutkhanqindev.custom_aod.presentation.components.onClick
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.custom_aod.presentation.model.customize.toThemePreviewDraft
import com.decoutkhanqindev.custom_aod.presentation.screens.aodtab.state.AodTabIntent
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyED
import com.decoutkhanqindev.custom_aod.presentation.theme.Mint
import com.decoutkhanqindev.custom_aod.presentation.theme.NeutralVariant30
import com.decoutkhanqindev.custom_aod.presentation.theme.RoundedCornerShape12dp
import com.decoutkhanqindev.custom_aod.presentation.theme.RoundedCornerShape16dp
import com.decoutkhanqindev.custom_aod.presentation.theme.Theme
import com.decoutkhanqindev.custom_aod.presentation.theme.TitleLarge
import com.decoutkhanqindev.custom_aod.presentation.theme.TitleMedium

// Chủ đề mặc định không nằm trong lưới: quay về nó bằng icon đặt lại ở tiêu đề.
private val ListedThemes = AodThemeValue.entries.filter { theme -> theme != AodThemeValue.DEFAULT }

// Lưới chủ đề trong LazyVerticalGrid của tab AOD: tiêu đề chiếm cả hàng, mỗi chủ đề một ô. Mỗi mục chỉ xem trước kiểu mặc định của chủ
// đề (không theo cấu hình user đã chỉnh; đồng hồ, ngày, khối chủ đề tự vẽ; không nền, thông báo, pin... của user) với tên căn giữa, không
// khung thẻ; chủ đề đang dùng viền mint và có nhãn "Đang dùng" phủ trên ảnh.
fun LazyGridScope.aodTabThemesSection(
    activeTheme: AodThemeValue,
    onIntent: (AodTabIntent) -> Unit,
) {
    item(
        key = "themes_title",
        span = { GridItemSpan(maxLineSpan) },
        contentType = "themes_title",
    ) {
        ThemesHeader(
            isResetVisible = activeTheme != AodThemeValue.DEFAULT,
            onResetClick = { onIntent(AodTabIntent.ShowThemeResetConfirm) },
            modifier = Modifier.padding(top = 8.dp),
        )
    }

    items(
        items = ListedThemes,
        key = { theme -> theme.id },
        contentType = { "theme" },
    ) { theme ->
        ThemeCard(
            theme = theme,
            isActive = theme == activeTheme,
            onClick = { onIntent(AodTabIntent.OpenTheme(theme)) },
        )
    }
}

// Tiêu đề "Chủ đề" và icon đặt lại về chủ đề mặc định (chỉ khi đang dùng chủ đề khác); hàng luôn cao 48dp để icon hiện / ẩn không làm
// lưới nhảy.
@Composable
private fun ThemesHeader(
    isResetVisible: Boolean,
    onResetClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.aod_tab_themes_title),
            modifier = Modifier
                .weight(1f)
                .semantics { heading() },
            color = GreyED,
            fontWeight = FontWeight.SemiBold,
            style = TitleLarge,
        )

        AnimatedVisibility(
            visible = isResetVisible,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            IconButton(onClick = onResetClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_counter_clockwise),
                    contentDescription = stringResource(R.string.aod_tab_theme_reset),
                    tint = GreyED,
                )
            }
        }
    }
}

@Composable
private fun ThemeCard(
    theme: AodThemeValue,
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val preview = rememberAodPreview(draft = remember(theme) { AodThemeUiModel(value = theme).toThemePreviewDraft() })

    val borderColor = animateColorAsState(
        targetValue = if (isActive) Mint else NeutralVariant30,
        label = "ThemeCardBorder",
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .onClick(shape = RoundedCornerShape16dp, action = onClick)
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Ảnh rộng đúng bằng ô, cao theo tỉ lệ màn hình.
        BoxWithConstraints(contentAlignment = Alignment.TopCenter) {
            val windowSize = LocalWindowInfo.current.containerSize
            AodThumbnail(
                state = preview,
                height = maxWidth * windowSize.height.coerceAtLeast(1) / windowSize.width.coerceAtLeast(1),
                shape = RoundedCornerShape12dp,
                borderColor = borderColor.value,
            )

            ActiveMark(
                isActive = isActive,
                modifier = Modifier.padding(top = 8.dp),
            )
        }

        Text(
            text = stringResource(theme.nameRes),
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
        AppBadge(text = stringResource(R.string.theme_in_use))
    }
}

@Preview(widthDp = 360, heightDp = 400)
@Composable
private fun AodTabThemesSectionPreview() {
    Theme {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            aodTabThemesSection(
                activeTheme = AodThemeValue.CLASSIC,
                onIntent = {},
            )
        }
    }
}
