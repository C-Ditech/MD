package com.example.mycapstone.ui.upload

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mycapstone.R
import com.example.mycapstone.databinding.ActivityUploadBinding
import com.example.mycapstone.ui.hasil.HasilActivity
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.File
import java.util.*

class UploadActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUploadBinding
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayShowHomeEnabled(true)


        hasilCamera()
        setupInput()

        binding.inputTanggal.setOnClickListener {
            showDatePicker()
        }


        binding.btnCamera.setOnClickListener {
            startCameraX() }
        binding.btnImport.setOnClickListener {
            startGallery() }
        binding.btnUpload.setOnClickListener {
            val intent = Intent(this, HasilActivity::class.java)

            val data1 = binding.inputNama.text.toString()
            intent.putExtra("key1", data1)


            val data2 = binding.inputTanggal.text.toString()
            intent.putExtra("key2", data2)

            val data3 = binding.inputDeskripsi.text.toString()
            intent.putExtra("key3", data3)
            startActivity(intent)
        }
    }

    private fun setupInput(){val loginTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            val isNameEmpty = binding.inputNama.text.toString().trim().isEmpty()
            binding.btnUpload.isEnabled = !isNameEmpty
        }

        override fun afterTextChanged(s: Editable?) {

        }
    }
        binding.inputNama.addTextChangedListener(loginTextWatcher)

    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.inputTanggal.setText(selectedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        const val REQUEST_CODE_PERMISSIONS = 10
    }



    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        startActivity(intent)
//        launcherIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        // Periksa izin akses ke penyimpanan
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Jika izin belum diberikan, minta izin kepada pengguna
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE_PERMISSIONS
            )
        } else {
            // Jika izin sudah diberikan, lanjutkan untuk membuka galeri
            openGallery()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Izin diberikan, lanjutkan untuk membuka galeri
                openGallery()
            } else {
                // Izin ditolak, berikan tanggapan atau tindakan sesuai kebutuhan Anda
                Toast.makeText(this, getString(R.string.deny), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun openGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }


    private fun launchImageCrop(uri: Uri){
        CropImage.activity(uri)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setCropShape(CropImageView.CropShape.RECTANGLE) // default is rectangle
            .start(this)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                launchImageCrop(uri)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val croppedUri = result.uri
                val myFile = uriToFile(croppedUri, this@UploadActivity)
                getFile = myFile
                binding.imgPrev.setImageURI(croppedUri)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Toast.makeText(this,"eror saat memilih gambar",Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun hasilCamera(){
        // Dapatkan Uri gambar hasil crop dari Intent
        val croppedImageUri = intent.getParcelableExtra<Uri>("croppedImageUri")

        // Tampilkan gambar hasil crop di ImageView
        binding.imgPrev.setImageURI(croppedImageUri)
    }


}