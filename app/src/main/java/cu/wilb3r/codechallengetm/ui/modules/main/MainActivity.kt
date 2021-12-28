package cu.wilb3r.codechallengetm.ui.modules.main

import android.os.Bundle
import android.os.Looper
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import cu.wilb3r.codechallengetm.R
import cu.wilb3r.codechallengetm.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity @Inject constructor() : AppCompatActivity(),
    NavController.OnDestinationChangedListener,
    Toolbar.OnMenuItemClickListener {

    lateinit var binding: ActivityMainBinding
    private var back: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_CodeChallengeTM)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        NavigationUI.setupWithNavController(
            binding.bottomAppBar,
            navHost.navController
        )
        binding.bottomAppBar.setOnItemReselectedListener { }

    }

    override fun onBackPressed() {
        val navController = findNavController(R.id.nav_host_fragment)
        when (navController.graph.startDestination) {
            navController.currentDestination?.id -> {
                if (back) {
                    super.onBackPressed()
                    overridePendingTransition(0, 0)
                    return
                }
                back = true
                Toast.makeText(this, "Press back to exit", Toast.LENGTH_SHORT).show()
                android.os.Handler(Looper.getMainLooper()).postDelayed({ back = false }, 2000)
            }
            else -> super.onBackPressed()
        }
    }


    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        TODO("Not yet implemented")
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        TODO("Not yet implemented")
    }

}