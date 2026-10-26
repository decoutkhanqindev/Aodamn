package com.decoutkhanqindev.aodamn.presentation.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.aodamn.R

// Font (Google Fonts, OFL, có tiếng Việt) là file TTF tĩnh từng độ đậm trong res/font, sinh từ font variable (font chỉ có bản tĩnh như Anton
// thì chép nguyên) bằng tools/fonts/make_static_fonts.py (đổi danh sách độ đậm ở đó thì đổi ở đây). Không để Compose áp trục độ đậm cho font variable: nó áp trục
// wght qua Paint, máy Xiaomi (MIUI / HyperOS) thay font của app bằng font hệ thống. Một font vừa là font app vừa là font đồng hồ nên đủ dải
// mảnh → đậm: đồng hồ Cổ điển dùng Thin / Light, chữ số chủ đề Sỏi dùng Bold, Viên nang ExtraBold, chữ phụ dùng Normal. Độ đậm font không có thì Compose lấy
// độ đậm gần nhất (Plus Jakarta Sans nhỏ nhất 200, Comfortaa 300, Dancing Script 400).
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

// Chữ đậm hẹp của Chuỗi (font của app mẫu Muviz Edge), chỉ có một độ đậm.
val FontFamilyAnton = FontFamily(
    Font(R.font.anton_regular, FontWeight.Normal),
)

// Ngày, pin và trình phát của Vành: Google Sans (bản mở của Product Sans mà app mẫu dùng), chỉ có bản đậm.
val FontFamilyGoogleSans = FontFamily(
    Font(R.font.google_sans_bold, FontWeight.Bold),
)

// Chữ của La bàn: Google Sans Regular (dòng pin, thông báo, nghệ sĩ) và Bold (ngày, tên bài) như Product Sans của app mẫu. Họ riêng vì
// FontFamilyGoogleSans của Vành chỉ có Bold (thêm Regular vào đó thì dòng pin của Vành đổi độ đậm).
val FontFamilyGoogleSansText = FontFamily(
    Font(R.font.google_sans_regular, FontWeight.Normal),
    Font(R.font.google_sans_bold, FontWeight.Bold),
)

// Chữ của Gợn sóng: Google Sans Regular / Medium thay Rubik của app mẫu (Rubik thiếu tiếng Việt). Họ riêng vì thêm Medium vào
// FontFamilyGoogleSansText thì chữ pin (Medium) của La bàn, Bán nguyệt đổi độ đậm.
val FontFamilyGoogleSansMedium = FontFamily(
    Font(R.font.google_sans_regular, FontWeight.Normal),
    Font(R.font.google_sans_medium, FontWeight.Medium),
)

// Số trên các vòng của La bàn (font của app mẫu), chỉ có một độ đậm và chỉ vẽ chữ số nên không cần tiếng Việt.
val FontFamilyKronaOne = FontFamily(
    Font(R.font.krona_one_regular, FontWeight.Normal),
)

// Bốn số 12 / 3 / 6 / 9 của Vành (font của app mẫu), chỉ có một độ đậm.
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

// TODO(coum-face): họ font mới của chủ đề
// TODO(big-overlap): họ font mới của chủ đề
// Bốn chữ số của Chồng số: Google Sans Flex như app mẫu, độ đậm 1000 (font mẫu còn đậm hơn bản Google Fonts đậm nhất). Góc vuông / bo tròn là hai
// file tĩnh ghim trục ROND 0 / 100 nên là hai họ; chỉ vẽ chữ số.
val FontFamilyGoogleSansFlex = FontFamily(
    Font(R.font.google_sans_flex_extrablack, FontWeight(1000)),
)
val FontFamilyGoogleSansFlexRounded = FontFamily(
    Font(R.font.google_sans_flex_rounded_extrablack, FontWeight(1000)),
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
