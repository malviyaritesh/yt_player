#import "YtPlayerPlugin.h"
#if __has_include(<yt_player/yt_player-Swift.h>)
#import <yt_player/yt_player-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "yt_player-Swift.h"
#endif

@implementation YtPlayerPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftYtPlayerPlugin registerWithRegistrar:registrar];
}
@end
