package com.tobyz.mlkittest

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_proses.*
import android.graphics.BitmapFactory
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import android.widget.Toast
import com.google.firebase.ml.vision.FirebaseVision
import kotlinx.android.synthetic.main.activity_crop_image.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import android.text.method.TextKeyListener.clear
import java.io.FileNotFoundException
import java.io.FileOutputStream


class ProsesActivity : AppCompatActivity() {


    private var myClipboard: ClipboardManager? = null
    private var myClip: ClipData? = null
    private val FILE_NAME = "example.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proses)

        //Action bar Back
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        //Detect Gambar dari Crop Activity
        detect()
        myClipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?

        btnCopy.setOnClickListener {
            myClip = ClipData.newPlainText("text", txtResult.text);
            myClipboard?.setPrimaryClip(myClip);

            Toast.makeText(this, "Text Copied", Toast.LENGTH_SHORT).show();
        }

        btnSave.setOnClickListener {
            simpan()
        }

    }

    fun simpan(){
        val file:String = txtResult.text.toString()
        val data:String = txtResult.text.toString()
        val fileOutputStream:FileOutputStream
        try {
            fileOutputStream = openFileOutput(file, Context.MODE_PRIVATE)
            fileOutputStream.write(data.toByteArray())
        } catch (e: FileNotFoundException){
            e.printStackTrace()
        }catch (e: NumberFormatException){
            e.printStackTrace()
        }catch (e: IOException){
            e.printStackTrace()
        }catch (e: Exception){
            e.printStackTrace()
        }
        Toast.makeText(applicationContext,"Data di Simpan",Toast.LENGTH_LONG).show()

    }

    fun detect() {
        val bundle = getIntent().extras
        val image_path = intent.data
        val image: FirebaseVisionImage
        if (bundle != null) {
            val b = BitmapFactory.decodeByteArray(intent.getByteArrayExtra("images"), 0, intent.getByteArrayExtra("images").size)
            imgCapture.setImageBitmap(b)
            image = FirebaseVisionImage.fromBitmap(b)
            recognizeText(image)
        }
        else if(image_path!=null){
            try {
                imgCapture.setImageURI(image_path)
                image = FirebaseVisionImage.fromFilePath(this, image_path)
                recognizeText(image)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        else {
            Toast.makeText(applicationContext, "Gambar Kosong!!!", Toast.LENGTH_LONG).show()
        }
    }

    private fun recognizeText(image: FirebaseVisionImage) {
        val detector = FirebaseVision.getInstance()
            .onDeviceTextRecognizer
            detector.processImage(image)
            .addOnSuccessListener { firebaseVisionText ->
                txtResult.setText(firebaseVisionText.text)
            }
            .addOnFailureListener {e ->
                e.printStackTrace()
            }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

