package com.netsom.app.extractors

import com.fasterxml.jackson.annotation.JsonProperty
import com.netsom.app.app
import com.netsom.app.utils.AppUtils.tryParseJson
import com.netsom.app.utils.ExtractorApi
import com.netsom.app.utils.ExtractorLink
import com.netsom.app.utils.getAndUnpack
import com.netsom.app.utils.Qualities
import com.netsom.app.utils.M3u8Helper

open class StreamoUpload : ExtractorApi() {
    override val name = "StreamoUpload"
    override val mainUrl = "https://streamoupload.xyz"
    override val requiresReferer = true

    override suspend fun getUrl(url: String, referer: String?): List<ExtractorLink> {
        val sources = mutableListOf<ExtractorLink>()
        val response = app.get(url, referer = referer)
        val scriptElements = response.document.select("script").map { script ->
            if (script.data().contains("eval(function(p,a,c,k,e,d)")) {
                val data = getAndUnpack(script.data())
                    .substringAfter("sources:[")
                    .substringBefore("],")
                    .replace("file", "\"file\"")
                    .trim()
                tryParseJson<File>(data)?.let {
                    M3u8Helper.generateM3u8(
                        name,
                        it.file,
                        "$mainUrl/",
                    ).forEach { m3uData -> sources.add(m3uData) }
                }
            }
        }
        return sources
    }

    private data class File(
        @JsonProperty("file") val file: String,
    )
}
