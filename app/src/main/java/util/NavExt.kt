```kotlin
package id.temenpulang.app.util

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController

/**
 * Berpindah halaman hanya jika action tersebut memang ada di halaman saat ini.
 * Mencegah crash ketika pengguna menekan tombol dua kali dengan cepat
 * (klik kedua terjadi saat halaman sudah berpindah).
 */
fun NavController.navigasiAman(@IdRes actionId: Int, args: Bundle? = null) {
    if (currentDestination?.getAction(actionId) != null) {
        navigate(actionId, args)
    }
}
```