plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "be.kdg.filmapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "be.kdg.filmapp"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.material.icons)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.androidx.navigation.compose)
    debugImplementation(libs.androidx.ui.tooling)

    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.annotations)
    ksp(libs.koin.ksp.compiler)

    // Retrofit + serialization
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.okhttp)

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    // AppCompat (language switching)
    implementation(libs.androidx.appcompat)
}

ksp {
    arg("KOIN_CONFIG_CHECK", "true")
}
