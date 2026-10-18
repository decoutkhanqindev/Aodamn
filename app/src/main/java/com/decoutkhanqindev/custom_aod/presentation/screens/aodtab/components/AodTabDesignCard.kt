package com.decoutkhanqindev.custom_aod.presentation.screens.aodtab.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.AppIconTile
import com.decoutkhanqindev.custom_aod.presentation.components.aod.AodThumbnail
import com.decoutkhanqindev.custom_aod.presentation.components.aod.rememberAodPreview
import com.decoutkhanqindev.custom_aod.presentation.components.onClick
import com.decoutkhanqindev.custom_aod.presentation.model.aod.appearance.WallpaperValue
import com.decoutkhanqindev.custom_aod.presentation.model.customize.CustomizeAppearanceUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.customize.CustomizeDecorUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.customize.CustomizeDraftUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.customize.CustomizeInfoUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.customize.CustomizeTabValue
import com.decoutkhanqindev.custom_aod.presentation.model.customize.enabledBlockTabs
import com.decoutkhanqindev.custom_aod.presentation.screens.aodtab.state.AodTabIntent
import com.decoutkhanqindev.custom_aod.presentation.theme.BodyMedium
import com.decoutkhanqindev.custom_aod.presentation.theme.BodySmall
import com.decoutkhanqindev.custom_aod.presentation.theme.Grey9A
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyB4
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyED
import com.decoutkhanqindev.custom_aod.presentation.theme.LabelMedium
import com.decoutkhanqindev.custom_aod.presentation.theme.Neutral12
import com.decoutkhanqindev.custom_aod.presentation.theme.Neutral17
import com.decoutkhanqindev.custom_aod.presentation.theme.RoundedCornerShape16dp
import com.decoutkhanqindev.custom_aod.presentation.theme.RoundedCornerShape8dp
import com.decoutkhanqindev.custom_aod.presentation.theme.Theme
import com.decoutkhanqindev.custom_aod.presentation.theme.TitleMedium
import kotlinx.collections.immutable.ImmutableList

@Composable
fun AodTabDesignCard(
    design: CustomizeDraftUiModel,
    isAodEnabled: Boolean,
    onIntent: (AodTabIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val preview = rememberAodPreview(draft = design, isStill = true)
    val blockTabs = remember(design) { design.enabledBlockTabs() }
    val summary = designSummary(design = design, blockCount = blockTabs.size)
    val previewAlpha by animateFloatAsState(
        targetValue = if (isAodEnabled) 1f else 0.4f,
        label = "DesignPreviewAlpha",
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .onClick(shape = RoundedCornerShape16dp) { onIntent(AodTabIntent.NavigateToCustomize) }
            .background(Neutral12)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AodThumbnail(
            state = preview,
            height = 112.dp,
            modifier = Modifier.alpha(previewAlpha),
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
                .animateContentSize(),
        ) {
            Text(
                text = stringResource(R.string.aod_tab_design_title),
                color = GreyED,
                fontWeight = FontWeight.SemiBold,
                style = TitleMedium,
            )

            Text(
                text = stringResource(R.string.aod_tab_design_theme, stringResource(design.appearance.clock.theme.value.nameRes)),
                modifier = Modifier.padding(top = 4.dp),
                color = Grey9A,
                style = BodyMedium,
            )

            Crossfade(targetState = blockTabs, label = "DesignBlocks") { tabs ->
                if (tabs.isNotEmpty()) {
                    DesignBlockIcons(
                        tabs = tabs,
                        modifier = Modifier.padding(top = 12.dp),
                    )
                }
            }

            Crossfade(targetState = summary, label = "DesignSummary") { text ->
                Text(
                    text = text,
                    modifier = Modifier.padding(top = 8.dp),
                    color = Grey9A,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = BodySmall,
                )
            }
        }

        val customizeLabel = stringResource(R.string.customize_title)

        AppIconTile(
            iconRes = R.drawable.ic_pencil_simple_duotone,
            modifier = Modifier.semantics { contentDescription = customizeLabel },
        )
    }
}

// Số khối đang bật và nền đang dùng, vd "7 khối · ảnh nền Sao".
@Composable
private fun designSummary(
    design: CustomizeDraftUiModel,
    blockCount: Int,
): String {
    val wallpaper = design.decor.wallpaper
    val blocks = if (blockCount == 0) {
        stringResource(R.string.aod_tab_design_clock_only)
    } else {
        pluralStringResource(R.plurals.aod_tab_design_blocks, blockCount, blockCount)
    }
    val background = when {
        wallpaper != null -> stringResource(R.string.aod_tab_design_wallpaper, stringResource(wallpaper.labelRes))
        design.decor.background != null -> stringResource(R.string.aod_tab_design_photo)
        else -> stringResource(R.string.aod_tab_design_black)
    }
    return stringResource(R.string.aod_tab_design_summary, blocks, background)
}

// Icon của từng khối như ở thanh tab Tùy chỉnh; không đủ chỗ cho mọi khối thì ô cuối là "+N". Dòng tóm tắt đã có số khối nên TalkBack bỏ
// qua hàng này.
@Composable
private fun DesignBlockIcons(
    tabs: ImmutableList<CustomizeTabValue>,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .clearAndSetSemantics { },
    ) {
        val capacity = ((maxWidth + 6.dp) / (28.dp + 6.dp)).toInt().coerceAtLeast(1)
        val shownCount = if (tabs.size <= capacity) tabs.size else capacity - 1

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            tabs.take(shownCount).forEach { tab ->
                key(tab) {
                    DesignBlockTile {
                        Icon(
                            painter = painterResource(tab.iconRes),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = GreyB4,
                        )
                    }
                }
            }

            if (shownCount < tabs.size) {
                DesignBlockTile {
                    Text(
                        text = stringResource(R.string.aod_tab_design_more, tabs.size - shownCount),
                        color = Grey9A,
                        maxLines = 1,
                        style = LabelMedium,
                    )
                }
            }
        }
    }
}

@Composable
private fun DesignBlockTile(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .size(28.dp)
            .clip(RoundedCornerShape8dp)
            .background(Neutral17),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Preview(widthDp = 360)
@Composable
private fun AodTabDesignCardPreview() {
    Theme {
        AodTabDesignCard(
            design = CustomizeDraftUiModel(),
            isAodEnabled = true,
            onIntent = {},
        )
    }
}

@Preview(widthDp = 360)
@Composable
private fun AodTabDesignCardBlocksPreview() {
    Theme {
        AodTabDesignCard(
            design = CustomizeDraftUiModel(
                appearance = CustomizeAppearanceUiModel(isDateEnabled = true, isBatteryEnabled = true),
                decor = CustomizeDecorUiModel(wallpaper = WallpaperValue.entries.first()),
                info = CustomizeInfoUiModel(
                    isNotificationIconsEnabled = true,
                    isCalendarEnabled = true,
                    isWeatherEnabled = true,
                    isMediaControlsEnabled = true,
                ),
            ),
            isAodEnabled = true,
            onIntent = {},
        )
    }
}
