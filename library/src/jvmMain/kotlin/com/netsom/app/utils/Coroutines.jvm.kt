package com.netsom.app.utils

actual fun runOnMainThreadNative(work: () -> Unit) {
    work.invoke()
}