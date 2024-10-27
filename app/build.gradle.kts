plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.googleGmsGoogleServices)
}

android {

    viewBinding {
        enable = true
    }
    namespace = "com.example.balancebite"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.balancebite"
        minSdk = 28
        //noinspection OldTargetApi
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildToolsVersion = "34.0.0"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.runtime.android)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.firebase.analytics)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.database)
    implementation(libs.androidx.preference)
    implementation(libs.androidx.ui.graphics.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    implementation (libs.androidx.recyclerview)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation (libs.androidx.appcompat.v170)
    implementation (libs.androidx.drawerlayout)
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.firebase.storage)
    implementation (libs.glide)
    implementation (libs.picasso)
    implementation (libs.circleimageview)
    implementation (libs.picasso.v28)
    implementation (libs.androidx.work.runtime.ktx)

}