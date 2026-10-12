package com.decoutkhanqindev.custom_aod.presentation.components.aod

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodComponentValue
import com.decoutkhanqindev.custom_aod.presentation.model.customize.CustomizeDraftUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.customize.CustomizePreviewSamples
import com.decoutkhanqindev.custom_aod.presentation.model.customize.CustomizePreviewSamples.Companion.toPreviewAodState
import com.decoutkhanqindev.custom_aod.presentation.screens.aod.state.AodState

@Composable
fun rememberAodPreview(
    draft: CustomizeDraftUiModel,
    isGlowing: Boolean = false,
    shownComponent: AodComponentValue? = null,
): AodState {
    val samples = rememberAodPreviewSamples()

    return remember(draft, isGlowing, shownComponent, samples) {
        draft.toPreviewAodState(
            isGlowing = isGlowing,
            samples = samples,
            shownComponent = shownComponent,
        )
    }
}

// Dữ liệu mẫu cố định (chữ lấy theo ngôn ngữ đang dùng, giờ 08:30) cho khung xem trước AOD và ảnh xem trước kiểu từng khối.
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
