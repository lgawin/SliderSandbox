package dev.gawluk.car.debug

import android.car.Car
import android.car.media.CarAudioManager
import android.util.Log
import androidx.annotation.RequiresPermission

@RequiresPermission(Car.PERMISSION_CAR_CONTROL_AUDIO_VOLUME)
fun CarAudioManager.logVolumeChanges(logTag: String = "VolumeCallback") {
    registerCarVolumeCallback(LogCatLoggingVolumeCallback(logTag))
}

private class LogCatLoggingVolumeCallback(
    private val logTag: String,
) : CarAudioManager.CarVolumeCallback() {

    override fun onMasterMuteChanged(zoneId: Int, flags: Int) {
        Log.v(logTag, "onMasterMuteChanged($zoneId, $flags)")
        super.onMasterMuteChanged(zoneId, flags)
    }

    override fun onGroupMuteChanged(zoneId: Int, groupId: Int, flags: Int) {
        Log.v(logTag, "onGroupMuteChanged($zoneId, $groupId, $flags)")
        super.onGroupMuteChanged(zoneId, groupId, flags)
    }

    override fun onGroupVolumeChanged(zoneId: Int, groupId: Int, flags: Int) {
        Log.v(logTag, "onGroupVolumeChanged($zoneId, $groupId, $flags)")
        super.onGroupVolumeChanged(zoneId, groupId, flags)
    }
}
