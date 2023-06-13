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
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.mycapstone.R
import com.example.mycapstone.databinding.ActivityUploadBinding
import com.example.mycapstone.ui.Login.LoginViewModel
import com.example.mycapstone.ui.hasil.HasilActivity
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.*

class UploadActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUploadBinding
    private var getFile: File? = null
    private lateinit var uploadViewModel: UploadViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        uploadViewModel = ViewModelProvider(this).get(UploadViewModel::class.java)


        hasilCamera()

        binding.inputTanggal.setOnClickListener {
            showDatePicker()
        }


        binding.btnCamera.setOnClickListener {
            startCameraX() }
        binding.btnImport.setOnClickListener {
            startGallery() }
        binding.btnUpload.setOnClickListener {
            postPenyakit()

            println("ini haruse $getFile")
//            val intent = Intent(this, HasilActivity::class.java)
//
//            val data1 = binding.inputNama.text.toString()
//            intent.putExtra("key1", data1)
//
//
//            val data2 = binding.inputTanggal.text.toString()
//            intent.putExtra("key2", data2)
//
//            val data3 = binding.inputDeskripsi.text.toString()
//            intent.putExtra("key3", data3)
//            startActivity(intent)
        }
    }



    private fun postPenyakit(){
        if (getFile != null ){
            val file = reduceImage(getFile as File)
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "uploaded_file",
                file.name,
                requestImageFile
            )
                uploadViewModel.cekpenyakit(imageMultipart)

        } else {
            Toast.makeText(this@UploadActivity, getString(R.string.input_benar), Toast.LENGTH_SHORT).show()
        }
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



    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@UploadActivity)
                getFile = myFile
                binding.imgPrev.setImageURI(uri)
            }
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            val result = CropImage.getActivityResult(data)
//            if (resultCode == RESULT_OK) {
//                val croppedUri = result.uri
//                val myFile = uriToFile(croppedUri, this@UploadActivity)
//                Log.d("ActivityResult", "myFile: $myFile")
//                getFile = myFile
//                binding.imgPrev.setImageURI(croppedUri)
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                val error = result.error
//                Toast.makeText(this,"eror saat memilih gambar",Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    private fun hasilCamera(){
        // Dapatkan Uri gambar hasil crop dari Intent
        val croppedImageUri = intent.getParcelableExtra<Uri>("croppedImageUri")

        val myFile = croppedImageUri?.let { uriToFile(it, this@UploadActivity) }
        Log.d("ActivityResult", "myFile: $myFile")
        getFile = myFile

        // Tampilkan gambar hasil crop di ImageView
        binding.imgPrev.setImageURI(croppedImageUri)
    }


}