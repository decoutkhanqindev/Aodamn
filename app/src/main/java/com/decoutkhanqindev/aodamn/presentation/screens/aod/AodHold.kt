package com.decoutkhanqindev.aodamn.presentation.screens.aod

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AodHold<T : Any>(
    private val scope: CoroutineScope,
    private val holdSeconds: Int,
    private val isSame: (T, T) -> Boolean,
    private val onShownChange: (Boolean) -> Unit,
) {
    private var value: T? = null
    private var isShown = false
    private var hideJob: Job? = null

    fun onChange(value: T?) {
        val previous = this.value
        this.value = value
        if (value == null) {
            hide()
        } else if (previous == null || !isSame(previous, value)) {
            show()
        }
    }

    fun toggle(isShown: Boolean) {
        if (isShown && value != null) show() else if (!isShown) hide()
    }

    fun extend() {
        if (isShown) show()
    }

    private fun show() {
        hideJob?.cancel()
        setShown(true)
        if (holdSeconds > 0) {
            hideJob = scope.launch {
                delay(holdSeconds * 1_000L)
                hide()
            }
        }
    }

    private fun hide() {
        hideJob?.cancel()
        setShown(false)
    }

    private fun setShown(isShown: Boolean) {
        if (this.isShown == isShown) return
        this.isShown = isShown
        onShownChange(isShown)
    }
}
