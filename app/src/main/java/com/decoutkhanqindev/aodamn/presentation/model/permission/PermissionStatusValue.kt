package com.decoutkhanqindev.aodamn.presentation.model.permission

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.R

@Immutable
enum class PermissionStatusValue(
    @param:DrawableRes val iconRes: Int,
    @param:StringRes val descriptionRes: Int,
) {
    GRANTED(iconRes = R.drawable.ic_check, descriptionRes = R.string.permission_state_granted),
    UNKNOWN(iconRes = R.drawable.ic_question, descriptionRes = R.string.permission_state_unknown),
    MISSING_REQUIRED(
        iconRes = R.drawable.ic_x,
        descriptionRes = R.string.permission_state_missing
    ),
    MISSING_OPTIONAL(
        iconRes = R.drawable.ic_minus,
        descriptionRes = R.string.permission_state_missing
    ),
}
