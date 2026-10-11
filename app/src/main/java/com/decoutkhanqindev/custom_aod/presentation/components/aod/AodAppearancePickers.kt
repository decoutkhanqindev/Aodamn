package com.decoutkhanqindev.custom_aod.presentation.components.aod

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.model.aod.appearance.WallpaperValue
import com.decoutkhanqindev.custom_aod.presentation.theme.Grey9A
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyED
import com.decoutkhanqindev.custom_aod.presentation.theme.LabelMedium
import com.decoutkhanqindev.custom_aod.presentation.theme.Mint
import com.decoutkhanqindev.custom_aod.presentation.theme.NeutralVariant30
import com.decoutkhanqindev.custom_aod.presentation.theme.RoundedCornerShape12dp
import com.decoutkhanqindev.custom_aod.presentation.theme.RoundedCornerShape8dp
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@Composable
fun WallpaperPicker(
    selected: WallpaperValue?,
    onSelect: (WallpaperValue) -> Unit,
) {
    // Cuộn sẵn tới ảnh đang chọn (18 ảnh không vừa một màn hình); LazyRow chỉ dựng các ảnh đang thấy nên mở tab Nền không phải
    // phân tích hết 18 vector một lượt.
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = (WallpaperValue.entries.indexOf(selected) - 1).coerceAtLeast(0),
    )

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        state = listState,
        contentPadding = PaddingValues(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(WallpaperValue.entries, key = { wallpaper -> wallpaper.code }) { wallpaper ->
            val isSelected = wallpaper == selected

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape8dp)
                    .selectable(
                        selected = isSelected,
                        role = Role.RadioButton,
                        onClick = { onSelect(wallpaper) })
                    .padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 60.dp, height = 132.dp)
                        .clip(RoundedCornerShape12dp)
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) {
                                Mint
                            } else {
                                NeutralVariant30
                            },
                            shape = RoundedCornerShape12dp,
                        ),
                ) {
                    Image(
                        painter = painterResource(wallpaper.drawableRes),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )

                    if (isSelected) {
                        Icon(
                            painter = painterResource(R.drawable.ic_check),
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(6.dp)
                                .size(16.dp),
                            tint = Mint,
                        )
                    }
                }

                Text(
                    text = stringResource(wallpaper.labelRes),
                    modifier = Modifier.padding(top = 6.dp),
                    color = if (isSelected) {
                        Mint
                    } else {
                        Grey9A
                    },
                    style = LabelMedium,
                )
            }
        }
    }
}

// Wheel HSV + thanh độ sáng của skydoves/colorpicker-compose. Kéo thì onColorPreview (xem trước trực tiếp), thả tay mới onColorChange
// (một lần cho cả lần kéo). Màu đổi từ ngoài (hoàn tác, làm lại) thì kéo wheel theo; màu do chính picker phát ra đã bằng selectedColor
// nên không chạy lại.
@Composable
fun ClockColorPicker(
    color: Color,
    onColorPreview: (Color) -> Unit,
    onColorChange: (Color) -> Unit,
    modifier: Modifier = Modifier,
) {
    val controller = rememberColorPickerController()
    val currentOnColorPreview by rememberUpdatedState(onColorPreview)
    val currentOnColorChange by rememberUpdatedState(onColorChange)

    SideEffect(controller) {
        controller.wheelRadius = 12.dp
        controller.wheelColor = GreyED
    }

    SideEffect(color) {
        if (controller.selectedColor.value != color) controller.selectByColor(color, fromUser = false)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        HsvColorPicker(
            modifier = Modifier
                .fillMaxWidth()
                .height(176.dp),
            controller = controller,
            initialColor = color,
            onColorChanged = { envelope -> if (envelope.fromUser) currentOnColorPreview(envelope.color) },
            onColorPickingFinished = { envelope -> currentOnColorChange(envelope.color) },
        )

        BrightnessSlider(
            modifier = Modifier
                .fillMaxWidth()
                .height(28.dp),
            controller = controller,
            borderRadius = 14.dp,
            borderSize = 2.dp,
            borderColor = NeutralVariant30,
            wheelRadius = 10.dp,
            wheelColor = GreyED,
            onColorChanged = { envelope -> if (envelope.fromUser) currentOnColorPreview(envelope.color) },
            onFinish = { currentOnColorChange(controller.selectedColor.value) },
        )
    }
}
