package com.decoutkhanqindev.aodamn.testing

import android.content.Intent
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.captureToImage
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
class AodThemeMotionTest(
    private val expectation: ThemeExpectation,
) {
    private val deviceRule = ThemeDeviceRule()
    private val dropshots = ThemeDropshots.create(maxDifferentPixels = 16)
    private val composeRule = createEmptyComposeRule()

    @get:Rule
    val rules: RuleChain = RuleChain
        .outerRule(deviceRule)
        .around(dropshots)
        .around(composeRule)

    @Test
    fun matchesEveryApprovedMotionFrame() {
        val motion = checkNotNull(expectation.motion)
        ThemeTestActivity.initialState = ThemeTestFixtures.state(
            theme = expectation.theme,
            profile = ThemeRenderProfile.REFERENCE,
            minute = motion.fromMinute,
        )
        ThemeTestActivity.initialLandscape = false
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(context, ThemeTestActivity::class.java)

        ActivityScenario.launch<ThemeTestActivity>(intent).use { scenario ->
            composeRule.onNodeWithTag(ThemeTestActivity.ROOT_TAG).assertExists()
            composeRule.waitForIdle()
            assertFrame(millis = 0L)
            composeRule.mainClock.autoAdvance = false
            scenario.onActivity { activity ->
                activity.render(
                    ThemeTestFixtures.state(
                        theme = expectation.theme,
                        profile = ThemeRenderProfile.REFERENCE,
                        minute = motion.toMinute,
                    ),
                )
            }
            composeRule.waitForIdle()

            var elapsedMillis = 0L
            motion.checkpointsMillis.forEach { checkpointMillis ->
                composeRule.mainClock.advanceTimeBy(checkpointMillis - elapsedMillis)
                composeRule.waitForIdle()
                assertFrame(millis = checkpointMillis)
                elapsedMillis = checkpointMillis
            }
        }
    }

    private fun assertFrame(millis: Long) {
        dropshots.assertSnapshot(
            bitmap = composeRule
                .onNodeWithTag(ThemeTestActivity.ROOT_TAG)
                .captureToImage()
                .asAndroidBitmap(),
            name = "${expectation.theme.id}_motion_$millis",
            filePath = "themes/${expectation.theme.id}/motion",
        )
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun cases(): List<Array<ThemeExpectation>> = selectedThemeExpectations()
            .filter { expectation -> expectation.motion != null }
            .map { expectation -> arrayOf(expectation) }
    }
}