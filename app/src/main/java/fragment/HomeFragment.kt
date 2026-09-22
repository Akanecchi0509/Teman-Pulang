```kotlin
package id.temenpulang.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import id.temenpulang.app.R
import id.temenpulang.app.databinding.FragmentHomeBinding
import id.temenpulang.app.model.Perjalanan
import id.temenpulang.app.util.NavArgs
import id.temenpulang.app.util.PerjalananUi
import id.temenpulang.app.util.navigasiAman
import id.temenpulang.app.viewmodel.PerjalananViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PerjalananViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonProfil.setOnClickListener {
            findNavController().navigasiAman(R.id.action_home_to_profil)
        }
        binding.textLihatSemua.setOnClickListener {
            findNavController().navigasiAman(R.id.action_home_to_riwayat)
        }

        viewModel.perjalananAktif.observe(viewLifecycleOwner) { aktif ->
            tampilkanStatusPerjalanan(aktif)
        }
        viewModel.riwayat.observe(viewLifecycleOwner) { daftar ->
            tampilkanPerjalananTerakhir(daftar.firstOrNull())
        }
    }

    /** Kartu status: berbeda tampilan saat ada / tidak ada perjalanan aktif. */
    private fun tampilkanStatusPerjalanan(aktif: Perjalanan?) {
        if (aktif == null) {
            binding.textStatusJudul.setText(R.string.home_status_kosong_judul)
            binding.textStatusDetail.setText(R.string.home_status_kosong_detail)
            binding.buttonMulai.setText(R.string.home_button_mulai)
            binding.buttonMulai.setOnClickListener {
                findNavController().navigasiAman(R.id.action_home_to_perjalanan)
            }
        } else {
            binding.textStatusJudul.setText(R.string.home_status_aktif_judul)
            binding.textStatusDetail.text =
                getString(R.string.home_status_aktif_detail, aktif.nama, aktif.tujuan)
            binding.buttonMulai.setText(R.string.home_button_lanjut)
            binding.buttonMulai.setOnClickListener {
                val args = bundleOf(
                    NavArgs.NAMA_PERJALANAN to aktif.nama,
                    NavArgs.TUJUAN to aktif.tujuan,
                    NavArgs.CATATAN to aktif.catatan
                )
                findNavController().navigasiAman(R.id.action_home_to_tracking, args)
            }
        }
    }

    /** Ringkasan perjalanan terakhir (atau pesan kosong jika belum ada). */
    private fun tampilkanPerjalananTerakhir(terakhir: Perjalanan?) {
        val ada = terakhir != null
        binding.itemTerakhir.root.isVisible = ada
        binding.cardTerakhirKosong.isVisible = !ada

        if (terakhir != null) {
            PerjalananUi.tampilkanItem(binding.itemTerakhir, terakhir)
            binding.itemTerakhir.root.setOnClickListener {
                findNavController().navigasiAman(
                    R.id.action_home_to_detail,
                    bundleOf(NavArgs.PERJALANAN_ID to terakhir.id)
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
```