package com.decoutkhanqindev.custom_aod.presentation.screens.aodtab.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.AppBadge
import com.decoutkhanqindev.custom_aod.presentation.components.AppIconTile
import com.decoutkhanqindev.custom_aod.presentation.theme.BodyLarge
import com.decoutkhanqindev.custom_aod.presentation.theme.BodyMedium
import com.decoutkhanqindev.custom_aod.presentation.theme.Grey9A
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyED
import com.decoutkhanqindev.custom_aod.presentation.theme.Neutral12
import com.decoutkhanqindev.custom_aod.presentation.theme.NeutralVariant30
import com.decoutkhanqindev.custom_aod.presentation.theme.RoundedCornerShape16dp
import com.decoutkhanqindev.custom_aod.presentation.theme.RoundedCornerShape8dp
import com.decoutkhanqindev.custom_aod.presentation.theme.Theme
import com.decoutkhanqindev.custom_aod.presentation.theme.TitleLarge

// TODO: Thay lưới giữ chỗ bằng danh sách chủ đề / kiểu AOD thật khi làm phần theme.
@Composable
fun AodTabThemesSection(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.aod_tab_themes_title),
                modifier = Modifier.semantics { heading() },
                color = GreyED,
                fontWeight = FontWeight.SemiBold,
                style = TitleLarge,
            )

            AppBadge(
                text = stringResource(R.string.coming_soon),
                modifier = Modifier.padding(start = 10.dp),
            )
        }

        Column(
            modifier = Modifier
                .padding(top = 12.dp)
                .clearAndSetSemantics { },
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ThemeSkeleton(modifier = Modifier.weight(1f)) {
                    SkeletonBlock(width = 46.dp, height = 28.dp, shape = RoundedCornerShape8dp)
                    SkeletonBlock(width = 46.dp, height = 28.dp, shape = RoundedCornerShape8dp)
                    SkeletonBlock(
                        width = 34.dp,
                        height = 6.dp,
                        modifier = Modifier.padding(top = 6.dp),
                    )
                }

                ThemeSkeleton(modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(58.dp)
                            .border(
                                width = 4.dp,
                                color = NeutralVariant30,
                                shape = CircleShape,
                            ),
                    )
                    SkeletonBlock(
                        width = 40.dp,
                        height = 6.dp,
                        modifier = Modifier.padding(top = 6.dp),
                    )
                }

                ThemeSkeleton(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column(
                        modifier = Modifier.padding(top = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        SkeletonBlock(width = 60.dp, height = 22.dp, shape = RoundedCornerShape8dp)
                        SkeletonBlock(width = 38.dp, height = 6.dp)
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        SkeletonBlock(width = 56.dp, height = 6.dp)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(CircleShape)
                                .background(NeutralVariant30),
                        )
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ThemeSkeleton(modifier = Modifier.weight(1f)) {
                    SkeletonBlock(width = 66.dp, height = 34.dp, shape = RoundedCornerShape8dp)
                    SkeletonBlock(width = 44.dp, height = 6.dp)
                    SkeletonBlock(width = 30.dp, height = 6.dp)
                }

                ThemeSkeleton(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom),
                ) {
                    SkeletonBlock(width = 14.dp, height = 86.dp, shape = RoundedCornerShape8dp)
                    SkeletonBlock(width = 30.dp, height = 6.dp)
                }

                ThemeSkeleton(modifier = Modifier.weight(1f)) {
                    SkeletonBlock(width = 44.dp, height = 44.dp)
                    SkeletonBlock(
                        width = 52.dp,
                        height = 6.dp,
                        modifier = Modifier.padding(top = 6.dp),
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = NeutralVariant30,
                    shape = RoundedCornerShape16dp,
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppIconTile(iconRes = R.drawable.ic_sparkle_duotone)

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp),
            ) {
                Text(
                    text = stringResource(R.string.aod_tab_themes_soon_title),
                    color = GreyED,
                    fontWeight = FontWeight.Medium,
                    style = BodyLarge,
                )

                Text(
                    text = stringResource(R.string.aod_tab_themes_soon_desc),
                    modifier = Modifier.padding(top = 4.dp),
                    color = Grey9A,
                    style = BodyMedium,
                )
            }
        }
    }
}

@Composable
private fun ThemeSkeleton(
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .aspectRatio(9f / 17f)
            .clip(RoundedCornerShape16dp)
            .background(Neutral12)
            .padding(14.dp),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        content = content,
    )
}

@Composable
private fun SkeletonBlock(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
) {
    Box(
        modifier = modifier
            .size(
                width = width,
                height = height,
            )
            .clip(shape)
            .background(NeutralVariant30),
    )
}

@Preview(widthDp = 360)
@Composable
private fun AodTabThemesSectionPreview() {
    Theme {
        AodTabThemesSection()
    }
}
