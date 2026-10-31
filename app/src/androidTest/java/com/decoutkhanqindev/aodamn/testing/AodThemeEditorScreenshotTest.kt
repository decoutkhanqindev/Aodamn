package com.decoutkhanqindev.aodamn.testing

import android.view.View
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.presentation.components.aod.editor.AodThemeOptionList
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.theme.Black
import com.decoutkhanqindev.aodamn.presentation.theme.Theme
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class AodThemeEditorScreenshotTest(
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
    fun showsEveryEditorPreviewWithoutClipping() {
        composeRule.setContent {
            ThemeTestLocaleProvider {
                Theme {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Black)
                            .padding(16.dp)
                            .testTag(ThemeTestCatalog.EDITOR_ROOT_TAG),
                    ) {
                        AodThemeOptionList(
                            tab = expectation.theme.spec.tabs.first(),
                            config = AodThemeConfig(),
                            onOptionPreview = {},
                            onOptionChange = {},
                        )
                    }
                }
            }
        }
        composeRule.onNodeWithTag(ThemeTestCatalog.EDITOR_ROOT_TAG).assertExists()
        composeRule.waitForIdle()
        composeRule.activityRule.scenario.onActivity { activity ->
            dropshots.assertSnapshot(
                view = activity.findViewById<View>(android.R.id.content),
                name = "${expectation.theme.id}_editor",
                filePath = "themes/${expectation.theme.id}/editor",
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
