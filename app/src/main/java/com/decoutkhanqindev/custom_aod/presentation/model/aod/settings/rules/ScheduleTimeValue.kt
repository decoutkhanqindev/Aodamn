package com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.rules

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.custom_aod.R

@Immutable
enum class ScheduleTimeValue(
    @param:StringRes val labelRes: Int,
    @param:StringRes val descriptionRes: Int,
) {
    START(labelRes = R.string.opt_schedule_start, descriptionRes = R.string.opt_schedule_start_desc),
    END(labelRes = R.string.opt_schedule_end, descriptionRes = R.string.opt_schedule_end_desc),
}
