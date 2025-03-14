package com.modulenative

import android.app.Activity
import android.app.UiModeManager
import android.provider.Settings
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.annotations.ReactModule
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.facebook.react.modules.core.DeviceEventManagerModule

@ReactModule(name = ModuleNativeModule.NAME)
class ModuleNativeModule(reactContext: ReactApplicationContext) :
  NativeModuleNativeSpec(reactContext) {
    private val audioManager = reactContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val brightness = Settings.System.getInt(
    reactContext.contentResolver,
      Settings.System.SCREEN_BRIGHTNESS
  )

  private val uiModeManager = reactContext.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager

  private var volumeChange: VolumeChangeReceiver? = null
  private var brightNessChange: BrightnessObserver? = null

  override fun getName(): String {
    return NAME
  }

  override fun addListener(eventName: String?) {
    // Required for RN New Architecture, even if you don't use it internally
  }

  override fun removeListeners(count: Double) {
    // Required for RN New Architecture, even if you don't use it internally
  }

  override fun getModeApp(): String {
    val context = reactApplicationContext
    val currentMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return when (currentMode) {
      Configuration.UI_MODE_NIGHT_YES -> "dark"
      Configuration.UI_MODE_NIGHT_NO -> "light"
      else -> "unknown"
    }
  }

  @RequiresApi(Build.VERSION_CODES.S)
  override fun setModeApp(mode: String) {
    if(mode == "dark"){
      uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_YES)
    }
    else{
      uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_NO)
    }
      sendModeChanged(mode)
  }
  private fun sendModeChanged(value:String){
    reactApplicationContext
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
      .emit("onModeChanged",value)
  }

  private fun sendBrightnessApp(value:Double){
    reactApplicationContext
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
      .emit("onBrightnessApp",value)
  }
  override fun setBrightnessDevice(value: Double) {
    val context = reactApplicationContext
    if (Settings.System.canWrite(context)) {
      val brightnessValue = (value * 255).toInt().coerceIn(0, 255)
      val success = Settings.System.putInt(
        context.contentResolver,
        Settings.System.SCREEN_BRIGHTNESS,
        brightnessValue
      )
      if (!success) {
        Log.w("Brightness", "Failed to write system brightness")
      }
    } else {
      val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
      intent.data = Uri.parse("package:" + context.packageName)
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      context.startActivity(intent)
    }
  }

  override fun setBrightness(value: Double) {
    val activity: Activity = currentActivity ?: return

    activity.runOnUiThread {
      activity.theme
      val window = activity.window ?: return@runOnUiThread
      val layoutParams = window.attributes
      layoutParams.screenBrightness = value.toFloat()
      window.attributes = layoutParams
      sendBrightnessApp(value)
    }
  }

  override fun getBrightness(): Double {
    val normalized = brightness / 255.0
    return normalized
  }

  override fun setVolume(value: Double) {
    val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING)
    val targetVolume = (value * maxVolume).toInt().coerceIn(0, maxVolume)
    audioManager.setStreamVolume(AudioManager.STREAM_RING, targetVolume, 0)
  }

  override fun getVolume(): Double {
    val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING)
    val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING)
    return currentVolume.toDouble() / maxVolume
  }

  override fun setVolumeNotify(value: Double) {
    val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION)
    val targetVolume = (value * maxVolume).toInt().coerceIn(0, maxVolume)
    audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, targetVolume, 0)
  }

  override fun getVolumeNotify(): Double {
    val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION)
    val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION)
    return currentVolume.toDouble() / maxVolume
  }

  override fun setVolumeSystem(value: Double) {
    val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM)
    val targetVolume = (value * maxVolume).toInt().coerceIn(0, maxVolume)
    audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, targetVolume, 0)
  }

  override fun getVolumeSystem(): Double {
    val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM)
    val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM)
    return currentVolume.toDouble() / maxVolume
  }

  override fun setVolumeMedia(value: Double) {
    val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    val targetVolume = (value * maxVolume).toInt().coerceIn(0, maxVolume)
    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, targetVolume, 0)
  }

  override fun getVolumeMedia(): Double {
    val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
    val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    return currentVolume.toDouble() / maxVolume
  }

  override fun initialize() {
    super.initialize()
    registerVolumeChange()
    registerBrightnessChange()
  }

  override fun invalidate() {
    super.invalidate()
    unregister()
  }

  private fun sendVolumeChanged(value:Double){
    reactApplicationContext
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
      .emit("onVolumeChanged",value)
  }

  private fun sendBrightnessChanged(value:Double){
    reactApplicationContext
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
      .emit("onBrightnessChanged",value)
  }

  private fun registerBrightnessChange(){
    if(brightNessChange==null){
      brightNessChange = BrightnessObserver(reactApplicationContext){value->
        sendBrightnessChanged(value)
      }
    }
    brightNessChange?.register()
  }

  private fun registerVolumeChange(){
    if (volumeChange == null) {
      volumeChange = VolumeChangeReceiver(reactApplicationContext) { vol ->
        sendVolumeChanged(vol)
      }
      volumeChange?.register()
    }
  }


  private fun unregister(){
    try {
      volumeChange?.unregister()
      brightNessChange?.unregister()
    } catch (_: Exception) {}
    volumeChange = null
    brightNessChange = null
  }

  companion object {
    const val NAME = "ModuleNative"
  }
}
