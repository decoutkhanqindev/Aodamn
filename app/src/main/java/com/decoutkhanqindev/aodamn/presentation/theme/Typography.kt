package com.decoutkhanqindev.aodamn.presentation.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.aodamn.R

val FontFamilyPlusJakartaSans = FontFamily(
    Font(R.font.plus_jakarta_sans_extralight, FontWeight.ExtraLight),
    Font(R.font.plus_jakarta_sans_light, FontWeight.Light),
    Font(R.font.plus_jakarta_sans_regular, FontWeight.Normal),
    Font(R.font.plus_jakarta_sans_medium, FontWeight.Medium),
    Font(R.font.plus_jakarta_sans_semibold, FontWeight.SemiBold),
    Font(R.font.plus_jakarta_sans_bold, FontWeight.Bold),
    Font(R.font.plus_jakarta_sans_extrabold, FontWeight.ExtraBold),
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
    Font(R.font.big_shoulders_display_black, FontWeight.Black),
)

val FontFamilyAnton = FontFamily(
    Font(R.font.anton_regular, FontWeight.Normal),
)

val FontFamilyGoogleSans = FontFamily(
    Font(R.font.google_sans_bold, FontWeight.Bold),
)

val FontFamilyGoogleSansText = FontFamily(
    Font(R.font.google_sans_regular, FontWeight.Normal),
    Font(R.font.google_sans_bold, FontWeight.Bold),
)

val FontFamilyGoogleSansMedium = FontFamily(
    Font(R.font.google_sans_regular, FontWeight.Normal),
    Font(R.font.google_sans_medium, FontWeight.Medium),
)

val FontFamilyKronaOne = FontFamily(
    Font(R.font.krona_one_regular, FontWeight.Normal),
)

val FontFamilyZalandoSansExpanded = FontFamily(
    Font(R.font.zalando_sans_expanded_semibold, FontWeight.SemiBold),
)

val FontFamilyJosefinSans = FontFamily(
    Font(R.font.josefin_sans_thin, FontWeight.Thin),
    Font(R.font.josefin_sans_light, FontWeight.Light),
    Font(R.font.josefin_sans_regular, FontWeight.Normal),
    Font(R.font.josefin_sans_semibold, FontWeight.SemiBold),
    Font(R.font.josefin_sans_bold, FontWeight.Bold),
)

val FontFamilyGoogleSansFlex = FontFamily(
    Font(R.font.google_sans_flex_extrablack, FontWeight(1000)),
)
val FontFamilyGoogleSansFlexRounded = FontFamily(
    Font(R.font.google_sans_flex_rounded_extrablack, FontWeight(1000)),
)

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
