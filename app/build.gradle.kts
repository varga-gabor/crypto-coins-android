plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.com.google.devtools.ksp)
    alias(libs.plugins.de.mannodermaus.android.junit5)
}

android {
    namespace = "com.aldi.cryptocoins"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.aldi.cryptocoins"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["runnerBuilder"] = "de.mannodermaus.junit5.AndroidJUnit5Builder"
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            isDebuggable = true
        }
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

	// Core Android
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.recyclerview)
    implementation(libs.material)

	// Lifecycle
    implementation(libs.androidx.lifecycle.common.java8)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // DI
    ksp(libs.io.insert.koin.ksp.compiler)
    implementation(libs.io.insert.koin.core)
    implementation(libs.io.insert.koin.android)
    implementation(libs.io.insert.koin.annotations)

    // Logging
    implementation(libs.com.jakewharton.timber)

    // Network
    ksp(libs.com.squareup.moshi.kotlin.codegen)
    implementation(libs.com.squareup.moshi)
    implementation(libs.com.squareup.retrofit)
    implementation(libs.com.squareup.retrofit.converter.moshi)
    implementation(libs.com.squareup.okhttp3.okhttp)
    implementation(libs.com.squareup.okhttp3.logging.interceptor)

    // Unit testing
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.params)
    testImplementation(libs.org.jetbrains.kotlinx.coroutines.android)
    testImplementation(libs.org.jetbrains.kotlinx.coroutines.test)
    testImplementation(libs.io.mockk)
    testImplementation(libs.app.cash.turbine)
    testRuntimeOnly(libs.junit.jupiter.engine)
}
