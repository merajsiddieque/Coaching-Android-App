plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.app.checking"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.app.checking"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        viewBinding = true
    }

    packagingOptions {
        exclude("META-INF/DEPENDENCIES") // Exclude the duplicate dependencies file
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Firebase dependencies
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)

    // Lifecycle dependencies
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)

    // Navigation dependencies
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // Google services and YouTube API dependencies
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.google.apis:google-api-services-youtube:v3-rev222-1.25.0")
    implementation("com.google.api-client:google-api-client-android:1.31.5")
    implementation("com.google.api-client:google-api-client-gson:1.31.5")

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
