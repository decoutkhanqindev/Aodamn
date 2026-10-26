package com.decoutkhanqindev.aodamn.presentation.components.aod.editor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.AodShapeOutline
import com.decoutkhanqindev.aodamn.presentation.components.settings.AppSectionLabel
import com.decoutkhanqindev.aodamn.presentation.components.settings.AppSliderRow
import com.decoutkhanqindev.aodamn.presentation.components.settings.AppSwitchRow
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.ClockFontValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodChoiceOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodToggleOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.pebble.AodShapeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.pebble.PebbleThemeSpec
import com.decoutkhanqindev.aodamn.presentation.theme.Black
import com.decoutkhanqindev.aodamn.presentation.theme.Grey9A
import com.decoutkhanqindev.aodamn.presentation.theme.LabelMedium
import com.decoutkhanqindev.aodamn.presentation.theme.Mint
import com.decoutkhanqindev.aodamn.presentation.theme.NeutralVariant30
import com.decoutkhanqindev.aodamn.presentation.theme.NeutralVariant60
import com.decoutkhanqindev.aodamn.presentation.theme.RoundedCornerShape12dp
import com.decoutkhanqindev.aodamn.presentation.theme.Theme

@Composable
fun AodThemeOptionList(
    tab: AodThemeTab,
    config: AodThemeConfig,
    onOptionPreview: (raws: Map<String, Int?>) -> Unit,
    onOptionChange: (raws: Map<String, Int?>) -> Unit,
) {
    val colorOptions = remember(tab) { tab.options.filterIsInstance<AodColorOption>() }
    val onRawChange = { key: String, raw: Int -> onOptionChange(mapOf(key to raw)) }

    Column {
        tab.options.forEach { option ->
            key(option.key) {
                when {
                    option !is AodColorOption || colorOptions.size == 1 ->
                        ThemeOptionControl(
                            option = option,
                            config = config,
                            onOptionChange = onRawChange,
                            onColorPreview = onOptionPreview,
                            onColorChange = onOptionChange,
                        )

                    option == colorOptions.first() ->
                        ColorGroupControl(
                            options = colorOptions,
                            config = config,
                            onColorPreview = onOptionPreview,
                            onColorChange = onOptionChange,
                        )
                }
            }
        }
    }
}

@Composable
private fun ThemeOptionControl(
    option: AodThemeOption<*>,
    config: AodThemeConfig,
    onOptionChange: (key: String, raw: Int) -> Unit,
    onColorPreview: (raws: Map<String, Int?>) -> Unit,
    onColorChange: (raws: Map<String, Int?>) -> Unit,
) {
    when (option) {
        is AodChoiceOption<*> -> ChoiceControl(option = option, config = config, onOptionChange = onOptionChange)

        is AodColorOption -> {
            AppSectionLabel(text = stringResource(option.labelRes))

            AodColorPicker(
                option = option,
                config = config,
                onPreview = onColorPreview,
                onChange = onColorChange,
            )
        }

        is AodRangeOption -> if (option.autoLabelRes != null) {
            AutoRangeControl(option = option, config = config, onOptionChange = onOptionChange)
        } else {
            AppSliderRow(
                label = stringResource(option.labelRes, config[option]),
                progress = { config[option] },
                valueRange = option.range,
                step = option.step,
                onProgressChange = { value -> onOptionChange(option.key, value) },
            )
        }

        is AodToggleOption -> AppSwitchRow(
            label = stringResource(option.labelRes),
            isChecked = config[option],
            onCheckedChange = { isChecked ->
                onOptionChange(option.key, option.encode(isChecked))
            },
        )
    }
}

@Composable
private fun AutoRangeControl(
    option: AodRangeOption,
    config: AodThemeConfig,
    onOptionChange: (key: String, raw: Int) -> Unit,
) {
    val isAuto = config[option] == AodRangeOption.AUTO
    val pinned = { config[option].coerceIn(option.range) }

    AppSwitchRow(
        label = stringResource(option.autoLabelRes ?: option.labelRes),
        isChecked = isAuto,
        onCheckedChange = { isChecked ->
            onOptionChange(option.key, if (isChecked) AodRangeOption.AUTO else (option.range.first + option.range.last) / 2)
        },
    )

    AnimatedVisibility(visible = !isAuto) {
        AppSliderRow(
            label = stringResource(option.labelRes, pinned()),
            progress = pinned,
            valueRange = option.range,
            step = option.step,
            onProgressChange = { value -> onOptionChange(option.key, value) },
        )
    }
}

