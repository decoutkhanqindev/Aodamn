package com.decoutkhanqindev.custom_aod.presentation.components.aod

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.model.customize.CustomizeDraftUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.customize.CustomizePreviewSamples
import com.decoutkhanqindev.custom_aod.presentation.model.customize.CustomizePreviewSamples.Companion.toPreviewAodState
import com.decoutkhanqindev.custom_aod.presentation.screens.aod.state.AodState

@Composable
fun rememberAodPreview(
    draft: CustomizeDraftUiModel,
    nowMillis: Long,
    isGlowing: Boolean = false,
): AodState {
    val samples = rememberPreviewSamples()

    return remember(draft, nowMillis, isGlowing, samples) {
        draft.toPreviewAodState(
            nowMillis = nowMillis,
            isGlowing = isGlowing,
            samples = samples,
        )
    }
}

@Composable
private fun rememberPreviewSamples(): CustomizePreviewSamples {
    val notificationTitle = stringResource(R.string.customize_sample_notification_title)
    val notificationText = stringResource(R.string.customize_sample_notification_text)
    val eventTitle = stringResource(R.string.customize_sample_event_title)
    val mediaTitle = stringResource(R.string.customize_sample_media_title)
    val mediaArtist = stringResource(R.string.customize_sample_media_artist)

    return remember(notificationTitle, notificationText, eventTitle, mediaTitle, mediaArtist) {
        CustomizePreviewSamples(
            notificationTitle = notificationTitle,
            notificationText = notificationText,
            eventTitle = eventTitle,
            mediaTitle = mediaTitle,
            mediaArtist = mediaArtist,
        )
    }
}
