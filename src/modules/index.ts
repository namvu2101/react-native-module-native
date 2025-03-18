import ModuleNative from '../NativeModuleNative';

const {
  setBrightnessDevice,
  getBrightness,
  setBrightness,
  setVolume,
  setVolumeMedia,
  setVolumeNotify,
  setVolumeSystem,
  getVolume,
  getVolumeMedia,
  getVolumeNotify,
  getVolumeSystem,
  setModeApp,
  getModeApp,
  onChangeMode,
} = ModuleNative;

export const Brightness = {
  setBrightness,
  getBrightness,
  setBrightnessDevice,
};

export const Volume = {
  setVolume,
  getVolume,
  setVolumeMedia,
  getVolumeMedia,
  setVolumeNotify,
  getVolumeNotify,
  setVolumeSystem,
  getVolumeSystem,
};

export const ModeTheme = {
  getModeApp,
  setModeApp,
  onChangeMode,
};

export * from './useBrightnessChange';
export * from './useVolumeChange';
export * from './useModeChange';
