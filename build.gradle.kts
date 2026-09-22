```kotlin
// File build level project: hanya mendeklarasikan versi plugin.
// Plugin baru diterapkan (apply) di app/build.gradle.kts.
plugins {
    id("com.android.application") version "8.5.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false
}
```