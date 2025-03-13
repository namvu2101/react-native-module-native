#import "ModuleNative.h"

@implementation ModuleNative {
  float _lastVolume;
}
RCT_EXPORT_MODULE()

- (void)setBrightnessDevice:(double)value {
  dispatch_async(dispatch_get_main_queue(), ^{
    [UIScreen mainScreen].brightness = (CGFloat)value;
  });
}

- (NSNumber *)getBrightnessDevice {
  return @([UIScreen mainScreen].brightness);
}

- (void)setBrightness:(double)value {
  dispatch_async(dispatch_get_main_queue(), ^{
    [UIScreen mainScreen].brightness = (CGFloat)value;
  });
}

- (NSNumber *)getBrightness {
  return @([UIScreen mainScreen].brightness);
}

- (void)setVolume:(double)value {
  dispatch_async(dispatch_get_main_queue(), ^{
    [UIScreen mainScreen].brightness = (CGFloat)value;
  });
}

- (NSNumber *)getVolume {
  return @([AVAudioSession sharedInstance].outputVolume);
}
- (NSArray<NSString *> *)supportedEvents {
  return @[@"onVolumeChanged"];
}

- (instancetype)init {
  if (self = [super init]) {
    [self observeVolumeChanges];
  }
  return self;
}

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

- (void)dealloc {
  [[NSNotificationCenter defaultCenter] removeObserver:self];
}
RCT_EXPORT_METHOD(addListener:(NSString *)eventName)
{
  // No-op
}

RCT_EXPORT_METHOD(removeListeners:(double)count)
{
  // No-op
}


- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params
{
    return std::make_shared<facebook::react::NativeModuleNativeSpecJSI>(params);
}

@end
