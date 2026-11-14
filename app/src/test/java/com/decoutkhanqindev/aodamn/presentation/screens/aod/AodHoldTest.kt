package com.decoutkhanqindev.aodamn.presentation.screens.aod

import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaUiModel
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class AodHoldTest {

    @Test
    fun `media hold shows hides and re-arms only for track changes`() = runTest {
        val shown = mutableListOf<Boolean>()
        val hold = AodHold(backgroundScope, 30, isSame = { a: MediaUiModel, b -> a.title == b.title && a.artist == b.artist }) { shown += it }
        val first = media(title = "First")

        hold.onChange(first)
        runCurrent()
        assertEquals(listOf(true), shown)

        advanceTimeBy(10_000)
        hold.onChange(first.copy(isPlaying = false))
        advanceTimeBy(20_000)
        runCurrent()
        assertEquals(listOf(true, false), shown)

        hold.onChange(media(title = "Second"))
        runCurrent()
        assertEquals(listOf(true, false, true), shown)

        advanceTimeBy(29_000)
        hold.extend()
        advanceTimeBy(29_000)
        runCurrent()
        assertEquals(listOf(true, false, true), shown)

        advanceTimeBy(1_000)
        runCurrent()
        assertEquals(listOf(true, false, true, false), shown)
    }

    @Test
    fun `toggle and clearing media change visibility`() = runTest {
        val shown = mutableListOf<Boolean>()
        val hold = AodHold(backgroundScope, 30, isSame = { a: MediaUiModel, b -> a.title == b.title && a.artist == b.artist }) { shown += it }

        hold.toggle(true)
        hold.onChange(media())
        runCurrent()
        hold.toggle(false)
        hold.toggle(true)
        hold.onChange(null)
        runCurrent()

        assertEquals(listOf(true, false, true, false), shown)
    }

    @Test
    fun `zero hold never hides media`() = runTest {
        val shown = mutableListOf<Boolean>()
        val hold = AodHold(backgroundScope, 0, isSame = { a: MediaUiModel, b -> a.title == b.title && a.artist == b.artist }) { shown += it }

        hold.onChange(media())
        advanceTimeBy(60_000)
        runCurrent()

        assertEquals(listOf(true), shown)
    }

    @Test
    fun `notification hold tracks notification identity and content`() = runTest {
        val shown = mutableListOf<Boolean>()
        val hold = AodHold(backgroundScope, 30, isSame = { a: Notification, b -> a.key == b.key && a.title == b.title && a.text == b.text }) { shown += it }
        val first = Notification(key = "first", title = "Alice", text = "Hello")

        hold.onChange(first)
        advanceTimeBy(10_000)
        hold.onChange(first.copy())
        advanceTimeBy(20_000)
        runCurrent()
        assertEquals(listOf(true, false), shown)

        hold.onChange(first.copy(key = "second"))
        runCurrent()
        assertEquals(listOf(true, false, true), shown)
        hold.onChange(null)
        assertEquals(listOf(true, false, true, false), shown)

        val neverShown = mutableListOf<Boolean>()
        val neverHold = AodHold(backgroundScope, 0, isSame = { a: Notification, b -> a.key == b.key && a.title == b.title && a.text == b.text }) { neverShown += it }
        neverHold.onChange(first)
        advanceTimeBy(60_000)
        runCurrent()
        assertEquals(listOf(true), neverShown)
    }

    private fun media(title: String = "Song") = MediaUiModel(
        title = title,
        artist = "Artist",
        isPlaying = true,
        canSkipToPrevious = true,
        canSkipToNext = true,
    )

    private data class Notification(
        val key: String,
        val title: String,
        val text: String,
    )
}
