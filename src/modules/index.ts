import { NativeEventEmitter, NativeModules } from 'react-native';
import ModuleNative from '../NativeModuleNative';
import { useBrightnessChange } from './useBrightnessChange';
import { useModeChange } from './useModeChange';
import { useVolumeChange } from './useVolumeChange';

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

export const Emitter = new NativeEventEmitter(NativeModules.MyNewArchitect);

export const ModeTheme = {
  getModeApp,
  setModeApp,
};

export function useGetValueChange() {
  const volumeChange = useVolumeChange();
  const brightnessChange = useBrightnessChange();
  const modeChange = useModeChange();

  return { volumeChange, brightnessChange, modeChange };
}
