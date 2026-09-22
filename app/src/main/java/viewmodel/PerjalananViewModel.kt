```kotlin
package id.temenpulang.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import id.temenpulang.app.data.PerjalananRepository
import id.temenpulang.app.model.Perjalanan
import id.temenpulang.app.model.StatusPerjalanan
import id.temenpulang.app.util.FormatWaktu

/**
 * Menyimpan data yang dipakai bersama oleh banyak Fragment:
 * - riwayat: daftar perjalanan yang sudah selesai (disimpan permanen di SharedPreferences)
 * - perjalananAktif: perjalanan yang sedang berlangsung (hanya di memori)
 *
 * ViewModel dibuat sekali di level Activity (activityViewModels()), sehingga
 * Fragment yang berbeda melihat data yang sama dan data tidak hilang saat layar diputar.
 */
class PerjalananViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PerjalananRepository(application)

    private val _riwayat = MutableLiveData<List<Perjalanan>>(repository.ambilSemua())
    val riwayat: LiveData<List<Perjalanan>> = _riwayat

    private val _perjalananAktif = MutableLiveData<Perjalanan?>(null)
    val perjalananAktif: LiveData<Perjalanan?> = _perjalananAktif

    /** Membuat perjalanan aktif baru. Jika sudah ada yang aktif, tidak dibuat ulang. */
    fun mulaiPerjalananJikaBelumAda(nama: String, tujuan: String, catatan: String) {
        if (_perjalananAktif.value != null) return

        val daftar = _riwayat.value ?: emptyList()
        val idBaru = (daftar.maxOfOrNull { it.id } ?: 0) + 1

        _perjalananAktif.value = Perjalanan(
            id = idBaru,
            nama = nama,
            tujuan = tujuan,
            catatan = catatan,
            waktuMulai = FormatWaktu.sekarang(),
            waktuSelesai = null,
            latitude = null,
            longitude = null,
            status = StatusPerjalanan.BERJALAN
        )
    }

    /** Menyimpan lokasi terbaru ke perjalanan yang sedang aktif. */
    fun perbaruiLokasiAktif(latitude: Double, longitude: Double) {
        val aktif = _perjalananAktif.value ?: return
        _perjalananAktif.value = aktif.copy(latitude = latitude, longitude = longitude)
    }

    /**
     * Menyelesaikan perjalanan aktif: status jadi Selesai, waktu selesai diisi,
     * lalu dimasukkan ke riwayat (paling atas) dan disimpan.
     * Mengembalikan perjalanan yang selesai, atau null jika tidak ada perjalanan aktif.
     */
    fun selesaikanPerjalanan(): Perjalanan? {
        val aktif = _perjalananAktif.value ?: return null

        val selesai = aktif.copy(
            waktuSelesai = FormatWaktu.sekarang(),
            status = StatusPerjalanan.SELESAI
        )

        val daftarBaru = listOf(selesai) + (_riwayat.value ?: emptyList())
        repository.simpanSemua(daftarBaru)
        _riwayat.value = daftarBaru
        _perjalananAktif.value = null
        return selesai
    }

    /** Mencari satu perjalanan di riwayat berdasarkan id. */
    fun cariPerjalanan(id: Int): Perjalanan? {
        return _riwayat.value?.firstOrNull { it.id == id }
    }
}
```