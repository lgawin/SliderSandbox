package dev.gawluk.car.sandbox

import android.car.Car
import android.content.Context
import android.util.Log
import dev.gawluk.car.CarPermission
import dev.gawluk.car.MissingPermissionException
import dev.gawluk.car.carAudioManager
import dev.gawluk.car.debug.logVolumeChanges
import dev.gawluk.car.withPermissionGranted

fun Context.installCarFeature() {
    val car = Car.createCar(this)
    val audioManager = car.carAudioManager

//        if (ContextCompat.checkSelfPermission(
//                this,
//                Car.PERMISSION_CAR_CONTROL_AUDIO_VOLUME,
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//
//        }
//
//        if (CarPermission.CONTROL_AUDIO_VOLUME.isGranted(this)) {
//
//        }

    withPermissionGranted(
        CarPermission.CONTROL_AUDIO_VOLUME,
        onGranted = audioManager::logVolumeChanges,
        onDenied = { logw(MissingPermissionException(it)) },
    )

//        withPermissionGranted(CarPermission.CONTROL_AUDIO_VOLUME) {
//            // some logic if permission is available
//            audioManager.logVolumeChanges()
//        }
}

// TODO create some shared logging (for this and app)
private fun logw(t: Throwable) {
    Log.w("gawluk", t)
}
