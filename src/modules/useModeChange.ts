import { useEffect, useState } from 'react';
import { NativeEventEmitter, NativeModules } from 'react-native';
import { ModeTheme } from '.';

export function useModeChange() {
  const [state, setState] = useState<string>(ModeTheme.getModeApp() || 'light');

  useEffect(() => {
    const ReactNativeEmitter = new NativeEventEmitter(
      NativeModules.ModuleNative
    );
    const sub = ReactNativeEmitter.addListener('onModeChanged', setState);

    return () => {
      sub.remove();
    };
  }, []);

  return state;
}
