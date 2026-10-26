package com.decoutkhanqindev.aodamn.presentation.screens.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.AppBottomAdSlot
import com.decoutkhanqindev.aodamn.presentation.components.AppLottie
import com.decoutkhanqindev.aodamn.presentation.components.AppSlider
import com.decoutkhanqindev.aodamn.presentation.theme.BodyMedium
import com.decoutkhanqindev.aodamn.presentation.theme.BodySmall
import com.decoutkhanqindev.aodamn.presentation.theme.Grey9A
import com.decoutkhanqindev.aodamn.presentation.theme.GreyED
import com.decoutkhanqindev.aodamn.presentation.theme.HeadlineMedium
import com.decoutkhanqindev.aodamn.presentation.theme.Mint
import com.decoutkhanqindev.aodamn.presentation.theme.Theme
import kotlin.math.roundToInt

@Composable
fun SplashContent(bannerAd: @Composable () -> Unit) {
    val enterDuration = 600
    val titleState = remember { MutableTransitionState(false).apply { targetState = true } }
    val loadingProgress = remember { Animatable(0f) }

    LaunchedEffect(loadingProgress) {
        loadingProgress.animateTo(
            targetValue = LOADING_PROGRESS_CAP.toFloat(),
            animationSpec = tween(durationMillis = 5000),
        )
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 32.dp)
                .padding(bottom = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AppLottie(
                resId = R.raw.lottie_device_edge_light,
                modifier = Modifier.size(300.dp),
            )

            AnimatedVisibility(
                visibleState = titleState,
                modifier = Modifier.offset(y = (-32).dp),
                enter = fadeIn(animationSpec = tween(durationMillis = enterDuration)) +
                        expandVertically(
                            animationSpec = tween(durationMillis = enterDuration),
                            expandFrom = Alignment.Top,
                        ),
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.app_name),
                        color = GreyED,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        style = HeadlineMedium,
                    )

                    Text(
                        text = stringResource(R.string.splash_tagline),
                        modifier = Modifier.padding(top = 8.dp),
                        color = Grey9A,
                        textAlign = TextAlign.Center,
                        style = BodyMedium,
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
        ) {
            SplashLoading(progress = { loadingProgress.value })

            AppBottomAdSlot(background = Color.Transparent, content = bannerAd)
        }
    }
}

@Composable
private fun SplashLoading(progress: () -> Float) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.loading),
                modifier = Modifier.weight(1f),
                color = Grey9A,
                style = BodyMedium,
            )

            SplashLoadingPercent(progress = progress)
        }

        AppSlider(
            progress = { progress().roundToInt() },
            valueRange = 0..LOADING_PROGRESS_MAX,
            step = 1,
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(R.string.loading),
            isThumbVisible = false,
        )

        Text(
            text = stringResource(R.string.may_contain_ads),
            color = Grey9A,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center,
            style = BodySmall,
        )
    }
}

@Composable
private fun SplashLoadingPercent(progress: () -> Float) {
    val currentProgress by rememberUpdatedState(progress)
    val percent by remember {
        derivedStateOf { (currentProgress() * 100 / LOADING_PROGRESS_MAX).roundToInt() }
    }

    Text(
        text = stringResource(R.string.value_percent, percent),
        color = Mint,
        fontWeight = FontWeight.Medium,
        style = BodyMedium.copy(fontFeatureSettings = "tnum"),
    )
}

private const val LOADING_PROGRESS_MAX = 1000
private const val LOADING_PROGRESS_CAP = 990

@Preview(widthDp = 360, heightDp = 720)
@Composable
private fun SplashContentPreview() {
    Theme {
        SplashContent(bannerAd = {})
    }
}
