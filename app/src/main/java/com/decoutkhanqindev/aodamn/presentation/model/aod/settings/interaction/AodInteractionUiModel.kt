package com.decoutkhanqindev.aodamn.presentation.model.aod.settings.interaction

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.data.local.datastore.model.AodInteractionPrefs
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableMap

@Immutable
data class AodInteractionUiModel(
    val actions: ImmutableMap<AodGestureValue, AodActionValue> = AodInteractionPrefs.DEFAULT.toUiActions(),
    val isAutoDimEnabled: Boolean = AodInteractionPrefs.DEFAULT.isAutoDimEnabled,
    val isRaiseToWakeEnabled: Boolean = AodInteractionPrefs.DEFAULT.isRaiseToWakeEnabled,
) {
    fun actionOf(gesture: AodGestureValue): AodActionValue = actions[gesture] ?: gesture.defaultAction
}

fun AodInteractionPrefs.toUiModel(): AodInteractionUiModel = AodInteractionUiModel(
    actions = toUiActions(),
    isAutoDimEnabled = isAutoDimEnabled,
    isRaiseToWakeEnabled = isRaiseToWakeEnabled,
)

fun AodInteractionPrefs.withAction(gesture: AodGestureValue, action: AodActionValue): AodInteractionPrefs =
    copy(actions = actions + (gesture.id to action.id))

private fun AodInteractionPrefs.toUiActions(): ImmutableMap<AodGestureValue, AodActionValue> =
    AodGestureValue.entries.associateWith { gesture ->
        AodActionValue.fromIdOrNull(actions[gesture.id]) ?: gesture.defaultAction
    }.toImmutableMap()
