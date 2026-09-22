```kotlin
package id.temenpulang.app.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.temenpulang.app.databinding.ItemPerjalananBinding
import id.temenpulang.app.model.Perjalanan
import id.temenpulang.app.util.PerjalananUi

/**
 * Adapter menghubungkan daftar data Perjalanan dengan RecyclerView.
 *
 * Alurnya:
 * RecyclerView -> meminta Adapter membuat ViewHolder (onCreateViewHolder)
 *              -> ViewHolder memegang layout item_perjalanan.xml
 *              -> Adapter mengisi ViewHolder dengan data (onBindViewHolder)
 */
class PerjalananAdapter(
    private val onItemClick: (Perjalanan) -> Unit
) : RecyclerView.Adapter<PerjalananAdapter.PerjalananViewHolder>() {

    private val daftar = mutableListOf<Perjalanan>()

    /** Mengganti seluruh isi daftar lalu memberi tahu RecyclerView agar digambar ulang. */
    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataBaru: List<Perjalanan>) {
        daftar.clear()
        daftar.addAll(dataBaru)
        notifyDataSetChanged()
    }

    /** ViewHolder: memegang satu baris (item_perjalanan.xml) dan mengisinya dengan data. */
    inner class PerjalananViewHolder(
        private val binding: ItemPerjalananBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(perjalanan: Perjalanan) {
            PerjalananUi.tampilkanItem(binding, perjalanan)
            binding.root.setOnClickListener { onItemClick(perjalanan) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerjalananViewHolder {
        val binding = ItemPerjalananBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PerjalananViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PerjalananViewHolder, position: Int) {
        holder.bind(daftar[position])
    }

    override fun getItemCount(): Int = daftar.size
}
```