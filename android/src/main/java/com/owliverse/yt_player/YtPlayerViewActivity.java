package com.owliverse.yt_player;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class YtPlayerViewActivity extends YouTubeFailureRecoveryActivity {

    private String apiKey;
    private String videoId;
    private boolean autoPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yt_player_view);

        final Intent intent = getIntent();
        apiKey = intent.getStringExtra("apiKey");
        videoId = intent.getStringExtra("videoId");
        autoPlay = intent.getBooleanExtra("autoPlay", false);
        YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(getApiKey(), this);
    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }

    @Override
    protected String getApiKey() {
        return apiKey;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        if (!wasRestored) {
            youTubePlayer.setFullscreen(true);
            youTubePlayer.setShowFullscreenButton(false);
            if (autoPlay) {
                youTubePlayer.loadVideo(videoId);
            } else {
                youTubePlayer.cueVideo(videoId);
            }
        }
    }
}
