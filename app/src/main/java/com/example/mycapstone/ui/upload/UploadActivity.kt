package com.example.mycapstone.ui.upload

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
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
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
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
            if (binding.imgPrev.drawable !=null){
                postPenyakit()
            }else{
                Toast.makeText(this,"Masukkan gambar",Toast.LENGTH_SHORT).show()
            }

        }


        uploadViewModel.isLoading.observe(this) {
            isLoading(it)
        }

        uploadViewModel.isError.observe(this) { isError ->
            chekDatavalid(isError)
        }
    }


    private fun chekDatavalid( isError: Boolean) {
        if (!isError) {
            Toast.makeText(this,getString(R.string.uploadscs),Toast.LENGTH_SHORT).show()

            val intent = Intent(this, HasilActivity::class.java)
            val drawable = binding.imgPrev.drawable
            val originalBitmap = (drawable as BitmapDrawable).bitmap

            val file = saveBitmapToFile(originalBitmap)
            val reducedFile = reduceImage(file) // Set the desired maxParcelSize value
            intent.putExtra("img", reducedFile)

            val data1 = binding.inputNama.text.toString()
            intent.putExtra("key1", data1)


            val data2 = binding.inputTanggal.text.toString()
            intent.putExtra("key2", data2)

            val data3 = binding.inputDeskripsi.text.toString()
            intent.putExtra("key3", data3)
            startActivity(intent)


            uploadViewModel.disease.observe(this) { datapenyakit ->
                intent.putExtra("EXTRA_DISEASE", datapenyakit)
                println("cek apakah penyakit : $datapenyakit")
                startActivity(intent)
            }

            uploadViewModel.accuracy.observe(this) { dataakurasi ->
                intent.putExtra("EXTRA_ACCURACY", dataakurasi)
                println("cek apakah akuirasi : $dataakurasi")
                startActivity(intent)
            }


        } else {
            Toast.makeText(this, "Error response from the server", Toast.LENGTH_SHORT).show()
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

    private fun saveBitmapToFile(bitmap: Bitmap): File {
        val file = File(applicationContext.filesDir, "temp_image.jpg")
        file.createNewFile()
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        return file
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



    private fun hasilCamera(){
        // Dapatkan Uri gambar hasil crop dari Intent
        val croppedImageUri = intent.getParcelableExtra<Uri>("croppedImageUri")

        val myFile = croppedImageUri?.let { uriToFile(it, this@UploadActivity) }
        Log.d("ActivityResult", "myFile: $myFile")
        getFile = myFile

        // Tampilkan gambar hasil crop di ImageView
        binding.imgPrev.setImageURI(croppedImageUri)
    }

    private fun isLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }


}