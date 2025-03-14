package com.modulenative

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import com.facebook.react.bridge.ReactApplicationContext

class VolumeChangeReceiver(
  private val context: ReactApplicationContext,
  private val onVolumeChanged: (Double) -> Unit
) : BroadcastReceiver() {

  override fun onReceive(context: Context?, intent: Intent?) {
    if (intent?.action == "android.media.VOLUME_CHANGED_ACTION") {
      val audioManager = context?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
      val current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
      val max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
      val volume = current.toDouble() / max
      onVolumeChanged(volume)
    }
  }

  fun register() {
    val filter = IntentFilter("android.media.VOLUME_CHANGED_ACTION")
    context.registerReceiver(this, filter)
  }

  fun unregister() {
    try {
      context.unregisterReceiver(this)
    } catch (_: Exception) { }
  }
}
