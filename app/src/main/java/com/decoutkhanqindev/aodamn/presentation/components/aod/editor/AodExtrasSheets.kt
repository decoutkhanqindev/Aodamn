package com.decoutkhanqindev.aodamn.presentation.components.aod.editor

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.AppPrimaryButton
import com.decoutkhanqindev.aodamn.presentation.components.AppTextButton
import com.decoutkhanqindev.aodamn.presentation.components.onClick
import com.decoutkhanqindev.aodamn.presentation.components.sheet.AppSheet
import com.decoutkhanqindev.aodamn.presentation.components.sheet.AppSheetToolHeader
import com.decoutkhanqindev.aodamn.presentation.model.aod.settings.AodExtrasUiModel
import com.decoutkhanqindev.aodamn.presentation.theme.Black
import com.decoutkhanqindev.aodamn.presentation.theme.BodyLarge
import com.decoutkhanqindev.aodamn.presentation.theme.BodyMedium
import com.decoutkhanqindev.aodamn.presentation.theme.BodySmall
import com.decoutkhanqindev.aodamn.presentation.theme.Grey3A
import com.decoutkhanqindev.aodamn.presentation.theme.Grey5A
import com.decoutkhanqindev.aodamn.presentation.theme.Grey6E
import com.decoutkhanqindev.aodamn.presentation.theme.Grey9A
import com.decoutkhanqindev.aodamn.presentation.theme.GreyED
import com.decoutkhanqindev.aodamn.presentation.theme.LabelLarge
import com.decoutkhanqindev.aodamn.presentation.theme.Mint
import com.decoutkhanqindev.aodamn.presentation.theme.Neutral17
import com.decoutkhanqindev.aodamn.presentation.theme.RoundedCornerShape12dp
import com.decoutkhanqindev.aodamn.presentation.theme.RoundedCornerShape16dp
import com.decoutkhanqindev.aodamn.presentation.theme.White

// Sửa ghi nhớ: ô nhập nền đen viền mint, số ký tự ở góc, vài gợi ý nhanh (chạm là điền), Lưu / Hủy.
@Composable
fun MemoEditorSheet(
    isVisible: Boolean,
    memo: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    AppSheet(isVisible = isVisible, onDismissRequest = onDismiss) {
        val memoState = rememberTextFieldState(initialText = memo)
        val suggestions = listOf(
            stringResource(R.string.memo_suggestion_water),
            stringResource(R.string.memo_suggestion_call),
            stringResource(R.string.memo_suggestion_meeting),
        )

        AppSheetToolHeader(
            iconRes = R.drawable.ic_note_pencil,
            title = stringResource(R.string.opt_memo),
            description = stringResource(R.string.memo_desc),
        )

        BasicTextField(
            state = memoState,
            modifier = Modifier.fillMaxWidth(),
            textStyle = BodyLarge.copy(color = GreyED),
            cursorBrush = SolidColor(Mint),
            inputTransformation = InputTransformation.maxLength(AodExtrasUiModel.MEMO_MAX_LENGTH),
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            lineLimits = TextFieldLineLimits.MultiLine(minHeightInLines = 3, maxHeightInLines = 4),
            decorator = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Black, shape = RoundedCornerShape16dp)
                        .border(width = 1.5.dp, color = Mint, shape = RoundedCornerShape16dp)
                        .padding(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 28.dp),
                ) {
                    if (memoState.text.isEmpty()) {
                        Text(text = stringResource(R.string.memo_hint), color = Grey6E, style = BodyLarge)
                    }

                    innerTextField()
                }
            },
        )

        Text(
            text = stringResource(R.string.memo_length, memoState.text.length, AodExtrasUiModel.MEMO_MAX_LENGTH),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            color = Grey6E,
            style = BodySmall,
        )

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            suggestions.forEach { suggestion ->
                Text(
                    text = suggestion,
                    modifier = Modifier
                        .heightIn(min = 36.dp)
                        .clip(CircleShape)
                        .background(Neutral17)
                        .onClick(shape = CircleShape) { memoState.setTextAndPlaceCursorAtEnd(suggestion) }
                        .semantics { role = Role.Button }
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    color = Grey9A,
                    style = BodyMedium,
                )
            }
        }

        AppPrimaryButton(
            text = stringResource(R.string.action_save),
            onClick = { onConfirm(memoState.text.toString()) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            iconRes = R.drawable.ic_check,
        )

        AppTextButton(
            text = stringResource(R.string.action_cancel),
            onClick = onDismiss,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
        )
    }
}

