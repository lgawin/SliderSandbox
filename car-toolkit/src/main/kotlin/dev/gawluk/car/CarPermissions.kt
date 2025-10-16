package dev.gawluk.car

import android.car.Car
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

enum class CarPermission(val permission: String) {
    CONTROL_AUDIO_VOLUME(Car.PERMISSION_CAR_CONTROL_AUDIO_VOLUME),
    // TODO add more
}

fun CarPermission.isGranted(context: Context): Boolean = ContextCompat.checkSelfPermission(
    context,
    permission,
) == PackageManager.PERMISSION_GRANTED

fun Context.isPermissionGranted(permission: CarPermission) = permission.isGranted(this)

fun Context.withPermissionGranted(
    permission: CarPermission,
    onGranted: () -> Unit,
    onDenied: (CarPermission) -> Unit,
) = if (isPermissionGranted(permission)) onGranted() else onDenied(permission)

fun Context.withPermissionGranted(
    permission: CarPermission,
    onGranted: () -> Unit,
) = withPermissionGranted(permission, onGranted, onDenied = {})

class MissingPermissionException(permission: CarPermission) :
    SecurityException("Missing permission ${permission.permission}")
