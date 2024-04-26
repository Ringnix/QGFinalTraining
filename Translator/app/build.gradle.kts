plugins {
    alias(libs.plugins.androidApplication)
}

android {

    namespace = "com.example.translator"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.translator"
        minSdk = 28
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


    implementation(libs.bottomNavigation)
    // 网络访问
    implementation(libs.okhttp)
    // 下拉框
    implementation(libs.niceSpinner)
    // 卡片视图
    implementation(libs.cardview)
    // GSON 解析
    implementation(libs.gsonConverter)
    implementation(libs.appcompat)
    
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.translate)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}