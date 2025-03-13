import ModuleNative from './NativeModuleNative';

export function multiply(a: number, b: number): number {
  return ModuleNative.multiply(a, b);
}
