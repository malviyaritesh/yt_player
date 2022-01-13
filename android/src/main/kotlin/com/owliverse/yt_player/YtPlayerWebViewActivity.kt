package com.owliverse.yt_player

import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class YtPlayerWebViewActivity : AppCompatActivity() {
    var player: YouTubePlayer? = null
    lateinit var playerTracker: YouTubePlayerTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yt_player_webview)
        val youTubePlayerView: YouTubePlayerView = findViewById(R.id.youtube_player_view)
        youTubePlayerView
        lifecycle.addObserver(youTubePlayerView)
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val autoPlay = intent.getBooleanExtra("autoPlay", false)
                val videoId = intent.getStringExtra("videoId")!!
                if (autoPlay) {
                    youTubePlayer.loadVideo(videoId, 0F)
                } else {
                    youTubePlayer.cueVideo(videoId, 0F)
                }
                player = youTubePlayer
                playerTracker = YouTubePlayerTracker()
                youTubePlayer.addListener(playerTracker)
            }
        })
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        val dispatchFirst = super.dispatchKeyEvent(event)
        if (event.action == KeyEvent.ACTION_UP) {
            player?.let {
                when (event.keyCode) {
                    KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE -> {
                        if (playerTracker.state === PlayerConstants.PlayerState.PLAYING) {
                            it.pause()
                        } else {
                            it.play()
                        }
                    }
                    KeyEvent.KEYCODE_MEDIA_PLAY -> it.play()
                    KeyEvent.KEYCODE_MEDIA_PAUSE -> it.pause()
                    KeyEvent.KEYCODE_MEDIA_FAST_FORWARD -> it.seekTo(getPlayerSeekSeconds(true))
                    KeyEvent.KEYCODE_MEDIA_REWIND -> it.seekTo(getPlayerSeekSeconds(false))
                    else -> null
                }
            }
        }
        return dispatchFirst
    }

    private fun getPlayerSeekSeconds(forward: Boolean): Float {
        val currentSeconds: Float = playerTracker.currentSecond
        val seekSeconds = 10f
        if (forward) {
            val duration: Float = playerTracker.videoDuration
            return if (currentSeconds + seekSeconds > duration) {
                duration
            } else currentSeconds + seekSeconds
        } else if (currentSeconds - seekSeconds <= 0) {
            return 0F
        }
        return currentSeconds - seekSeconds
    }
}