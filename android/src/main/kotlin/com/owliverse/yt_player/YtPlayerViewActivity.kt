package com.owliverse.yt_player

import android.content.Context
import android.content.Intent
import com.google.android.youtube.player.YouTubeApiServiceUtil
import com.google.android.youtube.player.YouTubeInitializationResult
import com.owliverse.yt_player.YtPlayerViewActivity
import com.google.android.youtube.player.YouTubeIntents
import com.owliverse.yt_player.YtPlayerWebViewActivity
import com.owliverse.yt_player.YtPlayerPlugin
import com.owliverse.yt_player.YouTubeFailureRecoveryActivity
import android.os.Bundle
import com.owliverse.yt_player.R
import com.google.android.youtube.player.YouTubePlayerView
import android.view.View
import com.google.android.youtube.player.YouTubePlayer
import androidx.appcompat.app.AppCompatActivity
import android.view.KeyEvent
import com.google.android.youtube.player.YouTubeBaseActivity
import android.widget.Toast

class YtPlayerViewActivity : YouTubeFailureRecoveryActivity() {

    private lateinit var videoId: String
    private var autoPlay = false

    override lateinit var apiKey: String
    override lateinit var youTubePlayerProvider: YouTubePlayer.Provider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yt_player_view)
        apiKey = intent.getStringExtra("apiKey")!!
        videoId = intent.getStringExtra("videoId")!!
        autoPlay = intent.getBooleanExtra("autoPlay", false)
        findViewById<YouTubePlayerView>(R.id.youtube_view).let {
            youTubePlayerProvider = it
            it.initialize(apiKey, this)
        }
    }

    override fun onInitializationSuccess(
        provider: YouTubePlayer.Provider,
        youTubePlayer: YouTubePlayer,
        wasRestored: Boolean
    ) {
        if (!wasRestored) {
            youTubePlayer.setFullscreen(true)
            youTubePlayer.setShowFullscreenButton(false)
            if (autoPlay) {
                youTubePlayer.loadVideo(videoId)
            } else {
                youTubePlayer.cueVideo(videoId)
            }
        }
    }
}