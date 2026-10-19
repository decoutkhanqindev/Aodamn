package com.decoutkhanqindev.aodamn.presentation.model.aod.appearance

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.theme.AmazonOrange
import com.decoutkhanqindev.aodamn.presentation.theme.DiscordBlurple
import com.decoutkhanqindev.aodamn.presentation.theme.DuolingoGreen
import com.decoutkhanqindev.aodamn.presentation.theme.Grey9A
import com.decoutkhanqindev.aodamn.presentation.theme.InstagramMagenta
import com.decoutkhanqindev.aodamn.presentation.theme.Mint
import com.decoutkhanqindev.aodamn.presentation.theme.SnapchatYellow
import com.decoutkhanqindev.aodamn.presentation.theme.SoundcloudOrange
import com.decoutkhanqindev.aodamn.presentation.theme.SpotifyGreen
import com.decoutkhanqindev.aodamn.presentation.theme.TelegramBlue
import com.decoutkhanqindev.aodamn.presentation.theme.TiktokCyan
import com.decoutkhanqindev.aodamn.presentation.theme.TinderPink
import com.decoutkhanqindev.aodamn.presentation.theme.TwitchPurple
import com.decoutkhanqindev.aodamn.presentation.theme.White
import com.decoutkhanqindev.aodamn.presentation.theme.YoutubeRed
import com.decoutkhanqindev.aodamn.presentation.theme.ZaloBlue

// Màu có sẵn phía trên vòng màu, 2 hàng × 8 ô theo đúng thứ tự khai báo; chọn ô chỉ ghi màu ARGB như kéo vòng màu nên không lưu mã riêng.
@Immutable
enum class ColorSwatchValue(
    val color: Color,
    @param:StringRes val labelRes: Int,
) {
    WHITE(color = White, labelRes = R.string.color_swatch_white),
    GREY(color = Grey9A, labelRes = R.string.color_swatch_grey),
    YOUTUBE(color = YoutubeRed, labelRes = R.string.color_swatch_youtube),
    SOUNDCLOUD(color = SoundcloudOrange, labelRes = R.string.color_swatch_soundcloud),
    AMAZON(color = AmazonOrange, labelRes = R.string.color_swatch_amazon),
    SNAPCHAT(color = SnapchatYellow, labelRes = R.string.color_swatch_snapchat),
    DUOLINGO(color = DuolingoGreen, labelRes = R.string.color_swatch_duolingo),
    SPOTIFY(color = SpotifyGreen, labelRes = R.string.color_swatch_spotify),
    MINT(color = Mint, labelRes = R.string.color_swatch_mint),
    TIKTOK(color = TiktokCyan, labelRes = R.string.color_swatch_tiktok),
    TELEGRAM(color = TelegramBlue, labelRes = R.string.color_swatch_telegram),
    ZALO(color = ZaloBlue, labelRes = R.string.color_swatch_zalo),
    DISCORD(color = DiscordBlurple, labelRes = R.string.color_swatch_discord),
    TWITCH(color = TwitchPurple, labelRes = R.string.color_swatch_twitch),
    INSTAGRAM(color = InstagramMagenta, labelRes = R.string.color_swatch_instagram),
    TINDER(color = TinderPink, labelRes = R.string.color_swatch_tinder),
    ;

    companion object {
        const val COLUMN_COUNT = 8
    }
}
