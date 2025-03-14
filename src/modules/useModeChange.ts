import { useEffect, useState } from 'react';
import { NativeEventEmitter, NativeModules } from 'react-native';
import { ModeTheme } from '.';

export function useModeChange() {
  const [state, setState] = useState<string>(ModeTheme.getModeApp() || 'light');

  useEffect(() => {
    const Emitter = new NativeEventEmitter(NativeModules.ModuleNative);
    const sub = Emitter.addListener('onModeChanged', setState);

    return () => {
      sub.remove();
    };
  }, []);

  return state;
}
