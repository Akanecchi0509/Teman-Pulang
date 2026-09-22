```kotlin
package id.temenpulang.app.data

import android.content.Context
import android.content.SharedPreferences
import id.temenpulang.app.model.Perjalanan
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Menyimpan dan membaca daftar riwayat perjalanan di penyimpanan lokal perangkat
 * (SharedPreferences). Daftar diubah menjadi teks JSON agar mudah disimpan.
 * Memakai org.json bawaan Android, jadi tidak perlu dependency tambahan.
 */
class PerjalananRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(NAMA_PREFS, Context.MODE_PRIVATE)

    /** Membaca semua riwayat. Jika belum ada atau data rusak, hasilnya daftar kosong. */
    fun ambilSemua(): List<Perjalanan> {
        val json = prefs.getString(KUNCI_RIWAYAT, null) ?: return emptyList()
        return try {
            val array = JSONArray(json)
            (0 until array.length()).map { index -> dariJson(array.getJSONObject(index)) }
        } catch (e: JSONException) {
            emptyList()
        }
    }

    /** Menyimpan seluruh daftar riwayat (menimpa data lama). */
    fun simpanSemua(daftar: List<Perjalanan>) {
        val array = JSONArray()
        daftar.forEach { perjalanan -> array.put(keJson(perjalanan)) }
        prefs.edit().putString(KUNCI_RIWAYAT, array.toString()).apply()
    }

    private fun keJson(p: Perjalanan): JSONObject {
        val obj = JSONObject()
        obj.put("id", p.id)
        obj.put("nama", p.nama)
        obj.put("tujuan", p.tujuan)
        obj.put("catatan", p.catatan)
        obj.put("waktuMulai", p.waktuMulai)
        obj.put("waktuSelesai", p.waktuSelesai ?: JSONObject.NULL)
        obj.put("latitude", p.latitude ?: JSONObject.NULL)
        obj.put("longitude", p.longitude ?: JSONObject.NULL)
        obj.put("status", p.status)
        return obj
    }

    private fun dariJson(obj: JSONObject): Perjalanan {
        return Perjalanan(
            id = obj.getInt("id"),
            nama = obj.getString("nama"),
            tujuan = obj.getString("tujuan"),
            catatan = obj.getString("catatan"),
            waktuMulai = obj.getString("waktuMulai"),
            waktuSelesai = if (obj.isNull("waktuSelesai")) null else obj.getString("waktuSelesai"),
            latitude = if (obj.isNull("latitude")) null else obj.getDouble("latitude"),
            longitude = if (obj.isNull("longitude")) null else obj.getDouble("longitude"),
            status = obj.getString("status")
        )
    }

    private companion object {
        const val NAMA_PREFS = "temenpulang_prefs"
        const val KUNCI_RIWAYAT = "riwayat_perjalanan"
    }
}
```