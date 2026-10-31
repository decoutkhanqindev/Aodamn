package com.decoutkhanqindev.aodamn.testing

import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import com.decoutkhanqindev.aodamn.presentation.screens.aodtab.AodTabContent
import com.decoutkhanqindev.aodamn.presentation.screens.aodtab.state.AodTabIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aodtab.state.AodTabState
import com.decoutkhanqindev.aodamn.presentation.theme.Theme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import java.util.Locale

class AodThemeFlowTest {
    private val deviceRule = ThemeDeviceRule()
    private val composeRule = createAndroidComposeRule<ComponentActivity>()

    @get:Rule
    val rules: RuleChain = RuleChain
        .outerRule(deviceRule)
        .around(composeRule)

    @Test
    fun everyRegisteredThemeOpensFromTheHomeGrid() {
        val intents = mutableListOf<AodTabIntent>()
        composeRule.setContent {
            ThemeTestLocaleProvider {
                Theme {
                    AodTabContent(
                        state = AodTabState(isLoading = false, isAodEnabled = false),
                        onIntent = intents::add,
                    )
                }
            }
        }

        selectedThemeExpectations().forEach { expectation ->
            val configuration = Configuration(composeRule.activity.resources.configuration).apply {
                setLocale(Locale.forLanguageTag("vi"))
            }
            val name = composeRule.activity
                .createConfigurationContext(configuration)
                .getString(expectation.theme.nameRes)
            composeRule.onNode(hasScrollAction()).performScrollToNode(hasText(name))
            composeRule.onNodeWithText(name).performClick()
            composeRule.waitForIdle()
            assertEquals(AodTabIntent.OpenTheme(expectation.theme), intents.last())
        }
    }
}