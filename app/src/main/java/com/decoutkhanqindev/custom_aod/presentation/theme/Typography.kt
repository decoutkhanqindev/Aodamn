package com.decoutkhanqindev.custom_aod.presentation.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.custom_aod.R

// Font (Google Fonts, OFL, có tiếng Việt) là file TTF tĩnh từng độ đậm trong res/font, sinh từ font variable bằng
// tools/fonts/make_static_fonts.py (đổi danh sách độ đậm ở đó thì đổi ở đây). Không để Compose áp trục độ đậm cho font variable: nó áp trục
// wght qua Paint, máy Xiaomi (MIUI / HyperOS) thay font của app bằng font hệ thống. Một font vừa là font app vừa là font đồng hồ nên đủ dải
// mảnh → đậm: đồng hồ Cổ điển dùng Thin / Light, chữ số chủ đề Sỏi dùng Bold, chữ phụ dùng Normal. Độ đậm font không có thì Compose lấy
// độ đậm gần nhất (Plus Jakarta Sans nhỏ nhất 200, Comfortaa 300, Dancing Script 400).
val FontFamilyPlusJakartaSans = FontFamily(
    Font(R.font.plus_jakarta_sans_extralight, FontWeight.ExtraLight),
    Font(R.font.plus_jakarta_sans_light, FontWeight.Light),
    Font(R.font.plus_jakarta_sans_regular, FontWeight.Normal),
    Font(R.font.plus_jakarta_sans_medium, FontWeight.Medium),
    Font(R.font.plus_jakarta_sans_semibold, FontWeight.SemiBold),
    Font(R.font.plus_jakarta_sans_bold, FontWeight.Bold),
)

val FontFamilyFraunces = FontFamily(
    Font(R.font.fraunces_thin, FontWeight.Thin),
    Font(R.font.fraunces_light, FontWeight.Light),
    Font(R.font.fraunces_regular, FontWeight.Normal),
    Font(R.font.fraunces_semibold, FontWeight.SemiBold),
    Font(R.font.fraunces_bold, FontWeight.Bold),
)

val FontFamilyJetBrainsMono = FontFamily(
    Font(R.font.jetbrains_mono_thin, FontWeight.Thin),
    Font(R.font.jetbrains_mono_light, FontWeight.Light),
    Font(R.font.jetbrains_mono_regular, FontWeight.Normal),
    Font(R.font.jetbrains_mono_semibold, FontWeight.SemiBold),
    Font(R.font.jetbrains_mono_bold, FontWeight.Bold),
)

val FontFamilyDancingScript = FontFamily(
    Font(R.font.dancing_script_regular, FontWeight.Normal),
    Font(R.font.dancing_script_semibold, FontWeight.SemiBold),
    Font(R.font.dancing_script_bold, FontWeight.Bold),
)

val FontFamilyComfortaa = FontFamily(
    Font(R.font.comfortaa_light, FontWeight.Light),
    Font(R.font.comfortaa_regular, FontWeight.Normal),
    Font(R.font.comfortaa_semibold, FontWeight.SemiBold),
    Font(R.font.comfortaa_bold, FontWeight.Bold),
)

val FontFamilyBigShouldersDisplay = FontFamily(
    Font(R.font.big_shoulders_display_thin, FontWeight.Thin),
    Font(R.font.big_shoulders_display_light, FontWeight.Light),
    Font(R.font.big_shoulders_display_regular, FontWeight.Normal),
    Font(R.font.big_shoulders_display_semibold, FontWeight.SemiBold),
    Font(R.font.big_shoulders_display_bold, FontWeight.Bold),
)

val FontFamilyJosefinSans = FontFamily(
    Font(R.font.josefin_sans_thin, FontWeight.Thin),
    Font(R.font.josefin_sans_light, FontWeight.Light),
    Font(R.font.josefin_sans_regular, FontWeight.Normal),
    Font(R.font.josefin_sans_semibold, FontWeight.SemiBold),
    Font(R.font.josefin_sans_bold, FontWeight.Bold),
)

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
