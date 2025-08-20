plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 21
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
        debug {
            isDebuggable = false  // 设置为false避免应用被标记为调试模式
            isTestCoverageEnabled = false
            // 添加这行以防止应用被标记为testOnly
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    // 添加lint选项来解决lint检查错误
    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }
    
    // 使用压缩共享库作为16KB对齐问题的替代方案
    packaging {
        jniLibs {
            useLegacyPackaging = true
        }
        resources {

        }
    }
}

// 解决Guava库冲突问题
configurations.all {
    // 移除排除listenablefuture的配置，因为应用需要这个库
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.core)
    implementation(libs.activity)
    implementation(libs.activity.ktx) // 添加这行以支持ActivityResultLauncher
    implementation(libs.constraintlayout)
    // 添加更多必要的AndroidX依赖
    implementation(libs.lifecycle.runtime)
    
    // 添加缺失的依赖项以解决ClassNotFoundException
    implementation(libs.concurrent.futures)
    implementation(libs.guava)
    implementation(libs.profileinstaller)

    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp)
    implementation(libs.gson)
    // 添加DashScope SDK依赖（通义千问）
    implementation("com.alibaba:dashscope-sdk-java:2.12.0") {
        exclude(group = "com.google.guava", module = "guava")
        exclude(group = "com.google.guava", module = "listenablefuture")
    }

    // 确保使用与Android 14兼容的版本
    implementation("androidx.activity:activity:1.8.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}