package com.netsom.app.extractors

import com.fasterxml.jackson.annotation.JsonProperty
import com.netsom.app.SubtitleFile
import com.netsom.app.app
import com.netsom.app.newSubtitleFile
import com.netsom.app.utils.ExtractorApi
import com.netsom.app.utils.ExtractorLink
import com.netsom.app.utils.ExtractorLinkType
import com.netsom.app.utils.newExtractorLink

class Streamix(): Streamup() {
    override val name: String = "Streamix"
    override val mainUrl = "https://streamix.so"
}

class Vidara(): Streamup() {
    override val name: String = "Vidara"
    override val mainUrl = "https://vidara.to"
    override val apiPath: String = "/api/stream"
}

open class Streamup() : ExtractorApi() {
    override val name: String = "Streamup"
    override val mainUrl: String = "https://strmup.to"
    override val requiresReferer: Boolean = false
    open val apiPath: String = "/ajax/stream"

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val fileCode = url.substringAfterLast("/")
        val fileInfo = app.get("$mainUrl$apiPath?filecode=$fileCode")
            .parsed<StreamUpFileInfo>()

        callback.invoke(
            newExtractorLink(
                source = name,
                name = name,
                url = fileInfo.streamingUrl,
                type = ExtractorLinkType.M3U8
            )
        )

        fileInfo.subtitles?.forEach { subtitle ->
            subtitleCallback.invoke(
                newSubtitleFile(subtitle.language, subtitle.filePath)
            )
        }
    }

    private data class StreamUpFileInfo(
        val title: String,
        val thumbnail: String,
        @JsonProperty("streaming_url")
        val streamingUrl: String,
        val subtitles: List<StreamUpSubtitle>?
    )

    private data class StreamUpSubtitle(
        @JsonProperty("file_path")
        val filePath: String,
        @JsonProperty("language")
        val language: String,
    )
}
