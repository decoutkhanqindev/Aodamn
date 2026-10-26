package com.decoutkhanqindev.aodamn.presentation.model.customize

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.R

@Immutable
enum class CustomizeClusterValue(@param:StringRes val labelRes: Int) {
    APPEARANCE(labelRes = R.string.customize_cluster_appearance),
    DECOR(labelRes = R.string.customize_cluster_decor),
    INFO(labelRes = R.string.customize_cluster_info),
    EFFECT(labelRes = R.string.customize_cluster_effect),
}
