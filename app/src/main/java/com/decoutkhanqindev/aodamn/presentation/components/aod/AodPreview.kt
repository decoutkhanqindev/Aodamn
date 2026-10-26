package com.decoutkhanqindev.aodamn.presentation.components.aod

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.model.customize.CustomizeDraftUiModel
import com.decoutkhanqindev.aodamn.presentation.model.customize.CustomizePreviewSamples
import com.decoutkhanqindev.aodamn.presentation.model.customize.CustomizePreviewSamples.Companion.toPreviewAodState
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState

@Composable
fun rememberAodPreview(
    draft: CustomizeDraftUiModel,
    isGlowing: Boolean = false,
    shownComponent: AodComponentValue? = null,
    isStill: Boolean = false,
): AodState {
    val samples = rememberAodPreviewSamples()

    return remember(draft, isGlowing, shownComponent, isStill, samples) {
        draft.toPreviewAodState(
            isGlowing = isGlowing,
            samples = samples,
            shownComponent = shownComponent,
            isStill = isStill,
        )
    }
}

@Composable
fun rememberAodPreviewSamples(): CustomizePreviewSamples {
    val notificationTitle = stringResource(R.string.customize_sample_notification_title)
    val notificationText = stringResource(R.string.customize_sample_notification_text)
    val eventTitle = stringResource(R.string.customize_sample_event_title)
    val mediaTitle = stringResource(R.string.customize_sample_media_title)
    val mediaArtist = stringResource(R.string.customize_sample_media_artist)
    val memo = stringResource(R.string.customize_sample_memo)

    return remember(notificationTitle, notificationText, eventTitle, mediaTitle, mediaArtist, memo) {
        CustomizePreviewSamples(
            notificationTitle = notificationTitle,
            notificationText = notificationText,
            eventTitle = eventTitle,
            mediaTitle = mediaTitle,
            mediaArtist = mediaArtist,
            memo = memo,
        )
    }
}
