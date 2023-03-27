package com.fadhil.myfadhilstoryapp.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.preference.PreferenceManager
import com.fadhil.myfadhilstoryapp.R
import com.fadhil.myfadhilstoryapp.StoryViewModel
import com.fadhil.myfadhilstoryapp.ViewModelFactory
import com.fadhil.myfadhilstoryapp.data.CustomResult
import com.fadhil.myfadhilstoryapp.databinding.ActivityAddBinding
import com.fadhil.myfadhilstoryapp.tools.createCustomTempFile
import com.fadhil.myfadhilstoryapp.tools.reduceFileImage
import com.fadhil.myfadhilstoryapp.tools.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddActivity : AppCompatActivity() {
    private lateinit var currentPhotoPath: String
    private lateinit var binding : ActivityAddBinding
    private var getFile: File? = null
    private val viewModel by viewModels<StoryViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.toolbarDetail.setNavigationOnClickListener {
            finish()
        }

        binding.btPickCamera.setOnClickListener {
            startTakePhoto()
        }

        binding.btPickGallery.setOnClickListener {
            startGallery()
        }



        binding.btUpload.setOnClickListener {
            viewModel.description =  binding.edDesc.text.toString()
            if (viewModel.description!!.isNotEmpty()){
                uploadImage(viewModel.description!!)
            }else{
                Toast.makeText(this, "Isi Deskripsi Terlebih Dahulu", Toast.LENGTH_SHORT).show()
            }
        }


    }



    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile

            val result =  BitmapFactory.decodeFile(myFile.path)
            binding.ivAddPhoto.setImageBitmap(result)
        }
    }


    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this )
            getFile = myFile
            binding.ivAddPhoto.setImageURI(selectedImg)
        }
    }


    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this ,
                "com.fadhil.myfadhilstoryapp",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }
    private fun uploadImage(desc: String) {
        if (getFile != null) {
            val pref = PreferenceManager.getDefaultSharedPreferences(this@AddActivity)
            val token = pref.getString(getString(R.string.token), "")
            val file = reduceFileImage(getFile as File)

            val description = desc.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
            viewModel.uploadStory(

                "Bearer $token",
                imageMultipart,
                description
                 ).observe(this) { result ->
                     when(result){
                         is CustomResult.Loading -> {

                             binding.progress.visibility = View.VISIBLE

                         }
                         is CustomResult.Error -> {
                             binding.progress.visibility = View.GONE
                             Toast.makeText(this@AddActivity, "Gagal mengupload", Toast.LENGTH_SHORT).show()

                         }

                         is CustomResult.Success -> {
                             binding.progress.visibility = View.GONE
                             startActivity(Intent(this@AddActivity, MainActivity::class.java))
                             finishAffinity()

                         }
                     }
                 }






        } else {
            Toast.makeText(this , "Silakan masukkan berkas gambar terlebih dahulu.", Toast.LENGTH_SHORT).show()
        }
    }



    companion object {

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}