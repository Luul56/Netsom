package com.netsom.app.extractors

import com.netsom.app.SubtitleFile
import com.netsom.app.app
import com.netsom.app.utils.ExtractorApi
import com.netsom.app.utils.ExtractorLink
import com.netsom.app.utils.Qualities
import com.netsom.app.utils.newExtractorLink

class GoodstreamExtractor : ExtractorApi() {
    override var name = "Goodstream"
    override val mainUrl = "https://goodstream.uno"
    override val requiresReferer = false

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        app.get(url).document.select("script").map { script ->
            if (script.data().contains(Regex("file|player"))) {
                val urlRegex = Regex("file: \"(https:\\/\\/[a-z0-9.\\/-_?=&]+)\",")
                urlRegex.find(script.data())?.groupValues?.get(1).let { link ->
                    callback.invoke(
                        newExtractorLink(
                            name,
                            name,
                            link!!,
                        ) {
                            this.referer = mainUrl
                        }
                    )
                }
            }
        }
    }
}