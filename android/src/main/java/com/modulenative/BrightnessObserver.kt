package com.modulenative

import android.database.ContentObserver
import android.os.Handler
import android.provider.Settings
import com.facebook.react.bridge.ReactApplicationContext

class BrightnessObserver(
  private val context: ReactApplicationContext,
  private val onBrightnessChanged: (Double) -> Unit
) : ContentObserver(Handler(context.mainLooper)) {

  fun register() {
    context.contentResolver.registerContentObserver(
      Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS),
      false,
      this
    )
  }

  fun unregister() {
    context.contentResolver.unregisterContentObserver(this)
  }

  override fun onChange(selfChange: Boolean) {
    super.onChange(selfChange)
    val brightness = Settings.System.getInt(
      context.contentResolver,
      Settings.System.SCREEN_BRIGHTNESS,
      0
    )
    onBrightnessChanged(brightness / 255.0)
  }
}
