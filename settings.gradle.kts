pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        // No need to add the snapshot repository here unless you also have plugins that require it
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        // Add your AndroidX Snapshot repository here:
        maven("https://androidx.dev/snapshots/builds/6787662/artifacts/repository/")
    }
}

rootProject.name = "Adapti_v2"
include(":app")