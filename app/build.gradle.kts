plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("kotlin-android")
    id("kotlin-android-extensions")
}

android {
    namespace = "com.cosc4319.adapti_project"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.cosc4319.adapti_project"
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

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-database:20.2.2")
    implementation("com.google.firebase:firebase-auth:22.1.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.navigation:navigation-fragment:2.7.4")
    implementation("androidx.navigation:navigation-ui:2.7.4")
    implementation("com.google.android.gms:play-services-mlkit-text-recognition-common:19.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //implementation("com.firebaseui:firebase-ui-firestore:8.0.2")
    // ML Kit for text recognition
    // Text features
    implementation("com.google.mlkit:text-recognition:16.0.0")
    // Or comment the dependency above and uncomment the dependency below to
    // use unbundled model that depends on Google Play Services
    // implementation 'com.google.android.gms:play-services-mlkit-text-recognition:19.0.0'
    implementation("com.google.mlkit:text-recognition-chinese:16.0.0")
    // Or comment the dependency above and uncomment the dependency below to
    // use unbundled model that depends on Google Play Services
    // implementation 'com.google.android.gms:play-services-mlkit-text-recognition-chinese:16.0.0'
    implementation("com.google.mlkit:text-recognition-devanagari:16.0.0")
    // Or comment the dependency above and uncomment the dependency below to
    // use unbundled model that depends on Google Play Services
    // implementation 'com.google.android.gms:play-services-mlkit-text-recognition-devanagari:16.0.0'
    implementation("com.google.mlkit:text-recognition-japanese:16.0.0")
    // Or comment the dependency above and uncomment the dependency below to
    // use unbundled model that depends on Google Play Services
    // implementation 'com.google.android.gms:play-services-mlkit-text-recognition-japanese:16.0.0'
    implementation("com.google.mlkit:text-recognition-korean:16.0.0")
    // Or comment the dependency above and uncomment the dependency below to
    // use unbundled model that depends on Google Play Services
    // implementation 'com.google.android.gms:play-services-mlkit-text-recognition-korean:16.0.0'

    // Image labeling
    implementation("com.google.mlkit:image-labeling:17.0.7")
    // Or comment the dependency above and uncomment the dependency below to
    // use unbundled model that depends on Google Play Services
    // implementation 'com.google.android.gms:play-services-mlkit-image-labeling:16.0.8'

    // Image labeling custom
    implementation("com.google.mlkit:image-labeling-custom:17.0.1")
// Or comment the dependency above and uncomment the dependency below to
// use unbundled model that depends on Google Play Services
// implementation 'com.google.android.gms:play-services-mlkit-image-labeling-custom:16.0.0-beta4'

    implementation("com.google.code.gson:gson:2.8.6")
    implementation("com.google.guava:guava:27.1-android")

    // For how to setup gradle dependencies in Android X, see:
    // https://developer.android.com/training/testing/set-up-project#gradle-dependencies
    // Core library
    androidTestImplementation("androidx.test:core:1.4.0")

    // AndroidJUnitRunner and JUnit Rules
    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("androidx.test:rules:1.4.0")

    // Assertions
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    // ViewModel and LiveData
    implementation("androidx.lifecycle:lifecycle-livedata:2.3.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.3.1")

    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.annotation:annotation:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")

    // CameraX
    implementation("androidx.camera:camera-camera2:1.0.0-SNAPSHOT")
    implementation("androidx.camera:camera-lifecycle:1.0.0-SNAPSHOT")
    implementation("androidx.camera:camera-view:1.0.0-SNAPSHOT")
    // On Device Machine Learnings
    implementation ("com.google.android.odml:image:1.0.0-beta1")
}
configurations {
    all {
        exclude(group = "com.google.guava", module = "listenablefuture")
    }
}
