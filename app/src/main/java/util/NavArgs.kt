```kotlin
package id.temenpulang.app.util

/**
 * Nama-nama argument yang dikirim lewat Navigation Component.
 * Nilainya HARUS sama dengan android:name pada <argument> di nav_graph.xml.
 */
object NavArgs {
    const val NAMA_PERJALANAN = "namaPerjalanan"
    const val TUJUAN = "tujuan"
    const val CATATAN = "catatan"
    const val PERJALANAN_ID = "perjalananId"
}
```