package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.words

import android.icu.text.MessageFormat
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.words.WordsThemeSpec
import java.util.Locale

@Composable
fun AodWordsClock(
    nowMillis: Long,
    config: AodThemeConfig,
    modifier: Modifier = Modifier,
) {
    val time = rememberZonedDateTime(nowMillis)
    val words = rememberNumberWords()
    val hour = words((time.hour + 11) % 12 + 1)
    val minute = time.minute.takeIf { value -> value > 0 }?.let(words)
    val description = time.format(rememberTimeFormatter())
    val slot = config[WordsThemeSpec.Size].dp
    val weight = config[WordsThemeSpec.Weight]

    Column(modifier = modifier.clearAndSetSemantics { contentDescription = description }) {
        AodWordsLine(text = hour, slot = slot, weight = weight, config = config, colorOption = WordsThemeSpec.HourColor)

        AnimatedVisibility(visible = minute != null) {
            AodWordsLine(text = minute.orEmpty(), slot = slot, weight = weight, config = config, colorOption = WordsThemeSpec.MinuteColor)
        }
    }
}

@Composable
private fun rememberNumberWords(): (Int) -> String {
    val locale = LocalConfiguration.current.locales[0]
    val pattern = stringResource(R.string.aod_words_spellout_pattern)
    return remember(pattern, locale) { numberWords(pattern = pattern, locale = locale) }
}

private fun numberWords(
    pattern: String,
    locale: Locale,
): (Int) -> String {
    val format = MessageFormat(pattern, locale)
    val isEnglish = locale.language == Locale.ENGLISH.language

    return { number ->
        val text = format.format(arrayOf<Any>(number))
        val words = if (isEnglish) text.split(' ', '-') else listOf(text)
        words.joinToString(separator = " ") { word -> word.replaceFirstChar { char -> char.titlecase(locale) } }
    }
}

@Preview(widthDp = 313)
@Composable
private fun AodWordsClockPreview() {
    AodWordsClock(nowMillis = System.currentTimeMillis(), config = AodThemeConfig())
}
