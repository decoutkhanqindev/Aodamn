package com.decoutkhanqindev.custom_aod.presentation.theme

import androidx.annotation.FontRes
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.custom_aod.R

// Khai báo trước các font bên dưới: val cấp file khởi tạo theo thứ tự trong file.
private val ClockWeights = listOf(FontWeight.Thin, FontWeight.Light, FontWeight.Normal, FontWeight.Medium)

// Font app: Plus Jakarta Sans bản variable (Google Fonts, OFL, có tiếng Việt), một file cho mọi độ đậm qua trục wght.
val FontFamilyPlusJakartaSans = variableFontFamily(
    resId = R.font.plus_jakarta_sans_variable,
    weights = listOf(FontWeight.Normal, FontWeight.Medium, FontWeight.SemiBold, FontWeight.Bold),
)

// Font đồng hồ (Google Fonts, OFL, bản variable có tiếng Việt vì ngày và ghi chú dùng chung font). Đồng hồ dùng Thin / Light, chữ
// phụ dùng Normal; độ đậm ngoài dải của font được kẹp về mép dải (Dancing Script nhỏ nhất 400).
val FontFamilyFraunces = variableFontFamily(resId = R.font.fraunces_variable, FontVariation.Setting("SOFT", 100f))
val FontFamilyJetBrainsMono = variableFontFamily(resId = R.font.jetbrains_mono_variable)
val FontFamilyDancingScript = variableFontFamily(resId = R.font.dancing_script_variable)
val FontFamilyComfortaa = variableFontFamily(resId = R.font.comfortaa_variable)
val FontFamilyBigShouldersDisplay = variableFontFamily(resId = R.font.big_shoulders_display_variable)
val FontFamilyJosefinSans = variableFontFamily(resId = R.font.josefin_sans_variable)

// Type scale mặc định của Material 3 (kèm lineHeightStyle, platformStyle của nó), chỉ đổi font sang Plus Jakarta Sans; BodyLarge là bản riêng của app.
private val MaterialTypeScale = androidx.compose.material3.Typography()

val DisplaySmall = MaterialTypeScale.displaySmall.copy(fontFamily = FontFamilyPlusJakartaSans)
val HeadlineLarge = MaterialTypeScale.headlineLarge.copy(fontFamily = FontFamilyPlusJakartaSans)
val HeadlineMedium = MaterialTypeScale.headlineMedium.copy(fontFamily = FontFamilyPlusJakartaSans)
val HeadlineSmall = MaterialTypeScale.headlineSmall.copy(fontFamily = FontFamilyPlusJakartaSans)
val TitleLarge = MaterialTypeScale.titleLarge.copy(fontFamily = FontFamilyPlusJakartaSans)
val TitleMedium = MaterialTypeScale.titleMedium.copy(fontFamily = FontFamilyPlusJakartaSans)
val BodyLarge = TextStyle(
    fontFamily = FontFamilyPlusJakartaSans,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.5.sp,
)
val BodyMedium = MaterialTypeScale.bodyMedium.copy(fontFamily = FontFamilyPlusJakartaSans)
val BodySmall = MaterialTypeScale.bodySmall.copy(fontFamily = FontFamilyPlusJakartaSans)
val LabelLarge = MaterialTypeScale.labelLarge.copy(fontFamily = FontFamilyPlusJakartaSans)
val LabelMedium = MaterialTypeScale.labelMedium.copy(fontFamily = FontFamilyPlusJakartaSans)

// Cầu nối cho component Material 3: style app không dùng vẫn theo type scale của Material 3 nhưng đổi sang Plus Jakarta Sans.
val MaterialTypography = androidx.compose.material3.Typography(
    displayLarge = MaterialTypeScale.displayLarge.copy(fontFamily = FontFamilyPlusJakartaSans),
    displayMedium = MaterialTypeScale.displayMedium.copy(fontFamily = FontFamilyPlusJakartaSans),
    displaySmall = DisplaySmall,
    headlineLarge = HeadlineLarge,
    headlineMedium = HeadlineMedium,
    headlineSmall = HeadlineSmall,
    titleLarge = TitleLarge,
    titleMedium = TitleMedium,
    titleSmall = MaterialTypeScale.titleSmall.copy(fontFamily = FontFamilyPlusJakartaSans),
    bodyLarge = BodyLarge,
    bodyMedium = BodyMedium,
    bodySmall = BodySmall,
    labelLarge = LabelLarge,
    labelMedium = LabelMedium,
    labelSmall = MaterialTypeScale.labelSmall.copy(fontFamily = FontFamilyPlusJakartaSans),
)

private fun variableFontFamily(
    @FontRes resId: Int,
    vararg settings: FontVariation.Setting,
    weights: List<FontWeight> = ClockWeights,
): FontFamily = FontFamily(
    weights.map { weight ->
        Font(
            resId = resId,
            weight = weight,
            variationSettings = FontVariation.Settings(FontVariation.weight(weight.weight), *settings),
        )
    },
)
