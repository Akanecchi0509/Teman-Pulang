```kotlin
package id.temenpulang.app

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import id.temenpulang.app.databinding.ActivityMainBinding

/**
 * Satu-satunya Activity. Isinya hanya NavHostFragment (wadah Fragment)
 * dan BottomNavigationView. Semua halaman lain adalah Fragment.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Ambil NavHostFragment dari layout, lalu ambil NavController-nya
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // 2. Hubungkan BottomNavigationView dengan NavController
        //    (id item menu = id destination di nav_graph.xml)
        binding.bottomNavigation.setupWithNavController(navController)

        // 3. Bottom navigation hanya tampil di Home, Riwayat, dan Profil
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavigation.visibility = when (destination.id) {
                R.id.homeFragment,
                R.id.riwayatFragment,
                R.id.profilFragment -> View.VISIBLE
                else -> View.GONE
            }
        }
    }
}
```