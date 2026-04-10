package com.netsom.app.extractors

import com.netsom.app.SubtitleFile
import com.netsom.app.utils.*
import com.netsom.app.app
import com.netsom.app.utils.M3u8Helper.Companion.generateM3u8

open class Sendvid : ExtractorApi() {
    override var name = "Sendvid"
    override val mainUrl = "https://sendvid.com"
    override val requiresReferer = false
    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val doc = app.get(url).document
        val urlString = doc.select("head meta[property=og:video:secure_url]").attr("content")
        if (urlString.contains("m3u8"))  {
            generateM3u8(
                name,
                urlString,
                mainUrl,
            ).forEach(callback)
        }
    }
}