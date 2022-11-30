package com.globant.carlosmunoz.cameraexample

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private var cameraPhotoFilePath: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.tv_click)
        textView.setOnClickListener {
            callCamera()
        }
    }

    private fun callCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile: File? = try {
            createImageFileDir()
        } catch (e: Exception) {
            Log.e("Main", e.toString())
            null
        }
        photoFile?.also { file ->
            val photoUri: Uri = FileProvider.getUriForFile(
                this,
                "com.globant.carlosmunoz.cameraexample.provider",
                file
            )
            cameraPhotoFilePath = photoUri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        getResultCamera.launch(intent)
    }

    private fun createImageFileDir(): File {
        val timestamp: String = SimpleDateFormat("yyyMMdd_HHmmss").format(Date())
        val imagePath = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(imagePath, "JPEG_${timestamp}_" + ".jpg")
    }

    private val getResultCamera =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                cameraPhotoFilePath?.let { uri ->
                    println(uri.toString())
                }

            }
        }
}