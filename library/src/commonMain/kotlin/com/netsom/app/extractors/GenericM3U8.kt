package com.netsom.app.extractors

import com.netsom.app.app
import com.netsom.app.network.WebViewResolver
import com.netsom.app.utils.ExtractorApi
import com.netsom.app.utils.ExtractorLink
import com.netsom.app.utils.M3u8Helper


open class GenericM3U8 : ExtractorApi() {
    override var name = "Upstream"
    override var mainUrl = "https://upstream.to"
    override val requiresReferer = false

    override suspend fun getUrl(url: String, referer: String?): List<ExtractorLink> {
        val response = app.get(
            url, interceptor = WebViewResolver(
                Regex("""master\.m3u8""")
            )
        )
        val sources = mutableListOf<ExtractorLink>()
        if (response.url.contains("m3u8"))
            M3u8Helper.generateM3u8(
                name,
                response.url,
                url,
                headers = response.headers.toMap()
            ).forEach { link ->
                sources.add(link)
            }
        return sources
    }
}