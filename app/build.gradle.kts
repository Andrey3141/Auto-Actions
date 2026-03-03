plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.printer.information"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.printer.information"
        minSdk = 24
        targetSdk = 35
        versionCode = 5
        versionName = "1.1.1"

        externalNativeBuild {
            cmake {
                cFlags.addAll(listOf(
                    "-std=c99",
                    "-Os",
                    "-fvisibility=hidden",
                    "-fdata-sections",
                    "-ffunction-sections",
                    "-flto",
                    "-fomit-frame-pointer",
                    "-ffast-math",
                    "-fstrict-aliasing"
                ))

                arguments.addAll(listOf(
                    "-DANDROID_STL=none",
                    "-DCMAKE_BUILD_TYPE=Release"
                ))
            }
        }
        ndk {
            abiFilters.add("arm64-v8a")
        }
    }

    splits {
        abi {
            isEnable = true
            reset()
            include("arm64-v8a")
            isUniversalApk = true
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file("release-key.jks")
            storePassword = "123456"
            keyAlias = "mykey"
            keyPassword = "123456"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }

    externalNativeBuild {
        cmake {
            path = file("src/main/c/CMakeLists.txt")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.2.0")  // ДОБАВЬ ЭТО
    implementation("com.google.android.material:material:1.11.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}