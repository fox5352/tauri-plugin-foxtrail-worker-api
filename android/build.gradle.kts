plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("plugin.serialization") version "1.9.23"
}

android {
    namespace = "com.plugin.foxtrailworker"
    compileSdk = 36

    defaultConfig {
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // Android Core
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Project dependencies
    implementation(project(":tauri-android"))

    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.10.4")

    // Optional - only if you need testing
    // androidTestImplementation("androidx.work:work-testing:2.10.4")

    // Networking
    // implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Supabase - BOM manages versions automatically
    implementation(platform("io.github.jan-tennert.supabase:bom:2.2.1"))
    implementation("io.github.jan-tennert.supabase:postgrest-kt")
    implementation("io.github.jan-tennert.supabase:functions-kt")
    // implementation("io.github.jan-tennert.supabase:realtime-kt") // uncomment if needed

    // Ktor
    implementation("io.ktor:ktor-client-android:2.3.6")

    
    // Kotlin Serialization
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.4")

    implementation("com.squareup.okhttp3:okhttp:4.12.0")
}
