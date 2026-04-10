package com.netsom.app.extractors

import com.netsom.app.app
import com.netsom.app.utils.ExtractorApi
import com.netsom.app.utils.ExtractorLink
import com.netsom.app.utils.ExtractorLinkType
import com.netsom.app.utils.INFER_TYPE
import com.netsom.app.utils.JsUnpacker
import com.netsom.app.utils.Qualities
import com.netsom.app.utils.newExtractorLink
import java.net.URI

open class Streamhub : ExtractorApi() {
    override var mainUrl = "https://streamhub.to"
    override var name = "Streamhub"
    override val requiresReferer = false

    override fun getExtractorUrl(id: String): String {
        return "$mainUrl/e/$id"
    }

    override suspend fun getUrl(url: String, referer: String?): List<ExtractorLink>? {
        val response = app.get(url).text
        Regex("eval((.|\\n)*?)</script>").find(response)?.groupValues?.get(1)?.let { jsEval ->
            JsUnpacker("eval$jsEval").unpack()?.let { unPacked ->
                Regex("sources:\\[\\{src:\"(.*?)\"").find(unPacked)?.groupValues?.get(1)?.let { link ->
                    return listOf(
                        newExtractorLink(
                            source = this.name,
                            this.name,
                            link,
                        )
                    )
                }
            }
        }
        return null
    }
}