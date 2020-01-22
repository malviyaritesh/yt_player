package com.owliverse.yt_player;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeIntents;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * YtPlayerPlugin
 */
public class YtPlayerPlugin implements FlutterPlugin, MethodCallHandler {

    private Context applicationContext;
    private MethodChannel methodChannel;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        onAttachedToEngine(flutterPluginBinding.getApplicationContext(), flutterPluginBinding.getBinaryMessenger());
    }

    private void onAttachedToEngine(Context applicationContext, BinaryMessenger messenger) {
        this.applicationContext = applicationContext;
        methodChannel = new MethodChannel(messenger, "com.owliverse/yt_player");
        methodChannel.setMethodCallHandler(this);
    }

    // This static function is optional and equivalent to onAttachedToEngine. It supports the old
    // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
    // plugin registration via this function while apps migrate to use the new Android APIs
    // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
    //
    // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
    // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
    // depending on the user's project. onAttachedToEngine or registerWith must both be defined
    // in the same class.
    public static void registerWith(Registrar registrar) {
        final YtPlayerPlugin instance = new YtPlayerPlugin();
        instance.onAttachedToEngine(registrar.context(), registrar.messenger());
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals("createIntentAndStartYouTube")) {
            result.success(createIntentAndStartYouTube(call));
        } else {
            result.notImplemented();
        }
    }

    private boolean createIntentAndStartYouTube(MethodCall call) {
        final String apiKey = call.argument("apiKey");
        final String videoId = call.argument("videoId");
        final boolean autoPlay = (boolean) call.argument("autoPlay");
        final Intent intent;
        if (YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(applicationContext).equals(YouTubeInitializationResult.SUCCESS)) {
            intent = new Intent(applicationContext, YtPlayerViewActivity.class);
            intent.putExtra("apiKey", apiKey);
            intent.putExtra("videoId", videoId);
            intent.putExtra("autoPlay", autoPlay);
        } else if (YouTubeIntents.canResolvePlayVideoIntent(applicationContext)) {
            intent = YouTubeIntents.createPlayVideoIntentWithOptions(applicationContext, videoId, true, true);
        } else {
            intent = new Intent(applicationContext, YtPlayerWebViewActivity.class);
            intent.putExtra("apiKey", apiKey);
            intent.putExtra("videoId", videoId);
            intent.putExtra("autoPlay", autoPlay);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        applicationContext.startActivity(intent);
        return true;
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        methodChannel.setMethodCallHandler(null);
        methodChannel = null;
    }
}
