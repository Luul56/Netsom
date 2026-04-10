package com.netsom.app.extractors

import com.fasterxml.jackson.annotation.JsonProperty
import com.netsom.app.app
import com.netsom.app.utils.*
import com.netsom.app.utils.AppUtils.tryParseJson
import com.netsom.app.utils.JsUnpacker
import com.netsom.app.utils.ExtractorApi
import com.netsom.app.utils.ExtractorLink
import com.netsom.app.utils.Qualities
import com.netsom.app.utils.getQualityFromName
import java.net.URI


open class Vtbe : ExtractorApi() {
    override var name = "Vtbe"
    override var mainUrl = "https://vtbe.to"
    override val requiresReferer = true

    override suspend fun getUrl(url: String, referer: String?): List<ExtractorLink>? {
        val response = app.get(url,referer=mainUrl).document
        val extractedpack =response.selectFirst("script:containsData(function(p,a,c,k,e,d))")?.data().toString()
        JsUnpacker(extractedpack).unpack()?.let { unPacked ->
            Regex("sources:\\[\\{file:\"(.*?)\"").find(unPacked)?.groupValues?.get(1)?.let { link ->
                return listOf(
                    newExtractorLink(
                        this.name,
                        this.name,
                        link,
                    ) {
                        this.referer = referer ?: ""
                        this.quality = Qualities.Unknown.value
                    }
                )
            }
        }
        return null
    }
}
