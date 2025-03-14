import { useEffect, useState } from 'react';
import { Brightness } from '.';
import { NativeEventEmitter, NativeModules } from 'react-native';

export function useBrightnessChange() {
  const [state, setState] = useState<number>(Brightness.getBrightness() || 0);

  useEffect(() => {
    const Emitter = new NativeEventEmitter(NativeModules.ModuleNative);
    const bright = Emitter.addListener('onBrightnessChanged', setState);

    return () => {
      bright.remove();
    };
  }, []);

  return state;
}
