package com.owliverse.yt_player

import android.content.Context
import android.content.Intent
import androidx.annotation.NonNull
import com.google.android.youtube.player.YouTubeApiServiceUtil
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubeIntents
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

/**
 * YtPlayerPlugin
 */
class YtPlayerPlugin : FlutterPlugin, MethodChannel.MethodCallHandler {
    private lateinit var appContext: Context
    private lateinit var methodChannel: MethodChannel

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        methodChannel = MethodChannel(flutterPluginBinding.binaryMessenger, "com.owliverse/yt_player")
        appContext = flutterPluginBinding.applicationContext
        methodChannel.setMethodCallHandler(this)
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: MethodChannel.Result) {
        if (call.method == "playYouTubeVideo") {
            result.success(createIntentAndStartYouTube(call))
        } else {
            result.notImplemented()
        }
    }

    private fun createIntentAndStartYouTube(call: MethodCall): Boolean {
        val apiKey: String = call.argument("apiKey")!!
        val videoId: String = call.argument("videoId")!!
        val autoPlay: Boolean = call.argument("autoPlay")!!
        val intent: Intent
        if (YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(appContext) == YouTubeInitializationResult.SUCCESS) {
            intent = Intent(appContext, YtPlayerViewActivity::class.java)
            intent.putExtra("apiKey", apiKey)
            intent.putExtra("videoId", videoId)
            intent.putExtra("autoPlay", autoPlay)
        } else if (YouTubeIntents.canResolvePlayVideoIntent(appContext)) {
            intent = YouTubeIntents.createPlayVideoIntentWithOptions(
                appContext,
                videoId,
                true,
                true
            )
        } else {
            intent = Intent(appContext, YtPlayerWebViewActivity::class.java)
            intent.putExtra("apiKey", apiKey)
            intent.putExtra("videoId", videoId)
            intent.putExtra("autoPlay", autoPlay)
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        appContext.startActivity(intent)
        return true
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        methodChannel.setMethodCallHandler(null)
    }
}