// <project>/app/build.gradle.kts
plugins {
    // Use your version-catalog Android + Kotlin plugins
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    // Compose compiler plugin
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"

    // Apply the Google services Gradle plugin (NO version here)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.civicapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.civicapp"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // ===== Compose =====
    implementation(platform("androidx.compose:compose-bom:2024.02.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.activity:activity-compose:1.8.2")

    // Navigation (keep a single version)
    implementation("androidx.navigation:navigation-compose:2.7.5")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

    // Material3 Window Size
    implementation("androidx.compose.material3:material3-window-size-class")

    // ===== Core libraries (kept as-is) =====
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // ===== Testing =====
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    debugImplementation("androidx.compose.ui:ui-tooling")

    // ===== Firebase BoM (ONE time, before Firebase libs) =====
    implementation(platform("com.google.firebase:firebase-bom:34.6.0"))

    // Firebase libraries (no versions when using BoM)
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-functions")
    implementation("com.google.firebase:firebase-messaging")

    // Kotlin Coroutines for Firebase async operations
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
}
