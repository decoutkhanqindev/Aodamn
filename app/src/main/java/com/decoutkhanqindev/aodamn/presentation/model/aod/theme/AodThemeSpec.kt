package com.decoutkhanqindev.aodamn.presentation.model.aod.theme

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableSet

@Immutable
data class AodThemeTab(
    @param:DrawableRes val iconRes: Int,
    @param:StringRes val labelRes: Int,
    val options: ImmutableList<AodThemeOption<*>>,
    val block: AodBlockValue? = null,
)

@Immutable
interface AodOptionSpec {
    val tabs: ImmutableList<AodThemeTab>
}

private val AllComponents = AodComponentValue.entries.toImmutableSet()

@Immutable
interface AodThemeSpec : AodOptionSpec {
    val ownBlocks: ImmutableSet<AodBlockValue>

    val components: ImmutableSet<AodComponentValue>
        get() = AllComponents

    val hasNotificationContent: Boolean
        get() = AodBlockValue.STATUS !in ownBlocks

    val isLiveInPreview: Boolean
        get() = false
}

val AodOptionSpec.options: List<AodThemeOption<*>>
    get() = tabs.flatMap { tab -> tab.options }

fun AodOptionSpec.tabOf(block: AodBlockValue?): AodThemeTab? =
    block?.let { tabs.find { tab -> tab.block == block } }

fun AodOptionSpec.isDefault(config: AodThemeConfig): Boolean = options.all { option -> option.isDefault(config) }

fun AodThemeTab.isDefault(config: AodThemeConfig): Boolean = options.all { option -> option.isDefault(config) }

fun AodOptionSpec.isSame(first: AodThemeConfig, second: AodThemeConfig): Boolean =
    options.all { option -> option.isSame(first, second) }
