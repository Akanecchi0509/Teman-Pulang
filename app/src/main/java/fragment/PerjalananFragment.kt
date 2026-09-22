```kotlin
package id.temenpulang.app.fragment

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import id.temenpulang.app.R
import id.temenpulang.app.databinding.FragmentPerjalananBinding
import id.temenpulang.app.util.IzinLokasi
import id.temenpulang.app.util.NavArgs
import id.temenpulang.app.util.navigasiAman

class PerjalananFragment : Fragment() {

    private var _binding: FragmentPerjalananBinding? = null
    private val binding get() = _binding!!

    /**
     * Peluncur permintaan izin. Harus dibuat sebagai properti (sebelum Fragment STARTED).
     * Hasilnya (diberikan / ditolak) diterima di lambda di bawah.
     */
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { hasil ->
        val diberikan = hasil[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            hasil[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (diberikan) {
            lanjutKeTracking()
        } else {
            tampilkanDialogIzinDitolak()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPerjalananBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonKembali.setOnClickListener { findNavController().navigateUp() }
        binding.buttonMulaiSekarang.setOnClickListener { mulaiSekarang() }

        // Hapus pesan error begitu pengguna mulai mengetik
        binding.editNama.doAfterTextChanged { binding.layoutNama.error = null }
        binding.editTujuan.doAfterTextChanged { binding.layoutTujuan.error = null }
    }

    /** Urutan: validasi input -> cek izin -> (minta izin) -> Tracking. */
    private fun mulaiSekarang() {
        if (!validasiInput()) return

        when {
            // Izin sudah ada: langsung lanjut
            IzinLokasi.sudahDiberikan(requireContext()) -> lanjutKeTracking()

            // Pengguna pernah menolak sekali: jelaskan dulu kenapa izin dibutuhkan
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) ->
                tampilkanDialogRasional()

            // Belum pernah diminta: minta sekarang
            else -> mintaIzinLokasi()
        }
    }

    private fun validasiInput(): Boolean {
        val nama = binding.editNama.text?.toString().orEmpty().trim()
        val tujuan = binding.editTujuan.text?.toString().orEmpty().trim()

        binding.layoutNama.error =
            if (nama.isEmpty()) getString(R.string.error_nama_kosong) else null
        binding.layoutTujuan.error =
            if (tujuan.isEmpty()) getString(R.string.error_tujuan_kosong) else null

        return nama.isNotEmpty() && tujuan.isNotEmpty()
    }

    private fun mintaIzinLokasi() {
        // Memunculkan dialog izin bawaan Android
        requestPermissionLauncher.launch(IzinLokasi.DAFTAR_IZIN)
    }

    private fun tampilkanDialogRasional() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.izin_rasional_judul)
            .setMessage(R.string.izin_rasional_pesan)
            .setPositiveButton(R.string.izin_button_lanjut) { _, _ -> mintaIzinLokasi() }
            .setNegativeButton(R.string.izin_button_batal, null)
            .show()
    }

    private fun tampilkanDialogIzinDitolak() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.izin_ditolak_judul)
            .setMessage(R.string.izin_ditolak_pesan)
            .setPositiveButton(R.string.izin_button_pengaturan) { _, _ -> bukaPengaturanAplikasi() }
            .setNegativeButton(R.string.izin_button_batal, null)
            .show()
    }

    /** Membuka layar pengaturan aplikasi ini (Intent hanya dipakai untuk membuka Pengaturan sistem). */
    private fun bukaPengaturanAplikasi() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", requireContext().packageName, null)
        }
        startActivity(intent)
    }

    private fun lanjutKeTracking() {
        val args = bundleOf(
            NavArgs.NAMA_PERJALANAN to binding.editNama.text?.toString().orEmpty().trim(),
            NavArgs.TUJUAN to binding.editTujuan.text?.toString().orEmpty().trim(),
            NavArgs.CATATAN to binding.editCatatan.text?.toString().orEmpty().trim()
        )
        findNavController().navigasiAman(R.id.action_perjalanan_to_tracking, args)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
```