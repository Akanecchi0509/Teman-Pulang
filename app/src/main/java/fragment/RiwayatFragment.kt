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
import androidx.recyclerview.widget.LinearLayoutManager
import id.temenpulang.app.R
import id.temenpulang.app.adapter.PerjalananAdapter
import id.temenpulang.app.databinding.FragmentRiwayatBinding
import id.temenpulang.app.util.NavArgs
import id.temenpulang.app.util.navigasiAman
import id.temenpulang.app.viewmodel.PerjalananViewModel

class RiwayatFragment : Fragment() {

    private var _binding: FragmentRiwayatBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PerjalananViewModel by activityViewModels()

    private lateinit var perjalananAdapter: PerjalananAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRiwayatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Adapter menerima "aksi klik": berpindah ke Detail lewat Navigation Component
        perjalananAdapter = PerjalananAdapter { perjalanan ->
            findNavController().navigasiAman(
                R.id.action_riwayat_to_detail,
                bundleOf(NavArgs.PERJALANAN_ID to perjalanan.id)
            )
        }

        binding.recyclerRiwayat.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerRiwayat.adapter = perjalananAdapter

        // Setiap riwayat berubah, isi adapter diperbarui otomatis
        viewModel.riwayat.observe(viewLifecycleOwner) { daftar ->
            perjalananAdapter.setData(daftar)
            binding.recyclerRiwayat.isVisible = daftar.isNotEmpty()
            binding.textKosong.isVisible = daftar.isEmpty()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerRiwayat.adapter = null
        _binding = null
    }
}
```

**Fungsi file:** Memasang LayoutManager dan Adapter, mengamati riwayat dari ViewModel, dan membuka Detail saat item diklik (action_riwayat_to_detail).


---

## TAHAP 12 - DetailPerjalananFragment

NAMA FILE:  
`app/src/main/res/layout/fragment_detail_perjalanan.xml`

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

        <!-- Header: tombol kembali + judul -->
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:gravity="center_vertical"
            android:orientation="horizontal">

            <ImageButton
                android:id="@+id/button_kembali"
                android:layout_width="44dp"
                android:layout_height="44dp"
                android:background="@drawable/bg_icon_circle"
                android:contentDescription="@string/cd_kembali"
                android:padding="10dp"
                android:scaleType="fitCenter"
                android:src="@drawable/ic_arrow_back"
                app:tint="@color/tp_primary" />

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginStart="16dp"
                android:text="@string/detail_judul"
                android:textAppearance="@style/TextAppearance.TemenPulang.Headline" />
        </LinearLayout>

        <!-- Tampil hanya jika data tidak ditemukan -->
        <TextView
            android:id="@+id/text_tidak_ditemukan"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="32dp"
            android:gravity="center"
            android:text="@string/detail_tidak_ditemukan"
            android:textAppearance="@style/TextAppearance.TemenPulang.Body"
            android:visibility="gone" />

        <!-- Seluruh isi detail -->
        <LinearLayout
            android:id="@+id/layout_konten"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="24dp"
            android:orientation="vertical">

            <ImageView
                android:layout_width="72dp"
                android:layout_height="72dp"
                android:layout_gravity="center_horizontal"
                android:background="@drawable/bg_success_circle"
                android:importantForAccessibility="no"
                android:padding="20dp"
                android:src="@drawable/ic_check"
                app:tint="@color/tp_success" />

            <com.google.android.material.card.MaterialCardView
                style="@style/Widget.TemenPulang.Card"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="24dp">

                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="vertical"
                    android:padding="20dp">

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="@string/label_nama_perjalanan"
                        android:textAppearance="@style/TextAppearance.TemenPulang.Label" />

                    <TextView
                        android:id="@+id/text_nama"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:layout_marginTop="2dp"
                        android:layout_marginBottom="16dp"
                        android:textAppearance="@style/TextAppearance.TemenPulang.Title" />

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="@string/label_tujuan"
                        android:textAppearance="@style/TextAppearance.TemenPulang.Label" />

                    <TextView
                        android:id="@+id/text_tujuan"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:layout_marginTop="2dp"
                        android:layout_marginBottom="16dp"
                        android:textAppearance="@style/TextAppearance.TemenPulang.Value" />

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="@string/label_catatan"
                        android:textAppearance="@style/TextAppearance.TemenPulang.Label" />

                    <TextView
                        android:id="@+id/text_catatan"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:layout_marginTop="2dp"
                        android:layout_marginBottom="16dp"
                        android:textAppearance="@style/TextAppearance.TemenPulang.Value" />

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="@string/label_waktu_mulai"
                        android:textAppearance="@style/TextAppearance.TemenPulang.Label" />

                    <TextView
                        android:id="@+id/text_waktu_mulai"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:layout_marginTop="2dp"
                        android:layout_marginBottom="16dp"
                        android:textAppearance="@style/TextAppearance.TemenPulang.Value" />

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="@string/label_waktu_selesai"
                        android:textAppearance="@style/TextAppearance.TemenPulang.Label" />

                    <TextView
                        android:id="@+id/text_waktu_selesai"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:layout_marginTop="2dp"
                        android:layout_marginBottom="16dp"
                        android:textAppearance="@style/TextAppearance.TemenPulang.Value" />

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="@string/label_lokasi_terakhir"
                        android:textAppearance="@style/TextAppearance.TemenPulang.Label" />

                    <TextView
                        android:id="@+id/text_lokasi"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:layout_marginTop="2dp"
                        android:layout_marginBottom="16dp"
                        android:textAppearance="@style/TextAppearance.TemenPulang.Value" />

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="@string/label_status"
                        android:textAppearance="@style/TextAppearance.TemenPulang.Label" />

                    <TextView
                        android:id="@+id/text_status"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_marginTop="6dp"
                        android:paddingStart="12dp"
                        android:paddingTop="4dp"
                        android:paddingEnd="12dp"
                        android:paddingBottom="4dp"
                        android:textSize="13sp"
                        android:textStyle="bold" />
                </LinearLayout>
            </com.google.android.material.card.MaterialCardView>

            <com.google.android.material.button.MaterialButton
                android:id="@+id/button_home"
                style="@style/Widget.TemenPulang.Button"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="24dp"
                android:text="@string/detail_button_home" />

            <com.google.android.material.button.MaterialButton
                android:id="@+id/button_riwayat"
                style="@style/Widget.TemenPulang.Button.Outlined"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="12dp"
                android:text="@string/detail_button_riwayat" />
        </LinearLayout>

    </LinearLayout>
</ScrollView>
```