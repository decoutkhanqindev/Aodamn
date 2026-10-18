package com.decoutkhanqindev.aodamn.utils

interface Tag {
    val tag: String get() = this::class.simpleName ?: ""
}
