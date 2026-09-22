```kotlin
package id.temenpulang.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import id.temenpulang.app.R
import id.temenpulang.app.databinding.FragmentDetailPerjalananBinding
import id.temenpulang.app.model.Perjalanan
import id.temenpulang.app.util.NavArgs
import id.temenpulang.app.util.PerjalananUi
import id.temenpulang.app.util.navigasiAman
import id.temenpulang.app.viewmodel.PerjalananViewModel

class DetailPerjalananFragment : Fragment() {

    private var _binding: FragmentDetailPerjalananBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PerjalananViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailPerjalananBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonKembali.setOnClickListener { findNavController().navigateUp() }
        binding.buttonHome.setOnClickListener { kembaliKeHome() }
        binding.buttonRiwayat.setOnClickListener {
            findNavController().navigasiAman(R.id.action_detail_to_riwayat)
        }

        // Ambil id perjalanan dari argument, lalu cari datanya di ViewModel
        val id = requireArguments().getInt(NavArgs.PERJALANAN_ID, -1)
        val perjalanan = viewModel.cariPerjalanan(id)

        if (perjalanan == null) {
            binding.layoutKonten.isVisible = false
            binding.textTidakDitemukan.isVisible = true
        } else {
            tampilkanData(perjalanan)
        }
    }

    private fun tampilkanData(p: Perjalanan) {
        binding.textNama.text = p.nama
        binding.textTujuan.text = p.tujuan
        binding.textCatatan.text =
            if (p.catatan.isBlank()) getString(R.string.detail_catatan_kosong) else p.catatan
        binding.textWaktuMulai.text = p.waktuMulai
        binding.textWaktuSelesai.text = p.waktuSelesai ?: getString(R.string.nilai_kosong)

        val lat = p.latitude
        val lon = p.longitude
        binding.textLokasi.text = if (lat != null && lon != null) {
            getString(
                R.string.detail_lokasi_format,
                PerjalananUi.formatKoordinat(lat),
                PerjalananUi.formatKoordinat(lon)
            )
        } else {
            getString(R.string.detail_lokasi_tidak_ada)
        }

        PerjalananUi.gayaStatus(binding.textStatus, p.status)
    }

    /** Kembali ke Home dengan membuang halaman-halaman di atasnya dari back stack. */
    private fun kembaliKeHome() {
        val navController = findNavController()
        if (!navController.popBackStack(R.id.homeFragment, false)) {
            navController.navigate(R.id.homeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
```

**Fungsi file:** Membaca argument perjalananId, mencari data di ViewModel, dan menampilkannya. Tombol Home memakai popBackStack; tombol Riwayat memakai action_detail_to_riwayat.


---

## TAHAP 13 - ProfilFragment

NAMA FILE:  
`app/src/main/res/layout/fragment_profil.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fillViewport="true"
    android:scrollbars="none">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="20dp">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/profil_judul"
            android:textAppearance="@style/TextAppearance.TemenPulang.Headline" />

        <!-- Foto profil (placeholder), nama, email -->
        <ImageView
            android:layout_width="88dp"
            android:layout_height="88dp"
            android:layout_gravity="center_horizontal"
            android:layout_marginTop="24dp"
            android:background="@drawable/bg_icon_circle"
            android:contentDescription="@string/cd_avatar"
            android:padding="22dp"
            android:src="@drawable/ic_person"
            app:tint="@color/tp_primary" />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center_horizontal"
            android:layout_marginTop="14dp"
            android:text="@string/profil_nama"
            android:textAppearance="@style/TextAppearance.TemenPulang.Title" />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center_horizontal"
            android:layout_marginTop="2dp"
            android:text="@string/profil_email"
            android:textAppearance="@style/TextAppearance.TemenPulang.Body" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="12dp"
            android:gravity="center"
            android:text="@string/profil_catatan_dummy"
            android:textAppearance="@style/TextAppearance.TemenPulang.Label" />

        <!-- Section: Informasi Pengguna -->
        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="28dp"
            android:layout_marginBottom="10dp"
            android:text="@string/profil_section_info"
            android:textAppearance="@style/TextAppearance.TemenPulang.Title" />

        <com.google.android.material.card.MaterialCardView
            style="@style/Widget.TemenPulang.Card"
            android:layout_width="match_parent"
            android:layout_height="wrap_content">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical"
                android:padding="20dp">

                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="@string/profil_label_nama"
                    android:textAppearance="@style/TextAppearance.TemenPulang.Label" />

                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="2dp"
                    android:layout_marginBottom="14dp"
                    android:text="@string/profil_nama"
                    android:textAppearance="@style/TextAppearance.TemenPulang.Value" />

                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="@string/profil_label_email"
                    android:textAppearance="@style/TextAppearance.TemenPulang.Label" />

                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="2dp"
                    android:layout_marginBottom="14dp"
                    android:text="@string/profil_email"
                    android:textAppearance="@style/TextAppearance.TemenPulang.Value" />

                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="@string/profil_label_akun"
                    android:textAppearance="@style/TextAppearance.TemenPulang.Label" />

                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="2dp"
                    android:text="@string/profil_akun_nilai"
                    android:textAppearance="@style/TextAppearance.TemenPulang.Value" />
            </LinearLayout>
        </com.google.android.material.card.MaterialCardView>

        <!-- Section: Pengaturan -->
        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="24dp"
            android:layout_marginBottom="10dp"
            android:text="@string/profil_section_pengaturan"
            android:textAppearance="@style/TextAppearance.TemenPulang.Title" />

        <com.google.android.material.card.MaterialCardView
            style="@style/Widget.TemenPulang.Card"
            android:layout_width="match_parent"
            android:layout_height="wrap_content">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical"
                android:padding="20dp">

                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="@string/profil_notifikasi"
                    android:textAppearance="@style/TextAppearance.TemenPulang.Label" />

                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="2dp"
                    android:layout_marginBottom="14dp"
                    android:text="@string/profil_notifikasi_nilai"
                    android:textAppearance="@style/TextAppearance.TemenPulang.Value" />

                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="@string/profil_bahasa"
                    android:textAppearance="@style/TextAppearance.TemenPulang.Label" />

                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="2dp"
                    android:text="@string/profil_bahasa_nilai"
                    android:textAppearance="@style/TextAppearance.TemenPulang.Value" />
            </LinearLayout>
        </com.google.android.material.card.MaterialCardView>

        <!-- Section: Tentang Aplikasi -->
        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="24dp"
            android:layout_marginBottom="10dp"
            android:text="@string/profil_section_tentang"
            android:textAppearance="@style/TextAppearance.TemenPulang.Title" />

        <com.google.android.material.card.MaterialCardView
            style="@style/Widget.TemenPulang.Card"
            android:layout_width="match_parent"
            android:layout_height="wrap_content">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical"
                android:padding="20dp">

                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="@string/profil_tentang_versi"
                    android:textAppearance="@style/TextAppearance.TemenPulang.Value" />

                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="8dp"
                    android:text="@string/profil_tentang_deskripsi"
                    android:textAppearance="@style/TextAppearance.TemenPulang.Body" />
            </LinearLayout>
        </com.google.android.material.card.MaterialCardView>

    </LinearLayout>
</ScrollView>
```