@Composable
private fun ChoiceControl(
    option: AodChoiceOption<*>,
    config: AodThemeConfig,
    onOptionChange: (key: String, raw: Int) -> Unit,
) {
    val selectedCode = option.decode(config.values[option.key]).code
    val onSelect = { code: Int -> onOptionChange(option.key, code) }

    AppSectionLabel(text = stringResource(option.labelRes))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        option.entries.forEach { entry ->
            val isSelected = entry.code == selectedCode
            val preview = remember(entry) { aodChoicePreview(entry) }
            when {
                entry is AodShapeValue ->
                    ShapeChoice(shape = entry, isSelected = isSelected, onClick = { onSelect(entry.code) })

                preview != null -> PreviewChoice(
                    label = stringResource(entry.labelRes),
                    labelFontFamily = (entry as? ClockFontValue)?.fontFamily,
                    isSelected = isSelected,
                    onClick = { onSelect(entry.code) },
                    preview = preview,
                )

                else -> FilterChip(
                    selected = isSelected,
                    onClick = { onSelect(entry.code) },
                    label = { Text(text = stringResource(entry.labelRes)) },
                )
            }
        }
    }
}

@Composable
private fun ColorGroupControl(
    options: List<AodColorOption>,
    config: AodThemeConfig,
    onColorPreview: (raws: Map<String, Int?>) -> Unit,
    onColorChange: (raws: Map<String, Int?>) -> Unit,
) {
    var selectedKey by rememberSaveable { mutableStateOf(options.first().key) }
    val selected = options.find { it.key == selectedKey } ?: options.first()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        options.forEach { option ->
            FilterChip(
                selected = option == selected,
                onClick = { selectedKey = option.key },
                label = { Text(text = stringResource(option.labelRes)) },
                leadingIcon = {
                    val gradient = config.gradientOf(option)
                    if (gradient != null) {
                        AodGradientDots(colors = gradient.colors, dotSize = 12.dp)
                    } else {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .background(color = config[option], shape = CircleShape)
                                .border(width = 1.dp, color = NeutralVariant60, shape = CircleShape),
                        )
                    }
                },
            )
        }
    }

    key(selected.key) {
        AodColorPicker(
            option = selected,
            config = config,
            onPreview = onColorPreview,
            onChange = onColorChange,
        )
    }
}

@Composable
private fun PreviewChoice(
    label: String,
    labelFontFamily: FontFamily?,
    isSelected: Boolean,
    onClick: () -> Unit,
    preview: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape12dp)
            .selectable(
                selected = isSelected,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(width = 112.dp, height = 64.dp)
                .clip(RoundedCornerShape12dp)
                .background(Black)
                .border(
                    width = if (isSelected) 1.5.dp else 1.dp,
                    color = if (isSelected) Mint else NeutralVariant30,
                    shape = RoundedCornerShape12dp,
                )
                .clearAndSetSemantics { },
            contentAlignment = Alignment.Center,
        ) {
            preview()

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .pointerInput(Unit) { },
            )
        }

        Text(
            text = label,
            modifier = Modifier.padding(top = 6.dp),
            color = if (isSelected) Mint else Grey9A,
            fontFamily = labelFontFamily,
            style = LabelMedium,
        )
    }
}

@Composable
private fun ShapeChoice(
    shape: AodShapeValue,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val outline = remember(shape) { AodShapeOutline(shape) }

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape12dp)
            .selectable(
                selected = isSelected,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(color = if (isSelected) Mint else NeutralVariant60, shape = outline),
        )

        Text(
            text = stringResource(shape.labelRes),
            modifier = Modifier.padding(top = 6.dp),
            color = if (isSelected) Mint else Grey9A,
            style = LabelMedium,
        )
    }
}

@Preview(widthDp = 360)
@Composable
private fun AodThemeOptionListPreview() {
    Theme {
        AodThemeOptionList(
            tab = PebbleThemeSpec.tabs.first(),
            config = AodThemeConfig(),
            onOptionPreview = { },
            onOptionChange = { },
        )
    }
}
