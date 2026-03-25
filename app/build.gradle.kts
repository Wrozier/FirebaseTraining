import com.datadog.gradle.plugin.SdkCheckLevel
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.datadoghq.dd-sdk-android-gradle-plugin")
}

android {
    namespace = "com.tc.firebasetraining"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.tc.firebasetraining"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Datadog
        buildConfigField("String", "DATADOG_CLIENT_TOKEN", "\"pub7b6a64d0bb229c7a195eff2f1a2d3005\"")
        buildConfigField("String", "DATADOG_APPLICATION_ID", "\"afa6f274-8602-4a16-9c3c-56f030a64182\"")

        // Splunk
        buildConfigField("String", "SPLUNK_RUM_ACCESS_TOKEN", "\"lvJe7lWColdLwBpjjBhYaA\"")
        buildConfigField("String", "SPLUNK_REALM", "\"us1\"")
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
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
        isCoreLibraryDesugaringEnabled = true
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }


}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    implementation(platform("com.google.firebase:firebase-bom:34.10.0"))
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-analytics")

    implementation("com.datadoghq:dd-sdk-android-logs:3.7.1")
    implementation("com.datadoghq:dd-sdk-android-okhttp:3.7.1")
    implementation("com.datadoghq:dd-sdk-android-compose:3.7.1")
    implementation("com.datadoghq:dd-sdk-android-rum:3.7.1")

    implementation("com.splunk:splunk-otel-android:2.1.8")

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.4")

}

datadog {
    checkProjectDependencies = SdkCheckLevel.NONE
    site = "US1"
    variants {
        register("debug") {

        }
    }
}