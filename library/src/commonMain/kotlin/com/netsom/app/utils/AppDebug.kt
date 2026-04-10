package com.netsom.app.utils

import com.netsom.app.InternalAPI

@InternalAPI
object AppDebug {
    @Volatile
    var isDebug: Boolean = false
}
