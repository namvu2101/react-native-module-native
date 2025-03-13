import { useEffect, useState } from 'react';
import { Emitter, Volume } from '.';

export function useVolumeChange() {
  const [volumeChange, setVolumeChange] = useState<number>(
    Volume.getVolumeMedia()
  );

  useEffect(() => {
    const sub = Emitter.addListener('onVolumeChanged', setVolumeChange);

    return () => {
      sub.remove();
    };
  }, []);

  return volumeChange;
}
