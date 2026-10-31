package com.decoutkhanqindev.aodamn.testing

import android.os.Environment
import androidx.test.platform.app.InstrumentationRegistry
import com.dropbox.dropshots.CountValidator
import com.dropbox.dropshots.Dropshots
import java.io.File

object ThemeDropshots {
    fun create(maxDifferentPixels: Int = 0): Dropshots {
        val testPackage = InstrumentationRegistry.getInstrumentation().context.packageName
        val root = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "screenshots/$testPackage",
        )
        return Dropshots(
            recordScreenshots = File(root, ".isRecordingScreenshots").exists(),
            resultValidator = CountValidator(maxDifferentPixels),
            rootScreenshotDirectory = root,
        )
    }
}