package com.decoutkhanqindev.aodamn.presentation.components.aod.block

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentStyle

fun Modifier.aodTint(style: AodComponentStyle): Modifier = aodTint(style.gradient?.toBrush())

fun Modifier.aodTint(brush: Brush?): Modifier {
    if (brush == null) return this
    return graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
        .drawWithContent {
            drawContent()
            drawRect(brush = brush, blendMode = BlendMode.SrcIn)
        }
}
