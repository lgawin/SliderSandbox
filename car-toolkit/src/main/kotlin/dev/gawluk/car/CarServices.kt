package dev.gawluk.car

import android.car.Car
import android.car.Car.CAR_OCCUPANT_ZONE_SERVICE
import android.car.Car.CAR_UX_RESTRICTION_SERVICE
import android.car.Car.CAR_WATCHDOG_SERVICE
import android.car.Car.INFO_SERVICE
import android.car.Car.PACKAGE_SERVICE
import android.car.Car.POWER_SERVICE
import android.car.Car.PROPERTY_SERVICE
import android.car.Car.SENSOR_SERVICE
import android.car.CarAppFocusManager
import android.car.CarInfoManager
import android.car.CarOccupantZoneManager
import android.car.content.pm.CarPackageManager
import android.car.drivingstate.CarUxRestrictionsManager
import android.car.hardware.CarSensorManager
import android.car.hardware.power.CarPowerManager
import android.car.hardware.property.CarPropertyManager
import android.car.media.CarAudioManager
import android.car.watchdog.CarWatchdogManager

val Car.carAudioManager get() = getCarManager(Car.AUDIO_SERVICE) as CarAudioManager
val Car.carAppFocusManager get() = getCarManager(Car.APP_FOCUS_SERVICE) as CarAppFocusManager
//@SystemApi // see https://android.googlesource.com/platform/packages/services/Car/+/master/car-lib/src/android/car/navigation/CarNavigationStatusManager.java#32
//val Car.navigationStatusManager get() = getCarManager(Car.CAR_NAVIGATION_SERVICE) as CarNavigationStatusManager
val Car.carOccupantZoneManager get() = getCarManager(CAR_OCCUPANT_ZONE_SERVICE) as CarOccupantZoneManager
val Car.carUxRestrictionsManager get() = getCarManager(CAR_UX_RESTRICTION_SERVICE) as CarUxRestrictionsManager
val Car.carWatchdogManager get() = getCarManager(CAR_WATCHDOG_SERVICE) as CarWatchdogManager
@Deprecated("Deprecated in Car")
val Car.carInfoManager get() = getCarManager(INFO_SERVICE) as CarInfoManager
val Car.carPackageManager get() = getCarManager(PACKAGE_SERVICE) as CarPackageManager
val Car.carPowerManager get() = getCarManager(POWER_SERVICE) as CarPowerManager
val Car.carPropertyManager get() = getCarManager(PROPERTY_SERVICE) as CarPropertyManager
@Deprecated("Deprecated in Car")
val Car.carSensorManager get() = getCarManager(SENSOR_SERVICE) as CarSensorManager
