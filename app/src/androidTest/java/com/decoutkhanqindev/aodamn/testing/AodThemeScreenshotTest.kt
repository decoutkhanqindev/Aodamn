package com.decoutkhanqindev.aodamn.testing

import android.content.Intent
import androidx.compose.ui.test.junit4.v2.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class AodThemeScreenshotTest(
    private val expectation: ThemeExpectation,
) {
    private val deviceRule = ThemeDeviceRule()
    private val dropshots = ThemeDropshots.create()
    private val composeRule = createEmptyComposeRule()

    @get:Rule
    val rules: RuleChain = RuleChain
        .outerRule(deviceRule)
        .around(dropshots)
        .around(composeRule)

    @Test
    fun matchesEightApprovedThemeImages() {
        listOf(false, true).forEach { isLandscape ->
            val initial = ThemeScreenshotCase(
                theme = expectation.theme,
                profile = ThemeRenderProfile.REFERENCE,
                isLandscape = isLandscape,
            )
            ThemeTestActivity.initialState = ThemeTestFixtures.state(initial)
            ThemeTestActivity.initialLandscape = isLandscape
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            val intent = Intent(context, ThemeTestActivity::class.java)

            ActivityScenario.launch<ThemeTestActivity>(intent).use { scenario ->
                composeRule.onNodeWithTag(ThemeTestActivity.ROOT_TAG).assertExists()
                composeRule.waitForIdle()

                ThemeRenderProfile.entries.forEach { profile ->
                    val case = ThemeScreenshotCase(
                        theme = expectation.theme,
                        profile = profile,
                        isLandscape = isLandscape,
                    )
                    scenario.onActivity { activity -> activity.render(ThemeTestFixtures.state(case)) }
                    composeRule.waitForIdle()
                    scenario.onActivity { activity ->
                        dropshots.assertSnapshot(
                            view = activity.findViewById(android.R.id.content),
                            name = case.name,
                            filePath = "themes/${case.theme.id}",
                        )
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun cases(): List<Array<ThemeExpectation>> = selectedThemeExpectations()
            .map { expectation -> arrayOf(expectation) }
    }
}