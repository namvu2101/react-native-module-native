import { useEffect, useState } from 'react';
import { Emitter, ModeTheme } from '.';

export function useModeChange() {
  const [state, setState] = useState<string>(ModeTheme.getModeApp());

  useEffect(() => {
    const sub = Emitter.addListener('onModeChanged', setState);

    return () => {
      sub.remove();
    };
  }, []);

  return state;
}
