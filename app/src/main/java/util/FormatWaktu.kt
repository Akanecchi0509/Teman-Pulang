```kotlin
package id.temenpulang.app.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Membuat teks waktu saat ini, contoh: "19 September 2026, 14:30". */
object FormatWaktu {

    fun sekarang(): String {
        val format = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("id", "ID"))
        return format.format(Date())
    }
}
```