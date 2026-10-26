package com.decoutkhanqindev.aodamn.presentation.components.aod.editor

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodGradient
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodGradientDirection
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.GradientSwatchValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.theme.Grey9A
import com.decoutkhanqindev.aodamn.presentation.theme.GreyED
import com.decoutkhanqindev.aodamn.presentation.theme.LabelLarge
import com.decoutkhanqindev.aodamn.presentation.theme.LabelMedium
import com.decoutkhanqindev.aodamn.presentation.theme.Mint
import com.decoutkhanqindev.aodamn.presentation.theme.MintAlpha12
import com.decoutkhanqindev.aodamn.presentation.theme.Neutral12
import com.decoutkhanqindev.aodamn.presentation.theme.Neutral17
import com.decoutkhanqindev.aodamn.presentation.theme.NeutralVariant30
import com.decoutkhanqindev.aodamn.presentation.theme.NeutralVariant60
import com.decoutkhanqindev.aodamn.presentation.theme.RoundedCornerShape12dp
import com.decoutkhanqindev.aodamn.presentation.theme.RoundedCornerShape24dp
import com.decoutkhanqindev.aodamn.presentation.theme.Theme

@Composable
fun AodColorPicker(
    option: AodColorOption,
    config: AodThemeConfig,
    onPreview: (raws: Map<String, Int?>) -> Unit,
    onChange: (raws: Map<String, Int?>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val color = config[option]
    val gradient = config.gradientOf(option)
    var lastGradient by remember(option.key) {
        mutableStateOf(gradient ?: option.defaultGradient ?: GradientSwatchValue.DEFAULT.gradient)
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        if (option.isGradientAllowed) {
            ColorModeSwitch(
                color = color,
                gradientColors = (gradient ?: lastGradient).colors,
                isGradient = gradient != null,
                onSelectSolid = {
                    if (gradient != null) {
                        lastGradient = gradient
                        onChange(option.encodeSolid(gradient.start))
                    }
                },
                onSelectGradient = { if (gradient == null) onChange(option.encodeGradient(lastGradient)) },
            )
        }

        if (gradient == null) {
            ClockColorPicker(
                color = color,
                onColorPreview = { picked -> onPreview(option.encodeSolid(picked)) },
                onColorChange = { picked -> onChange(option.encodeSolid(picked)) },
            )
        } else {
            GradientEditor(
                optionKey = option.key,
                gradient = gradient,
                onPreview = { edited -> onPreview(option.encodeGradient(edited)) },
                onChange = { edited -> onChange(option.encodeGradient(edited)) },
            )
        }
    }
}

@Composable
fun AodGradientDots(
    colors: List<Color>,
    modifier: Modifier = Modifier,
    dotSize: Dp = 14.dp,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(-dotSize * 0.4f),
    ) {
        colors.forEach { dot ->
            Box(
                modifier = Modifier
                    .size(dotSize)
                    .background(color = dot, shape = CircleShape)
                    .border(width = 1.dp, color = Neutral12, shape = CircleShape),
            )
        }
    }
}

@Composable
private fun ColorModeSwitch(
    color: Color,
    gradientColors: List<Color>,
    isGradient: Boolean,
    onSelectSolid: () -> Unit,
    onSelectGradient: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .clip(RoundedCornerShape24dp)
            .border(width = 1.dp, color = NeutralVariant30, shape = RoundedCornerShape24dp),
    ) {
        ColorModeHalf(
            label = stringResource(R.string.color_mode_solid),
            isSelected = !isGradient,
            onClick = onSelectSolid,
            modifier = Modifier.weight(1f),
        ) {
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .background(color = color, shape = CircleShape)
                    .border(width = 1.dp, color = NeutralVariant60, shape = CircleShape),
            )
        }

        ColorModeHalf(
            label = stringResource(R.string.color_mode_gradient),
            isSelected = isGradient,
            onClick = onSelectGradient,
            modifier = Modifier.weight(1f),
        ) {
            AodGradientDots(colors = gradientColors, dotSize = 12.dp)
        }
    }
}

