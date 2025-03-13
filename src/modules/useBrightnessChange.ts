import { useEffect, useState } from 'react';
import { Brightness, Emitter } from '.';

export function useBrightnessChange() {
  const [state, setState] = useState<number>(Brightness.getBrightness());

  useEffect(() => {
    const bright = Emitter.addListener('onBrightnessChanged', setState);

    return () => {
      bright.remove();
    };
  }, []);

  return state;
}
