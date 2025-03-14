import { useEffect, useState } from 'react';
import { NativeEventEmitter, NativeModules } from 'react-native';

export function useVolumeChange() {
  const [volumeChange, setVolumeChange] = useState<number>(0);

  useEffect(() => {
    const Emitter = new NativeEventEmitter(NativeModules.ModuleNative);
    const sub = Emitter.addListener('onVolumeChanged', setVolumeChange);

    return () => {
      sub.remove();
    };
  }, []);

  return volumeChange;
}