@Composable
private fun ColorModeHalf(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leading: @Composable () -> Unit,
) {
    val background by animateColorAsState(
        targetValue = if (isSelected) MintAlpha12 else Color.Transparent,
        label = "colorModeBackground",
    )
    val content by animateColorAsState(
        targetValue = if (isSelected) Mint else Grey9A,
        label = "colorModeContent",
    )

    Row(
        modifier = modifier
            .height(40.dp)
            .background(background)
            .selectable(selected = isSelected, role = Role.Tab, onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
    ) {
        leading()

        Text(text = label, color = content, style = LabelLarge)
    }
}

@Composable
private fun GradientEditor(
    optionKey: String,
    gradient: AodGradient,
    onPreview: (AodGradient) -> Unit,
    onChange: (AodGradient) -> Unit,
) {
    var selectedStop by rememberSaveable(optionKey) { mutableIntStateOf(0) }
    val stopLabels = listOf(
        stringResource(R.string.gradient_stop_start),
        stringResource(R.string.gradient_stop_middle),
        stringResource(R.string.gradient_stop_end),
    )

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        GradientSwatchGrid(
            gradient = gradient,
            onSelect = { swatch -> onChange(swatch.gradient.copy(direction = gradient.direction)) },
        )

        PickerCaption(text = stringResource(R.string.gradient_custom))

        GradientStopBar(
            gradient = gradient,
            stopLabels = stopLabels,
            selectedStop = selectedStop,
            onSelectStop = { stop -> selectedStop = stop },
        )

        PickerCaption(text = stringResource(R.string.gradient_direction))

        GradientDirectionRow(
            direction = gradient.direction,
            onSelectDirection = { direction -> onChange(gradient.copy(direction = direction)) },
            onReverse = { onChange(gradient.reversed()) },
        )

        PickerCaption(text = stringResource(R.string.gradient_stop_color, stopLabels[selectedStop]))

        ClockColorPicker(
            color = gradient.colors[selectedStop],
            onColorPreview = { picked -> onPreview(gradient.withColor(selectedStop, picked)) },
            onColorChange = { picked -> onChange(gradient.withColor(selectedStop, picked)) },
        )
    }
}

@Composable
private fun PickerCaption(text: String) {
    Text(text = text, color = Grey9A, style = LabelMedium)
}

@Composable
private fun GradientSwatchGrid(
    gradient: AodGradient,
    onSelect: (GradientSwatchValue) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        PickerCaption(text = stringResource(R.string.gradient_presets))

        GradientSwatchValue.entries.chunked(GradientSwatchValue.COLUMN_COUNT).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                row.forEach { swatch ->
                    val isSelected = swatch.gradient.colors == gradient.colors
                    val border by animateColorAsState(
                        targetValue = if (isSelected) Mint else Color.Transparent,
                        label = "gradientSwatchBorder",
                    )
                    val label = stringResource(swatch.labelRes)

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clip(RoundedCornerShape12dp)
                            .background(Neutral17)
                            .border(width = 2.dp, color = border, shape = RoundedCornerShape12dp)
                            .selectable(selected = isSelected, role = Role.RadioButton, onClick = { onSelect(swatch) })
                            .semantics { contentDescription = label },
                        contentAlignment = Alignment.Center,
                    ) {
                        AodGradientDots(colors = swatch.gradient.colors)
                    }
                }
            }
        }
    }
}

@Composable
private fun GradientStopBar(
    gradient: AodGradient,
    stopLabels: List<String>,
    selectedStop: Int,
    onSelectStop: (Int) -> Unit,
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp)
                .padding(top = 5.dp)
                .height(18.dp)
                .background(brush = Brush.horizontalGradient(gradient.colors), shape = RoundedCornerShape12dp),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            gradient.colors.forEachIndexed { stop, stopColor ->
                val isSelected = stop == selectedStop
                val dotSize by animateDpAsState(targetValue = if (isSelected) 28.dp else 22.dp, label = "gradientStopSize")
                val ring by animateColorAsState(
                    targetValue = if (isSelected) Mint else GreyED,
                    label = "gradientStopRing",
                )

                Column(
                    modifier = Modifier
                        .width(56.dp)
                        .clip(RoundedCornerShape12dp)
                        .selectable(selected = isSelected, role = Role.RadioButton, onClick = { onSelectStop(stop) }),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Box(modifier = Modifier.size(28.dp), contentAlignment = Alignment.Center) {
                        Box(
                            modifier = Modifier
                                .size(dotSize)
                                .background(color = stopColor, shape = CircleShape)
                                .border(width = 2.dp, color = ring, shape = CircleShape),
                        )
                    }

                    Text(
                        text = stopLabels[stop],
                        color = if (isSelected) Mint else Grey9A,
                        style = LabelMedium,
                    )
                }
            }
        }
    }
}

@Composable
private fun GradientDirectionRow(
    direction: AodGradientDirection,
    onSelectDirection: (AodGradientDirection) -> Unit,
    onReverse: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AodGradientDirection.entries.forEach { entry ->
            FilterChip(
                selected = entry == direction,
                onClick = { onSelectDirection(entry) },
                label = {
                    Icon(
                        painter = painterResource(entry.iconRes),
                        contentDescription = stringResource(entry.labelRes),
                        modifier = Modifier.size(18.dp),
                    )
                },
            )
        }

        AssistChip(
            onClick = onReverse,
            label = { Text(text = stringResource(R.string.gradient_reverse)) },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_arrows_left_right),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
            },
        )
    }
}

@Preview(widthDp = 360)
@Composable
private fun AodColorPickerPreview() {
    val option = AodColorOption(
        key = "preview_color",
        labelRes = R.string.opt_clock_color,
        default = GradientSwatchValue.DEFAULT.gradient.start,
        defaultGradient = GradientSwatchValue.DEFAULT.gradient,
    )

    Theme {
        AodColorPicker(
            option = option,
            config = AodThemeConfig(),
            onPreview = { },
            onChange = { },
        )
    }
}
