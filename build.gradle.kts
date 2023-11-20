buildscript {
    repositories {
        mavenCentral()
        mavenLocal()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.2")
        classpath("com.google.gms:google-services:4.4.0") // Make sure this version is up-to-date
        // Add other classpath dependencies if needed
        classpath ("com.android.tools.build:gradle:7.4.2")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")
    }
}

plugins {
    id("com.android.application") version "8.1.3" apply false
    id("com.google.gms.google-services") version "4.3.15" apply false
}

allprojects {
    repositories {
    }
}

