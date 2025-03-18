#import "ModuleNative.h"
#import <AVFoundation/AVFoundation.h>
#import <React/RCTBridge.h>
#import <React/RCTUtils.h>
#import "generated/RNModuleNativeSpec/RNModuleNativeSpec.h"
#import <React/RCTEventEmitter.h>

@implementation ModuleNative {
  float _lastVolume;
}

RCT_EXPORT_MODULE()

// MARK: - Lifecycle

- (instancetype)init {
  if (self = [super init]) {
    [self observeVolumeChanges];
  }
  return self;
}

- (void)dealloc {
  [[NSNotificationCenter defaultCenter] removeObserver:self];
}

// MARK: - Brightness

- (void)setBrightnessDevice:(double)value {
  dispatch_async(dispatch_get_main_queue(), ^{
    [UIScreen mainScreen].brightness = (CGFloat)value;
  });
}

- (NSNumber *)getBrightnessDevice {
  return @([UIScreen mainScreen].brightness);
}

- (NSNumber *)getBrightness {
  return @([UIScreen mainScreen].brightness);
}

- (void)setBrightness:(double)value {
  dispatch_async(dispatch_get_main_queue(), ^{
    [UIScreen mainScreen].brightness = (CGFloat)value;
  });
}

// MARK: - Volume

- (void)setVolume:(double)value {
  // iOS không cho phép set volume trực tiếp bằng API công khai
  // Giải pháp thường là dùng MPVolumeView ẩn hoặc AVPlayer workaround
  // Tạm thời placeholder để tránh nhầm lẫn
  NSLog(@"setVolume is not directly supported on iOS.");
}

- (NSNumber *)getVolume {
  return @([AVAudioSession sharedInstance].outputVolume);
}

// MARK: - Mode

- (void)setModeApp:(NSString *)mode {
    dispatch_async(dispatch_get_main_queue(), ^{
        UIUserInterfaceStyle style = UIUserInterfaceStyleUnspecified;

        if ([mode isEqualToString:@"dark"]) {
            style = UIUserInterfaceStyleDark;
        } else if ([mode isEqualToString:@"light"]) {
            style = UIUserInterfaceStyleLight;
        }

        if (@available(iOS 13.0, *)) {
            UIWindow *keyWindow = nil;
            for (UIWindowScene* windowScene in [UIApplication sharedApplication].connectedScenes) {
                if (windowScene.activationState == UISceneActivationStateForegroundActive) {
                    keyWindow = windowScene.windows.firstObject;
                    break;
                }
            }
            if (keyWindow != nil) {
                keyWindow.overrideUserInterfaceStyle = style;
            }
        }
    });
}



- (NSString *)getModeApp {
    UIUserInterfaceStyle style = UIUserInterfaceStyleUnspecified;

    if (@available(iOS 13.0, *)) {
        UIWindow *keyWindow = nil;
        for (UIWindowScene* windowScene in [UIApplication sharedApplication].connectedScenes) {
            if (windowScene.activationState == UISceneActivationStateForegroundActive) {
                keyWindow = windowScene.windows.firstObject;
                break;
            }
        }
        if (keyWindow != nil) {
            style = keyWindow.traitCollection.userInterfaceStyle;
        }
    }

    return (style == UIUserInterfaceStyleDark) ? @"dark" : @"light";
}


- (void)onChangeMode {
    NSString *currentMode = [self getModeApp];
    if ([currentMode isEqualToString:@"dark"]) {
        [self setModeApp:@"light"];
    } else {
        [self setModeApp:@"dark"];
    }
}


// MARK: - Observe Volume Changes

- (void)observeVolumeChanges {
  AVAudioSession *session = [AVAudioSession sharedInstance];
  NSError *error = nil;
  [session setActive:YES error:&error];
  _lastVolume = session.outputVolume;

  [[NSNotificationCenter defaultCenter] addObserver:self
                                           selector:@selector(handleVolumeChange:)
                                               name:@"AVSystemController_SystemVolumeDidChangeNotification"
                                             object:nil];
}

- (void)handleVolumeChange:(NSNotification *)notification {
  float newVolume = [AVAudioSession sharedInstance].outputVolume;
  if (newVolume != _lastVolume) {
    _lastVolume = newVolume;
    [self sendEventWithName:@"onVolumeChanged" body:@(newVolume)];
  }
}

// MARK: - NativeEventEmitter Support

- (NSArray<NSString *> *)supportedEvents {
  return @[@"onVolumeChanged"];
}

- (void)addListener:(NSString *)eventName {
  // Bắt buộc implement để tránh warning
}

- (void)removeListeners:(double)count {
  // Bắt buộc implement để tránh warning
}

- (nonnull NSNumber *)getVolumeMedia {
  return @([AVAudioSession sharedInstance].outputVolume);
}


- (nonnull NSNumber *)getVolumeNotify {
  return @([AVAudioSession sharedInstance].outputVolume);

}


- (nonnull NSNumber *)getVolumeSystem {
  return @([AVAudioSession sharedInstance].outputVolume);
}


- (void)setVolumeMedia:(double)value { 
}


- (void)setVolumeNotify:(double)value { 
}


- (void)setVolumeSystem:(double)value { 
}


// MARK: - TurboModule (New Architecture)

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
  (const facebook::react::ObjCTurboModule::InitParams &)params {
  return std::make_shared<facebook::react::NativeModuleNativeSpecJSI>(params);
}

@end
