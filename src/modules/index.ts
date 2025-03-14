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
};

export * from './useBrightnessChange';
export * from './useVolumeChange';
export * from './useModeChange';