// Vẽ nhanh: khung vuông nền đen, Hoàn tác nét / Xóa hết, Lưu hình / Hủy. Sheet không kéo được để nét vẽ dọc không kéo sheet xuống.
@Composable
fun DrawingPadSheet(
    isVisible: Boolean,
    onConfirm: (Bitmap) -> Unit,
    onDismiss: () -> Unit,
) {
    AppSheet(isVisible = isVisible, onDismissRequest = onDismiss, isDragEnabled = false) {
        val density = LocalDensity.current
        val strokeWidthPx = with(density) { 8.dp.toPx() }
        val strokes = remember { mutableStateListOf<SnapshotStateList<Offset>>() }
        var padSize by remember { mutableStateOf(IntSize.Zero) }

        AppSheetToolHeader(
            iconRes = R.drawable.ic_paint_brush,
            title = stringResource(R.string.opt_drawing),
            description = stringResource(R.string.drawing_desc),
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape16dp)
                .background(Black)
                .border(width = 1.dp, color = Grey3A, shape = RoundedCornerShape16dp)
                .onSizeChanged { size -> padSize = size }
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val down = awaitFirstDown()
                        val stroke = mutableStateListOf(down.position)
                        strokes.add(stroke)
                        drag(down.id) { change ->
                            change.consume()
                            stroke.add(change.position)
                        }
                    }
                }
                .drawBehind {
                    strokes.forEach { stroke -> drawStroke(points = stroke, color = White, widthPx = strokeWidthPx) }
                },
            contentAlignment = Alignment.Center,
        ) {
            if (strokes.isEmpty()) {
                Text(text = stringResource(R.string.drawing_hint), color = Grey6E, style = BodyMedium)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            DrawingToolButton(
                iconRes = R.drawable.ic_arrow_u_up_left,
                text = stringResource(R.string.drawing_undo),
                isEnabled = strokes.isNotEmpty(),
                onClick = { strokes.removeAt(strokes.lastIndex) },
                modifier = Modifier.weight(1f),
            )

            DrawingToolButton(
                iconRes = R.drawable.ic_eraser,
                text = stringResource(R.string.action_clear),
                isEnabled = strokes.isNotEmpty(),
                onClick = { strokes.clear() },
                modifier = Modifier.weight(1f),
            )
        }

        AppPrimaryButton(
            text = stringResource(R.string.drawing_save),
            onClick = {
                onConfirm(
                    renderDrawing(
                        strokes = strokes,
                        size = padSize,
                        density = density,
                        strokeWidthPx = strokeWidthPx,
                        strokeColor = White,
                    ),
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            iconRes = R.drawable.ic_check,
            isEnabled = strokes.isNotEmpty(),
        )

        AppTextButton(
            text = stringResource(R.string.action_cancel),
            onClick = onDismiss,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
        )
    }
}

@Composable
private fun DrawingToolButton(
    @DrawableRes iconRes: Int,
    text: String,
    isEnabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val color = if (isEnabled) GreyED else Grey5A

    Row(
        modifier = modifier
            .heightIn(min = 44.dp)
            .clip(RoundedCornerShape12dp)
            .background(Neutral17)
            .then(if (isEnabled) Modifier.onClick(shape = RoundedCornerShape12dp, action = onClick) else Modifier)
            .semantics { role = Role.Button }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = color,
        )

        Text(text = text, color = color, fontWeight = FontWeight.SemiBold, style = LabelLarge)
    }
}

private fun DrawScope.drawStroke(
    points: List<Offset>,
    color: Color,
    widthPx: Float,
) {
    if (points.size == 1) {
        drawCircle(color = color, radius = widthPx / 2, center = points.first())
    } else {
        drawPoints(
            points = points,
            pointMode = PointMode.Polygon,
            color = color,
            strokeWidth = widthPx,
            cap = StrokeCap.Round,
        )
    }
}

private fun renderDrawing(
    strokes: List<List<Offset>>,
    size: IntSize,
    density: Density,
    strokeWidthPx: Float,
    strokeColor: Color,
): Bitmap {
    val image = ImageBitmap(width = size.width, height = size.height)
    CanvasDrawScope().draw(
        density = density,
        layoutDirection = LayoutDirection.Ltr,
        canvas = Canvas(image),
        size = size.toSize(),
    ) {
        strokes.forEach { stroke -> drawStroke(points = stroke, color = strokeColor, widthPx = strokeWidthPx) }
    }
    return image.asAndroidBitmap()
}
