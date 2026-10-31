import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.dropshots)
}

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) file.inputStream().use { load(it) }
}

android {
    namespace = "com.decoutkhanqindev.aodamn"
    compileSdk = 37
    defaultConfig {
        applicationId = "com.decoutkhanqindev.aodamn"
        minSdk = 30
        targetSdk = 37
        versionCode = 1
        versionName = "v1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "ADMOB_TEST_DEVICE_IDS",
            "\"${localProperties.getProperty("admob.test.device.ids", "")}\""
        )
        buildConfigField(
            "String",
            "ADMOB_BANNER_TEST_ID",
            "\"ca-app-pub-3940256099942544/9214589741\""
        )
        buildConfigField(
            "String",
            "ADMOB_NATIVE_TEST_ID",
            "\"ca-app-pub-3940256099942544/2247696110\""
        )
        buildConfigField(
            "String",
            "ADMOB_INTERSTITIAL_TEST_ID",
            "\"ca-app-pub-3940256099942544/1033173712\""
        )
        buildConfigField(
            "String",
            "ADMOB_REWARDED_TEST_ID",
            "\"ca-app-pub-3940256099942544/5224354917\""
        )
        buildConfigField(
            "String",
            "ADMOB_APP_OPEN_TEST_ID",
            "\"ca-app-pub-3940256099942544/9257395921\""
        )
    }

    signingConfigs {
        create("release") {
            localProperties.getProperty("signing.store.file")?.let { storeFile = file(it) }
            storePassword = localProperties.getProperty("signing.store.password")
            keyAlias = localProperties.getProperty("signing.key.alias")
            keyPassword = localProperties.getProperty("signing.key.password")
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // TODO: Thay bằng ad unit id thật của từng placement (debug giữ test id của Google)
            buildConfigField(
                "String",
                "INTER_SPLASH_ALL_ID",
                "\"ca-app-pub-3940256099942544/1033173712\""
            )
            buildConfigField(
                "String",
                "BANNER_SPLASH_ALL_ID",
                "\"ca-app-pub-3940256099942544/9214589741\""
            )
            buildConfigField(
                "String",
                "BANNER_ONBOARDING_ALL_ID",
                "\"ca-app-pub-3940256099942544/9214589741\""
            )
            buildConfigField(
                "String",
                "BANNER_MAIN_ALL_ID",
                "\"ca-app-pub-3940256099942544/9214589741\""
            )
            buildConfigField(
                "String",
                "BANNER_THEME_EDITOR_ALL_ID",
                "\"ca-app-pub-3940256099942544/9214589741\""
            )
            buildConfigField(
                "String",
                "BANNER_CUSTOMIZE_ALL_ID",
                "\"ca-app-pub-3940256099942544/9214589741\""
            )
        }

        debug {
            isDebuggable = true
            buildConfigField(
                "String",
                "INTER_SPLASH_ALL_ID",
                "\"ca-app-pub-3940256099942544/1033173712\""
            )
            buildConfigField(
                "String",
                "BANNER_SPLASH_ALL_ID",
                "\"ca-app-pub-3940256099942544/9214589741\""
            )
            buildConfigField(
                "String",
                "BANNER_ONBOARDING_ALL_ID",
                "\"ca-app-pub-3940256099942544/9214589741\""
            )
            buildConfigField(
                "String",
                "BANNER_MAIN_ALL_ID",
                "\"ca-app-pub-3940256099942544/9214589741\""
            )
            buildConfigField(
                "String",
                "BANNER_THEME_EDITOR_ALL_ID",
                "\"ca-app-pub-3940256099942544/9214589741\""
            )
            buildConfigField(
                "String",
                "BANNER_CUSTOMIZE_ALL_ID",
                "\"ca-app-pub-3940256099942544/9214589741\""
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        aidl = false
        buildConfig = true
        shaders = false
    }

    androidResources {
        generateLocaleConfig = true
    }

    bundle {
        language {
            enableSplit = false
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.profileinstaller)
    "baselineProfile"(project(":baselineprofile"))
    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    debugImplementation(libs.androidx.compose.ui.tooling)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)

    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.espresso.core)

    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)

    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    implementation(libs.timber)

    implementation(libs.kotlinx.collections.immutable)

    implementation(libs.lottie.compose)

    implementation(libs.colorpicker.compose)

    implementation(libs.androidx.datastore.preferences)

    implementation(libs.play.services.ads)
    implementation(libs.user.messaging.platform)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.kotlinx.serialization)
    implementation(libs.kotlinx.serialization.json)
}
