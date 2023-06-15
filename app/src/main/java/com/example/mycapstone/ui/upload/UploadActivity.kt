package com.example.mycapstone.ui.upload

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.mycapstone.R
import com.example.mycapstone.databinding.ActivityUploadBinding
import com.example.mycapstone.ui.hasil.HasilActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

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
            val reducedFile = reduceImage(file)
            intent.putExtra("img", reducedFile)

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
            Toast.makeText(this, "Server Eror,Silahkan Coba lagi", Toast.LENGTH_SHORT).show()
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
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE_PERMISSIONS
            )
        } else {
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
                openGallery()
            } else {
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
        val croppedImageUri = intent.getParcelableExtra<Uri>("croppedImageUri")

        val myFile = croppedImageUri?.let { uriToFile(it, this@UploadActivity) }
        Log.d("ActivityResult", "myFile: $myFile")
        getFile = myFile

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