import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';
export interface Spec extends TurboModule {
  setBrightnessDevice(value: number): void;
  setBrightness(value: number): void;
  getBrightness(): number;
  setVolume(value: number): void;
  getVolume(): number;
  setVolumeNotify(value: number): void;
  getVolumeNotify(): number;
  setVolumeSystem(value: number): void;
  getVolumeSystem(): number;
  setVolumeMedia(value: number): void;
  getVolumeMedia(): number;
  addListener(eventName: string): void;
  removeListeners(count: number): void;
  getModeApp(): 'dark' | 'light';
  setModeApp(mode: 'dark' | 'light'): void;
  onChangeMode(): void;
}

export default TurboModuleRegistry.getEnforcing<Spec>('ModuleNative');
