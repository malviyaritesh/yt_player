import 'package:flutter/material.dart';
import 'package:yt_player/yt_player.dart';

const String YOUTUBE_API_KEY = 'YOUR_API_KEY';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: RaisedButton(
            onPressed: () => YtPlayer.createIntentAndStartYouTube(
              apiKey: YOUTUBE_API_KEY,
              videoId: 'S0Q4gqBUs7c',
              autoPlay: true,
            ),
            child: Text('Start YouTube Video'),
          ),
        ),
      ),
    );
  }
}
