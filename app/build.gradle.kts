plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
}

android {
    namespace = "com.overdevx.mystoryapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.overdevx.mystoryapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String","BASE_URL","\"https://story-api.dicoding.dev/v1/\"")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"

    }
    testOptions {
        animationsDisabled = true
    }
    buildFeatures{
        viewBinding = true
        buildConfig = true
        dataBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //circleimageview
    implementation (libs.circleimageview)

    //retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit2.converter.gson)
    implementation(libs.logging.interceptor)

    //viewmodel
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    //datastore
    implementation (libs.androidx.datastore.preferences)
    implementation (libs.androidx.datastore)

    //glide
    implementation(libs.glide)

    //camera
    implementation (libs.androidx.camera.core)
    implementation (libs.androidx.camera.camera2)
    implementation (libs.androidx.camera.lifecycle)
    implementation (libs.androidx.camera.video)
    implementation (libs.androidx.camera.view)
    implementation (libs.androidx.camera.extensions)
    implementation(libs.androidx.exifinterface)

    //maps
    ksp (libs.ksp)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)

    //paging
    implementation(libs.androidx.paging.runtime.ktx)

    //room
    implementation(libs.androidx.room.paging)
    ksp(libs.room.compiler)
    implementation(libs.androidx.room.ktx)

    //mockito
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)
    testImplementation (libs.androidx.core)
    implementation (libs.guava)

    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.kotlinx.coroutines.test)

    androidTestImplementation(libs.androidx.core.testing) // InstantTaskExecutorRule
    androidTestImplementation(libs.kotlinx.coroutines.test)

    //TestCoroutineDispatcher
    debugImplementation(libs.androidx.fragment.testing) //launchFragmentInContainer
    androidTestImplementation(libs.androidx.fragment.testing)
    androidTestImplementation(libs.mockwebserver)
    androidTestImplementation(libs.okhttp3.okhttp.tls)
    androidTestImplementation(libs.espresso.contrib) //RecyclerViewActions
    implementation(libs.androidx.espresso.idling.resource)
    androidTestImplementation(libs.espresso.intents) //IntentsTestRule

    androidTestImplementation (libs.androidx.uiautomator)


}