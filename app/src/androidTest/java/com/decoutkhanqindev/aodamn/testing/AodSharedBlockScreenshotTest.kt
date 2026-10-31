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
class AodSharedBlockScreenshotTest(
    private val case: SharedBlockCase,
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
    fun matchesApprovedSharedBlockImage() {
        composeRule.setContent {
            ThemeTestLocaleProvider {
                SharedBlockTestContent(case)
            }
        }
        composeRule.onNodeWithTag(ThemeTestCatalog.SHARED_ROOT_TAG).assertExists()
        composeRule.waitForIdle()
        composeRule.activityRule.scenario.onActivity { activity ->
            dropshots.assertSnapshot(
                view = activity.findViewById<View>(android.R.id.content),
                name = case.name,
                filePath = "blocks/${case.component.key}",
            )
        }
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun cases(): List<Array<SharedBlockCase>> = SharedBlockTestCatalog.cases.map { case -> arrayOf(case) }
    }
}