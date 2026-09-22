```kotlin
package id.temenpulang.app.util

import android.widget.TextView
import androidx.core.content.ContextCompat
import id.temenpulang.app.R
import id.temenpulang.app.databinding.ItemPerjalananBinding
import id.temenpulang.app.model.Perjalanan
import id.temenpulang.app.model.StatusPerjalanan
import java.util.Locale

/**
 * Fungsi bantu untuk menampilkan data Perjalanan ke layout.
 * Dipakai bersama oleh PerjalananAdapter (Riwayat), HomeFragment, TrackingFragment,
 * dan DetailPerjalananFragment supaya tampilan konsisten.
 */
object PerjalananUi {

    /** Mengisi layout item_perjalanan.xml dengan data satu perjalanan. */
    fun tampilkanItem(binding: ItemPerjalananBinding, perjalanan: Perjalanan) {
        val context = binding.root.context
        binding.textNama.text = perjalanan.nama
        binding.textTujuan.text = context.getString(R.string.item_tujuan_format, perjalanan.tujuan)
        binding.textTanggal.text = perjalanan.tanggal
        gayaStatus(binding.textStatus, perjalanan.status)
    }

    /** Mengatur teks dan warna "chip" status (hijau = Selesai, kuning = Berjalan). */
    fun gayaStatus(textView: TextView, status: String) {
        val context = textView.context
        textView.text = status
        if (status == StatusPerjalanan.SELESAI) {
            textView.setBackgroundResource(R.drawable.bg_status_selesai)
            textView.setTextColor(ContextCompat.getColor(context, R.color.tp_success))
        } else {
            textView.setBackgroundResource(R.drawable.bg_status_berjalan)
            textView.setTextColor(ContextCompat.getColor(context, R.color.tp_warning))
        }
    }

    /** Format koordinat 6 angka di belakang koma, contoh: -6.208800 */
    fun formatKoordinat(nilai: Double): String {
        return String.format(Locale.US, "%.6f", nilai)
    }
}
```