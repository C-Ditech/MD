package com.example.mycapstone

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mycapstone.databinding.ActivityMainBinding
import com.example.mycapstone.ui.Akun.AkunFragment
import com.example.mycapstone.ui.history.HistoryFragment
import com.example.mycapstone.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //kamera jadi


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration.Builder(
                R.id.navigation_home,R.id.navigation_camera, R.id.navigation_history, R.id.navigation_akun
        ).build()
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {

                R.id.navigation_home -> {
                    val fragment = HomeFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit()
                    true
                }

                R.id.navigation_camera -> {
                    val intent = Intent(this, CameraActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.navigation_history -> {
                    val fragment = HistoryFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit()
                    true
                }
                R.id.navigation_akun -> {
                    val fragment = AkunFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit()
                    true
                }
                // Fragmen lainnya
                else -> false
            }
        }
    }
}