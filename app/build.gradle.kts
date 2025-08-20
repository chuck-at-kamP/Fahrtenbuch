plugins {
    alias(libs.plugins.android.application)
}


android {
    namespace = "com.example.fahrtenbuch"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.fahrtenbuch"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        base.archivesName = "fahrtenbuch-$versionName"
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("androidx.core:core:1.17.0")  // für FileProvider um Markor Editor zu öffnen.
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // https://mvnrepository.com/artifact/org.simpleframework/simple-xml
    implementation("org.simpleframework:simple-xml:2.7.1")
}
