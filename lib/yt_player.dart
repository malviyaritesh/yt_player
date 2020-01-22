import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class YtPlayer {
  static const MethodChannel _channel =
      const MethodChannel('com.owliverse/yt_player');

  static Future<bool> createIntentAndStartYouTube({
    @required String apiKey,
    @required String videoId,
    bool autoPlay = false,
  }) {
    return _channel.invokeMethod('createIntentAndStartYouTube', {
      'videoId': videoId,
      'apiKey': apiKey,
      'autoPlay': autoPlay,
    });
  }
}
