plugins {
    id("com.android.application") // For Android
    id("org.jetbrains.kotlin.android") // For Kotlin Android
    id("com.google.gms.google-services") // For Firebase Google Servicestlin for Android
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 24
        targetSdk = 34
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("com.google.zxing:core:3.3.3")
    implementation("com.journeyapps:zxing-android-embedded:4.1.0")

    implementation("com.google.mlkit:barcode-scanning:17.0.3")

    // Material Design Components for gradient and UI enhancements
    implementation("com.google.android.material:material:1.11.0")

    // For AppCompat compatibility
    implementation("androidx.appcompat:appcompat:1.7.0")

    // Corrected Firebase Analytics dependency
    implementation("com.google.firebase:firebase-analytics:21.0.0")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.firebase:firebase-auth:22.1.1")
    implementation("com.google.firebase:firebase-database:20.3.3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // AndroidX dependencies
    implementation("androidx.appcompat:appcompat:1.6.0")
    implementation("androidx.media:media:1.2.0") // or the latest version
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Kotlin dependencies
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.7.20")

}
