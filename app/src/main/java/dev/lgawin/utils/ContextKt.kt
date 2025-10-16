package dev.lgawin.utils

import android.content.Context
import android.content.pm.PackageManager

val Context.isAutomotive: Boolean
    get() = packageManager.hasSystemFeature(PackageManager.FEATURE_AUTOMOTIVE)
