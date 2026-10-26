package com.decoutkhanqindev.aodamn.presentation.model.aod.appearance

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import com.decoutkhanqindev.aodamn.R

@Immutable
enum class WallpaperValue(
    val code: Int,
    @param:DrawableRes val drawableRes: Int,
    @param:StringRes val labelRes: Int,
    val alignment: Alignment,
) {
    STARS(
        code = 1,
        drawableRes = R.drawable.img_wallpaper_stars,
        labelRes = R.string.wallpaper_stars,
        alignment = Alignment.Center,
    ),
    AURORA(
        code = 2,
        drawableRes = R.drawable.img_wallpaper_aurora,
        labelRes = R.string.wallpaper_aurora,
        alignment = Alignment.TopCenter,
    ),
    ECLIPSE(
        code = 3,
        drawableRes = R.drawable.img_wallpaper_eclipse,
        labelRes = R.string.wallpaper_eclipse,
        alignment = Alignment.TopCenter,
    ),
    WAVES(
        code = 4,
        drawableRes = R.drawable.img_wallpaper_waves,
        labelRes = R.string.wallpaper_waves,
        alignment = Alignment.BottomCenter,
    ),
    NEBULA(
        code = 5,
        drawableRes = R.drawable.img_wallpaper_nebula,
        labelRes = R.string.wallpaper_nebula,
        alignment = Alignment.Center,
    ),
    HORIZON(
        code = 6,
        drawableRes = R.drawable.img_wallpaper_horizon,
        labelRes = R.string.wallpaper_horizon,
        alignment = Alignment.BottomCenter,
    ),
    METEOR(
        code = 7,
        drawableRes = R.drawable.img_wallpaper_meteor,
        labelRes = R.string.wallpaper_meteor,
        alignment = Alignment.TopCenter,
    ),
    PLANET(
        code = 8,
        drawableRes = R.drawable.img_wallpaper_planet,
        labelRes = R.string.wallpaper_planet,
        alignment = Alignment.BottomCenter,
    ),
    ORBITS(
        code = 9,
        drawableRes = R.drawable.img_wallpaper_orbits,
        labelRes = R.string.wallpaper_orbits,
        alignment = Alignment.BottomCenter,
    ),
    MOUNTAINS(
        code = 10,
        drawableRes = R.drawable.img_wallpaper_mountains,
        labelRes = R.string.wallpaper_mountains,
        alignment = Alignment.BottomCenter,
    ),
    FIREFLIES(
        code = 11,
        drawableRes = R.drawable.img_wallpaper_fireflies,
        labelRes = R.string.wallpaper_fireflies,
        alignment = Alignment.BottomCenter,
    ),
    SAKURA(
        code = 12,
        drawableRes = R.drawable.img_wallpaper_sakura,
        labelRes = R.string.wallpaper_sakura,
        alignment = Alignment.TopCenter,
    ),
    JELLYFISH(
        code = 13,
        drawableRes = R.drawable.img_wallpaper_jellyfish,
        labelRes = R.string.wallpaper_jellyfish,
        alignment = Alignment.TopCenter,
    ),
    CITY(
        code = 14,
        drawableRes = R.drawable.img_wallpaper_city,
        labelRes = R.string.wallpaper_city,
        alignment = Alignment.BottomCenter,
    ),
    FIREWORKS(
        code = 15,
        drawableRes = R.drawable.img_wallpaper_fireworks,
        labelRes = R.string.wallpaper_fireworks,
        alignment = Alignment.TopCenter,
    ),
    CONTOURS(
        code = 16,
        drawableRes = R.drawable.img_wallpaper_contours,
        labelRes = R.string.wallpaper_contours,
        alignment = Alignment.TopCenter,
    ),
    SOUNDWAVE(
        code = 17,
        drawableRes = R.drawable.img_wallpaper_soundwave,
        labelRes = R.string.wallpaper_soundwave,
        alignment = Alignment.BottomCenter,
    ),
    LANTERNS(
        code = 18,
        drawableRes = R.drawable.img_wallpaper_lanterns,
        labelRes = R.string.wallpaper_lanterns,
        alignment = Alignment.BottomCenter,
    );

    companion object {
        const val NONE_CODE = 0

        fun fromCode(code: Int?): WallpaperValue? = entries.find { it.code == code }
    }
}
