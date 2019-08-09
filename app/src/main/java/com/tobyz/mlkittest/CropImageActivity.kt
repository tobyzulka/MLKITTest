package com.tobyz.mlkittest


import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_crop_image.*
import java.io.IOException
import android.graphics.Bitmap
import java.io.ByteArrayOutputStream


class CropImageActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop_image)

        //Action bar Back
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        detect()

        btnCrop.setOnClickListener {
            cropImageView.setOnCropImageCompleteListener { view, result -> }
            cropImageView.getCroppedImageAsync()
            val cropped = cropImageView.croppedImage
            sendBitmap(cropped)
        }
    }

    fun detect() {
        val bundle = getIntent().extras
        val image_path = intent.data
        if (bundle != null) {
            val b = BitmapFactory.decodeByteArray(
                intent.getByteArrayExtra("image"),
                0,
                intent.getByteArrayExtra("image").size
            )
            cropImageView.setImageBitmap(b)
        }
        else if(image_path!=null){
            try {
                cropImageView.setImageUriAsync(image_path)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        else {
            Toast.makeText(applicationContext, "Gambar Kosong!!!", Toast.LENGTH_LONG).show()
        }
    }

    fun sendBitmap(bitmap: Bitmap){
        val i = Intent(this, ProsesActivity::class.java)
        val bs = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bs)
        i.putExtra("image", bs.toByteArray())
        startActivity(i)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
