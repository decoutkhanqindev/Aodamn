package com.decoutkhanqindev.aodamn.testing

import androidx.test.platform.app.InstrumentationRegistry
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(AodThemeTestSuiteRunner::class)
class AodThemeTestSuite

class AodThemeTestSuiteRunner(testClass: Class<*>) : Suite(testClass, selectedClasses()) {
    companion object {
        private fun selectedClasses(): Array<Class<*>> {
            val selected = InstrumentationRegistry.getArguments()
                .getString("testGroups")
                ?.split('+')
                ?.toSet()
                .orEmpty()
            val runAll = selected.isEmpty()
            return buildList {
                if (runAll || "behavior" in selected) {
                    add(AodThemeFlowTest::class.java)
                    add(AodThemeEditorBehaviorTest::class.java)
                }
                if (runAll || "screens" in selected) add(AodThemeScreenshotTest::class.java)
                if (runAll || "options" in selected) add(AodThemeOptionScreenshotTest::class.java)
                if (runAll || "editor" in selected) add(AodThemeEditorScreenshotTest::class.java)
                if (runAll || "motion" in selected) add(AodThemeMotionTest::class.java)
            }.toTypedArray()
        }
    }
}
