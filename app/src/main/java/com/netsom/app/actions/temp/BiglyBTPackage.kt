package com.netsom.app.actions.temp

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.netsom.app.actions.OpenInAppAction
import com.netsom.app.ui.result.LinkLoadingResult
import com.netsom.app.ui.result.ResultEpisode
import com.netsom.app.utils.ExtractorLinkType
import com.netsom.app.utils.txt

/** https://github.com/BiglySoftware/BiglyBT-Android */
class BiglyBTPackage : OpenInAppAction(
    appName = txt("BiglyBT"),
    packageName = "com.biglybt.android.client",
    intentClass = "com.biglybt.android.client.activity.IntentHandler"
) {
    // Only torrents are supported by the app
    override val sourceTypes: Set<ExtractorLinkType> =
        setOf(ExtractorLinkType.MAGNET, ExtractorLinkType.TORRENT)

    override val oneSource: Boolean = true

    override suspend fun putExtra(
        context: Context,
        intent: Intent,
        video: ResultEpisode,
        result: LinkLoadingResult,
        index: Int?
    ) {
        intent.data = result.links[index!!].url.toUri()
    }

    override fun onResult(activity: Activity, intent: Intent?) = Unit
}