package com.tobyz.mlkittest

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import android.content.ContentValues


class MainActivity : AppCompatActivity() {

    companion object {
        val REQUEST_IMAGE_CAPTURE = 1
        val IMAGE_PICK_CODE = 2
        private val PERMISSION_CODE = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Tombol Camera
        ibtn_camera.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED) {
                    val permission = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permission, PERMISSION_CODE)
                } else {
                    dispatchTakePictureIntent()
                }
            } else {
                dispatchTakePictureIntent()
            }
        }

        // Tombol Ambil citra dari Gallery
        ibtn_image.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_DENIED) {
                    val permission = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permission, PERMISSION_CODE)
                } else {
                    pickImageFromGallery()
                }
            } else {
                pickImageFromGallery()
            }
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                takePictureIntent.putExtra(MediaStore.Images.Media.TITLE, "New Picture")
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap =data?.extras?.get("data") as Bitmap
            sendBitmap(imageBitmap)
        }
        else if(resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE ){
                val dataIamge =data?.data
                sendUri(dataIamge)
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    fun sendUri(uri: Uri?){
        val i = Intent(this, CropImageActivity::class.java)
        i.setData(uri)
        startActivity(i)
    }

    fun sendBitmap(bitmap: Bitmap){
        val i = Intent(this, CropImageActivity::class.java)
        val bs = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bs)
        i.putExtra("image", bs.toByteArray())
        startActivity(i)
    }

}
