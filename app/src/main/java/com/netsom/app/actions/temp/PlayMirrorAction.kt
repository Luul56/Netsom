package com.netsom.app.actions.temp

import android.app.Activity
import android.content.Context
import com.netsom.app.R
import com.netsom.app.actions.VideoClickAction
import com.netsom.app.ui.player.ExtractorUri
import com.netsom.app.ui.player.GeneratorPlayer
import com.netsom.app.ui.player.LOADTYPE_INAPP
import com.netsom.app.ui.player.SubtitleData
import com.netsom.app.ui.player.VideoGenerator
import com.netsom.app.ui.result.LinkLoadingResult
import com.netsom.app.ui.result.ResultEpisode
import com.netsom.app.utils.ExtractorLink
import com.netsom.app.utils.ExtractorLinkType
import com.netsom.app.utils.UIHelper.navigate
import com.netsom.app.utils.txt

class PlayMirrorAction : VideoClickAction() {
    override val name = txt(R.string.episode_action_play_mirror)

    override val oneSource = true

    override val isPlayer = true

    override val sourceTypes: Set<ExtractorLinkType> = LOADTYPE_INAPP

    override fun shouldShow(context: Context?, video: ResultEpisode?) = true

    override suspend fun runAction(
        context: Context?,
        video: ResultEpisode,
        result: LinkLoadingResult,
        index: Int?
    ) {
        //Implemented a generator to handle the single
        val activity = context as? Activity ?: return
        val generatorMirror = object : VideoGenerator<ResultEpisode>(listOf(video)) {
            override val hasCache: Boolean = false
            override val canSkipLoading: Boolean = false

            override suspend fun generateLinks(
                clearCache: Boolean,
                sourceTypes: Set<ExtractorLinkType>,
                callback: (Pair<ExtractorLink?, ExtractorUri?>) -> Unit,
                subtitleCallback: (SubtitleData) -> Unit,
                offset: Int,
                isCasting: Boolean
            ): Boolean {
                index?.let { callback(result.links[it] to null) }
                result.subs.forEach { subtitle -> subtitleCallback(subtitle) }
                return true
            }
        }

        activity.navigate(
            R.id.global_to_navigation_player,
            GeneratorPlayer.newInstance(
                generatorMirror, result.syncData
            )
        )
    }
}