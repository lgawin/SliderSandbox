plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "dev.gawluk.car.toolkit"

    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 30
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    useLibrary("android.car")
}

dependencies {
    implementation(libs.androidx.core.ktx)
}
