package com.example.mycapstone

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.mycapstone.databinding.ActivityMainBinding
import com.example.mycapstone.databinding.ActivityUploadBinding
import com.example.mycapstone.ui.Akun.AkunFragment
import com.example.mycapstone.ui.history.HistoryFragment
import com.example.mycapstone.ui.home.HomeFragment
import com.example.mycapstone.ui.upload.CameraActivity
import com.example.mycapstone.ui.upload.UploadActivity
import com.example.mycapstone.ui.upload.UploadActivity.Companion.CAMERA_X_RESULT
import com.example.mycapstone.ui.upload.rotateFile
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingUploadActivity: ActivityUploadBinding
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //kamera jadi


        binding = ActivityMainBinding.inflate(layoutInflater)
        bindingUploadActivity = ActivityUploadBinding.inflate(layoutInflater)
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
                    startCameraX()
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

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("picture")
            } as? File

            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            getFile = myFile

            myFile?.let { file ->
                rotateFile(file, isBackCamera)
                bindingUploadActivity.imgPrev.setImageBitmap(BitmapFactory.decodeFile(file.path))
                val uploadIntent = Intent(this, UploadActivity::class.java)
                uploadIntent.putExtra("file", file)
                startActivity(uploadIntent)
            }
        }
    }
}