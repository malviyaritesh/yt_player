package com.owliverse.yt_player;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class YtPlayerWebViewActivity extends AppCompatActivity {

    YouTubePlayer player;
    YouTubePlayerTracker playerTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yt_player_webview);

        final YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);

        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                final Intent intent = getIntent();
                final boolean autoPlay = intent.getBooleanExtra("autoPlay", false);
                final String videoId = intent.getStringExtra("videoId");
                if (autoPlay) {
                    youTubePlayer.loadVideo(videoId, 0);
                } else {
                    youTubePlayer.cueVideo(videoId, 0);
                }
                player = youTubePlayer;
                playerTracker = new YouTubePlayerTracker();
                youTubePlayer.addListener(playerTracker);
            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean dispatchFirst = super.dispatchKeyEvent(event);
        if (player != null && event.getAction() == KeyEvent.ACTION_UP) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_CENTER:
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                    if (playerTracker.getState() == PlayerConstants.PlayerState.PLAYING) {
                        player.pause();
                    } else {
                        player.play();
                    }
                    break;
                case KeyEvent.KEYCODE_MEDIA_PLAY:
                    player.play();
                    break;
                case KeyEvent.KEYCODE_MEDIA_PAUSE:
                    player.pause();
                    break;
                case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                    player.seekTo(getPlayerSeekSeconds(true));
                    break;
                case KeyEvent.KEYCODE_MEDIA_REWIND:
                    player.seekTo(getPlayerSeekSeconds(false));
                    break;
            }
        }

        return dispatchFirst;
    }

    private float getPlayerSeekSeconds(boolean forward) {
        final float currentSeconds = playerTracker.getCurrentSecond();
        float seekSeconds = 10;
        if (forward) {
            final float duration = playerTracker.getVideoDuration();
            if (currentSeconds + seekSeconds > duration) {
                return duration;
            }
            return currentSeconds + seekSeconds;
        } else if (currentSeconds - seekSeconds <= 0) {
            return 0;
        }
        return currentSeconds - seekSeconds;
    }
}
