```kotlin
package id.temenpulang.app.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import id.temenpulang.app.R
import id.temenpulang.app.databinding.FragmentTrackingBinding
import id.temenpulang.app.model.Perjalanan
import id.temenpulang.app.util.IzinLokasi
import id.temenpulang.app.util.NavArgs
import id.temenpulang.app.util.PerjalananUi
import id.temenpulang.app.util.navigasiAman
import id.temenpulang.app.viewmodel.PerjalananViewModel

/**
 * Halaman saat perjalanan berlangsung.
 *
 * CATATAN: ini versi sederhana. Lokasi diambil SATU KALI saat halaman dibuka dan
 * setiap kali tombol "Perbarui Lokasi" ditekan. Ini BUKAN pelacakan real-time yang
 * terus berjalan di latar belakang.
 */
class TrackingFragment : Fragment() {

    private var _binding: FragmentTrackingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PerjalananViewModel by activityViewModels()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var cancellationTokenSource: CancellationTokenSource? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrackingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Data dari halaman sebelumnya (Navigation Component argument)
        val args = requireArguments()
        viewModel.mulaiPerjalananJikaBelumAda(
            nama = args.getString(NavArgs.NAMA_PERJALANAN).orEmpty(),
            tujuan = args.getString(NavArgs.TUJUAN).orEmpty(),
            catatan = args.getString(NavArgs.CATATAN).orEmpty()
        )

        val sudahPunyaLokasi = viewModel.perjalananAktif.value?.latitude != null

        // Tampilan selalu mengikuti data perjalanan aktif di ViewModel
        viewModel.perjalananAktif.observe(viewLifecycleOwner) { aktif ->
            if (aktif != null) tampilkanData(aktif)
        }

        binding.buttonPerbaruiLokasi.setOnClickListener { perbaruiLokasi() }
        binding.buttonSelesai.setOnClickListener { selesaikanPerjalanan() }

        // Ambil lokasi pertama kali saat halaman dibuka
        if (!sudahPunyaLokasi) perbaruiLokasi()
    }

    private fun tampilkanData(aktif: Perjalanan) {
        binding.textNama.text = aktif.nama
        binding.textTujuan.text = aktif.tujuan

        val lat = aktif.latitude
        val lon = aktif.longitude
        if (lat != null && lon != null) {
            binding.textLokasiStatus.setText(R.string.tracking_lokasi_berhasil)
            binding.textLatitude.text = PerjalananUi.formatKoordinat(lat)
            binding.textLongitude.text = PerjalananUi.formatKoordinat(lon)
        }
    }

    /** Meminta lokasi terkini satu kali dari FusedLocationProviderClient. */
    @SuppressLint("MissingPermission") // izin sudah dicek lewat IzinLokasi.sudahDiberikan()
    private fun perbaruiLokasi() {
        if (!IzinLokasi.sudahDiberikan(requireContext())) {
            binding.textLokasiStatus.setText(R.string.tracking_error_izin)
            return
        }

        cancellationTokenSource?.cancel()
        val tokenSource = CancellationTokenSource()
        cancellationTokenSource = tokenSource

        binding.textLokasiStatus.setText(R.string.tracking_lokasi_diperbarui)
        binding.buttonPerbaruiLokasi.isEnabled = false

        fusedLocationClient
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, tokenSource.token)
            .addOnSuccessListener { lokasi ->
                val b = _binding ?: return@addOnSuccessListener
                b.buttonPerbaruiLokasi.isEnabled = true
                if (lokasi != null) {
                    // Menyimpan ke ViewModel; observer di atas yang memperbarui tampilan
                    viewModel.perbaruiLokasiAktif(lokasi.latitude, lokasi.longitude)
                } else {
                    b.textLokasiStatus.setText(R.string.tracking_error_kosong)
                }
            }
            .addOnFailureListener {
                val b = _binding ?: return@addOnFailureListener
                b.buttonPerbaruiLokasi.isEnabled = true
                b.textLokasiStatus.setText(R.string.tracking_error_gagal)
            }
    }

    private fun selesaikanPerjalanan() {
        val selesai = viewModel.selesaikanPerjalanan() ?: return
        findNavController().navigasiAman(
            R.id.action_tracking_to_detail,
            bundleOf(NavArgs.PERJALANAN_ID to selesai.id)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cancellationTokenSource?.cancel()
        cancellationTokenSource = null
        _binding = null
    }
}
```