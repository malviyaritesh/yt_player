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

/**
 * An abstract activity which deals with recovering from errors which may occur during API
 * initialization, but can be corrected through user action.
 */
abstract class YouTubeFailureRecoveryActivity : YouTubeBaseActivity(),
    YouTubePlayer.OnInitializedListener {

    protected abstract val youTubePlayerProvider: YouTubePlayer.Provider
    protected abstract val apiKey: String

    companion object {
        private const val RECOVERY_DIALOG_REQUEST_CODE = 1
    }

    override fun onInitializationFailure(
        provider: YouTubePlayer.Provider,
        errorReason: YouTubeInitializationResult
    ) {
        if (errorReason.isUserRecoverableError) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST_CODE).show()
        } else {
            Toast.makeText(
                this,
                "There was an error initializing the player $errorReason",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == RECOVERY_DIALOG_REQUEST_CODE) {
            // Retry initialization if user performed a recovery action
            youTubePlayerProvider.initialize(apiKey, this)
        }
    }
}