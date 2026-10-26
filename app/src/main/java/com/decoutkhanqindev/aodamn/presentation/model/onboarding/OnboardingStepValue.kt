package com.decoutkhanqindev.aodamn.presentation.model.onboarding

import androidx.compose.runtime.Immutable

@Immutable
enum class OnboardingStepValue {
    LANGUAGE,
    CUSTOMIZE,
    PERMISSION,
    ;

    val number: Int
        get() = ordinal + 1
}
