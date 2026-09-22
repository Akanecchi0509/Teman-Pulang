```kotlin
package id.temenpulang.app.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

/** Pusat pengecekan izin lokasi, dipakai oleh PerjalananFragment dan TrackingFragment. */
object IzinLokasi {

    /** Izin yang diminta ke pengguna (harus juga tertulis di AndroidManifest.xml). */
    val DAFTAR_IZIN = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    /**
     * True jika pengguna sudah memberi salah satu izin lokasi.
     * Di Android 12+, pengguna boleh hanya memberi lokasi "perkiraan" (COARSE).
     */
    fun sudahDiberikan(context: Context): Boolean {
        return DAFTAR_IZIN.any { izin ->
            ContextCompat.checkSelfPermission(context, izin) == PackageManager.PERMISSION_GRANTED
        }
    }
}
```