package com.decoutkhanqindev.aodamn.testing

import android.view.View
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class AodThemeOptionScreenshotTest(
    private val expectation: ThemeExpectation,
) {
    private val deviceRule = ThemeDeviceRule()
    private val dropshots = ThemeDropshots.create()
    private val composeRule = createAndroidComposeRule<ComponentActivity>()

    @get:Rule
    val rules: RuleChain = RuleChain
        .outerRule(deviceRule)
        .around(dropshots)
        .around(composeRule)

    @Test
    fun matchesApprovedChoiceBoard() {
        composeRule.setContent {
            ThemeTestLocaleProvider {
                ThemeOptionBoardContent(expectation.theme)
            }
        }
        composeRule.onNodeWithTag(ThemeTestCatalog.OPTION_ROOT_TAG).assertExists()
        composeRule.waitForIdle()
        composeRule.activityRule.scenario.onActivity { activity ->
            dropshots.assertSnapshot(
                view = activity.findViewById<View>(android.R.id.content),
                name = "${expectation.theme.id}_choices",
                filePath = "themes/${expectation.theme.id}/options",
            )
        }
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun cases(): List<Array<ThemeExpectation>> = selectedThemeExpectations()
            .map { expectation -> arrayOf(expectation) }
    }
}