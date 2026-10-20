package com.decoutkhanqindev.aodamn.presentation.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Black = Color(0xFF000000)
val White = Color(0xFFFFFFFF)
val Mint = Color(0xFF7FD1AE)
val Red = Color(0xFFE5484D)
val Blue = Color(0xFF8AB4F8)
val Purple = Color(0xFFC3A6FF)
val Lavender = Color(0xFFBAB8F8)
val Orange = Color(0xFFFFB27A)
val Pink = Color(0xFFFF9EC4)

val GreyED = Color(0xFFEDEDED)
val GreyB4 = Color(0xFFB4B4B4)
val Grey9A = Color(0xFF9A9A9A)
val Grey8A = Color(0xFF8A8A8A)
val Grey6E = Color(0xFF6E6E6E)
val Grey5A = Color(0xFF5A5A5A)
val Grey3A = Color(0xFF3A3A3A)

// Màu nhấn mặc định của chủ đề (thẻ ở lưới chủ đề): mỗi chủ đề một màu để lưới không trùng nhau. Lime đo trên thẻ chủ đề trong lưới của app
// mẫu Viên nang; Cyan của Vành (lime của app mẫu đã là màu Viên nang); Amber (hổ phách) của La bàn; GreyE0 là chữ phụ của Chuỗi.
val Lime = Color(0xFFEAFC82)
val Coral = Color(0xFFFF8B7B)
val Cyan = Color(0xFF80DEEA)
val Amber = Color(0xFFFFD54F)
val GreyE0 = Color(0xFFE0E0E0)

// Màu có sẵn của bảng chọn màu (ColorSwatchValue): màu đặc trưng của các app quen thuộc, xếp theo sắc độ.
val YoutubeRed = Color(0xFFFF0033)
val SoundcloudOrange = Color(0xFFFF5500)
val AmazonOrange = Color(0xFFFF9900)
val SnapchatYellow = Color(0xFFFFFC00)
val DuolingoGreen = Color(0xFF58CC02)
val SpotifyGreen = Color(0xFF1ED760)
val TiktokCyan = Color(0xFF25F4EE)
val TelegramBlue = Color(0xFF24A1DE)
val ZaloBlue = Color(0xFF0068FF)
val DiscordBlurple = Color(0xFF5865F2)
val TwitchPurple = Color(0xFF9146FF)
val InstagramMagenta = Color(0xFFC13584)
val TinderPink = Color(0xFFFE3C72)

// Ba màu Đầu / Giữa / Cuối của từng bộ gradient có sẵn (GradientSwatchValue), lấy từ bảng màu của app mẫu Muviz Edge.
val GradientSunsetStart = Color(0xFFFFCC70)
val GradientSunsetMiddle = Color(0xFFC850C0)
val GradientSunsetEnd = Color(0xFF4158D0)
val GradientIceEmberStart = Color(0xFFC1FFF4)
val GradientIceEmberMiddle = Color(0xFFD9D9D9)
val GradientIceEmberEnd = Color(0xFFCA5353)
val GradientLimeRoseStart = Color(0xFFAACC00)
val GradientLimeRoseMiddle = Color(0xFFFF85A1)
val GradientLimeRoseEnd = Color(0xFF343A40)
val GradientTropicStart = Color(0xFFF0AB32)
val GradientTropicMiddle = Color(0xFF07A7B9)
val GradientTropicEnd = Color(0xFFDD4B1C)
val GradientOrchidGoldStart = Color(0xFFB474FF)
val GradientOrchidGoldMiddle = Color(0xFFD9D99E)
val GradientOrchidGoldEnd = Color(0xFFCAB525)
val GradientPastelSkyStart = Color(0xFFA9C9FF)
val GradientPastelSkyMiddle = Color(0xFFCEC4F7)
val GradientPastelSkyEnd = Color(0xFFFFBBEC)
val GradientLavaStart = Color(0xFFFC210D)
val GradientLavaMiddle = Color(0xFF981424)
val GradientLavaEnd = Color(0xFF450A3B)
val GradientMintBerryStart = Color(0xFFA1D2AF)
val GradientMintBerryMiddle = Color(0xFFD43A44)
val GradientMintBerryEnd = Color(0xFF44008F)
val GradientLilacSeaStart = Color(0xFFD8B5FF)
val GradientLilacSeaMiddle = Color(0xFF63A4B6)
val GradientLilacSeaEnd = Color(0xFF1EAE98)
val GradientAutumnStart = Color(0xFFD24C4C)
val GradientAutumnMiddle = Color(0xFFD47C3A)
val GradientAutumnEnd = Color(0xFFE1B500)
val GradientGraphiteStart = Color(0xFF6A6A61)
val GradientGraphiteMiddle = Color(0xFF898989)
val GradientGraphiteEnd = Color(0xFFD6D6D6)
val GradientSilverStart = Color(0xFFA3A395)
val GradientSilverMiddle = Color(0xFFBBBBBB)
val GradientSilverEnd = Color(0xFFFFFFFF)

// Chữ phụ của chủ đề Thẻ lấy từ app mẫu (dịch ngược): phút / AM-PM / ngày xám sáng.
val GreyE4 = Color(0xFFE4E4E4)

// Ba màu trung tính mặc định của Material 3 (bản tối) mà app vẫn dùng, giữ đúng giá trị để giao diện không đổi.
val Neutral12 = Color(0xFF211F26)
val NeutralVariant30 = Color(0xFF49454F)
val NeutralVariant60 = Color(0xFF938F99)
// Nền phụ trong sheet / hộp thoại (ô icon, nút công cụ, chip) nổi nhẹ trên Neutral12.
val Neutral17 = Color(0xFF2B2930)
// Chữ của nút nguy hiểm trên nền RedAlpha12: đỏ nhạt hơn Red cho đủ tương phản trên nền tối.
val RedLight = Color(0xFFF2777B)
// Dải sáng của thanh kéo sheet, toast, lớp phủ đang áp dụng: cùng dải màu viền điện thoại của icon app (tools/brand/brand_geometry.py).
val EdgeGlowGradient = Brush.horizontalGradient(listOf(Mint, Blue, Purple))
val EdgeGlowOfflineGradient = Brush.horizontalGradient(listOf(Grey6E, Grey3A))

// Alpha 12: nền phủ nhạt một màu (thẻ đang chọn, ô icon, dòng cảnh báo), chữ trên đó vẫn đủ tương phản.
val MintAlpha12 = Mint.copy(alpha = 0.12f)
val MintAlpha40 = Mint.copy(alpha = 0.4f)
val RedAlpha12 = Red.copy(alpha = 0.12f)
val WhiteAlpha30 = White.copy(alpha = 0.3f)
// Bóng mờ của kim chủ đề Vành (#4D000000 của app mẫu).
val BlackAlpha30 = Black.copy(alpha = 0.3f)
val BlackAlpha50 = Black.copy(alpha = 0.5f)
val BlackAlpha70 = Black.copy(alpha = 0.7f)
val BlackAlpha80 = Black.copy(alpha = 0.8f)

// Nền chéo của các màn app (AOD và khung xem trước AOD vẫn đen): mint → đen → tím như viền sáng của icon app. Hai màu đầu cuối là
// mint / tím pha 20% trên nền đen để chữ và thẻ Neutral12 vẫn nổi.
val DarkMint = Color(0xFF192A23)
val DarkPurple = Color(0xFF272133)
val BackgroundGradient = Brush.linearGradient(0f to DarkMint, 0.5f to Black, 1f to DarkPurple)
