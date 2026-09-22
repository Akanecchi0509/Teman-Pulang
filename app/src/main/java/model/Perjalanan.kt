```kotlin
package id.temenpulang.app.model

/**
 * Data satu perjalanan.
 * Dipakai oleh ViewModel, Repository, Adapter, dan semua Fragment.
 */
data class Perjalanan(
    val id: Int,
    val nama: String,
    val tujuan: String,
    val catatan: String,
    val waktuMulai: String,
    val waktuSelesai: String?,
    val latitude: Double?,
    val longitude: Double?,
    val status: String
) {
    /** Bagian tanggal saja dari waktuMulai. Contoh: "19 September 2026" */
    val tanggal: String
        get() = waktuMulai.substringBefore(",").trim()
}

/** Nilai status yang dipakai di seluruh aplikasi (satu sumber, agar tidak salah ketik). */
object StatusPerjalanan {
    const val BERJALAN = "Berjalan"
    const val SELESAI = "Selesai"
}
```