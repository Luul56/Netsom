package com.netsom.app.metaproviders

import com.fasterxml.jackson.annotation.JsonProperty
import com.netsom.app.APIHolder.apis
import com.netsom.app.APIHolder.getApiFromNameNull
import com.netsom.app.ErrorLoadingException
import com.netsom.app.LoadResponse
import com.netsom.app.MovieLoadResponse
import com.netsom.app.MovieSearchResponse
import com.netsom.app.SearchResponseList
import com.netsom.app.SubtitleFile
import com.netsom.app.TvType
import com.netsom.app.amap
import com.netsom.app.mvvm.logError
import com.netsom.app.toNewSearchResponseList
import com.netsom.app.utils.AppUtils.toJson
import com.netsom.app.utils.AppUtils.tryParseJson
import com.netsom.app.utils.ExtractorLink

class CrossTmdbProvider : TmdbProvider() {
    override var name = "MultiMovie"
    override val apiName = "MultiMovie"
    override var lang = "en"
    override val useMetaLoadResponse = true
    override val usesWebView = true
    override val supportedTypes = setOf(TvType.Movie)

    private fun filterName(name: String): String {
        return Regex("""[^a-zA-Z0-9-]""").replace(name, "")
    }

    private val validApis
        get() =
            synchronized(apis) { apis.filter { it.lang == this.lang && it::class.java != this::class.java } }
    //.distinctBy { it.uniqueId }


    data class CrossMetaData(
        @JsonProperty("isSuccess") val isSuccess: Boolean,
        @JsonProperty("movies") val movies: List<Pair<String, String>>? = null,
    )

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        tryParseJson<CrossMetaData>(data)?.let { metaData ->
            if (!metaData.isSuccess) return false
            metaData.movies?.amap { (apiName, data) ->
                getApiFromNameNull(apiName)?.let {
                    try {
                        it.loadLinks(data, isCasting, subtitleCallback, callback)
                    } catch (e: Exception) {
                        logError(e)
                    }
                }
            }

            return true
        }
        return false
    }

    override suspend fun search(query: String, page: Int): SearchResponseList? {
        // TODO REMOVE
        return super.search(query, page)
            ?.items
            ?.filterIsInstance<MovieSearchResponse>()
            ?.toNewSearchResponseList()
    }

    override suspend fun load(url: String): LoadResponse? {
        val base = super.load(url)?.apply {
            this.recommendations =
                this.recommendations?.filterIsInstance<MovieSearchResponse>() // TODO REMOVE
            val matchName = filterName(this.name)
            when (this) {
                is MovieLoadResponse -> {
                    val data = validApis.amap { api ->
                        try {
                            if (api.supportedTypes.contains(TvType.Movie)) { //|| api.supportedTypes.contains(TvType.AnimeMovie)
                                return@amap api.search(this.name)?.first {
                                    if (filterName(it.name).equals(
                                            matchName,
                                            ignoreCase = true
                                        )
                                    ) {
                                        if (it is MovieSearchResponse)
                                            if (it.year != null && this.year != null && it.year != this.year) // if year exist then do a check
                                                return@first false

                                        return@first true
                                    }
                                    false
                                }?.let { search ->
                                    val response = api.load(search.url)
                                    if (response is MovieLoadResponse) {
                                        response
                                    } else {
                                        null
                                    }
                                }
                            }
                            null
                        } catch (e: Exception) {
                            logError(e)
                            null
                        }
                    }.filterNotNull()
                    this.dataUrl =
                        CrossMetaData(true, data.map { it.apiName to it.dataUrl }).toJson()
                }

                else -> {
                    throw ErrorLoadingException("Nothing besides movies are implemented for this provider")
                }
            }
        }

        return base
    }
}