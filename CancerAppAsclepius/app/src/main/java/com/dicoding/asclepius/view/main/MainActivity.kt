package com.dicoding.asclepius.view.main

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.view.article.ArticleActivity
import com.dicoding.asclepius.view.result.ResultActivity
import com.dicoding.asclepius.view.save.SaveActivity
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.NumberFormat


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var imageClassHelper: ImageClassifierHelper
    private var result:String? = null
    private var prediction:String? = null
    private var score:String? = null
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this, ResultActivity::class.java)

        binding.galleryBtn.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener {
            analyzeImage(intent)
            moveToResult(intent)
        }
        binding.btnSave.setOnClickListener {
            val intentSave = Intent(this, SaveActivity::class.java)
            startActivity(intentSave)
        }
        binding.btnArticle.setOnClickListener {
            val intentArticle = Intent(this, ArticleActivity::class.java)
            startActivity(intentArticle)
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "Tidak Ada Foto Yang Dipilih")
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showImage() {
        currentImageUri?.let {
            cropImage(it)
            Log.d("Image URI", "Menampilkan Gambar: $it")
        }
    }

    private fun analyzeImage(intent: Intent) {
        imageClassHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                        results?.let { it ->
                            if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                                println(it)
                                val sortedCategories =
                                    it[0].categories.sortedByDescending { it?.score }
                                result  =
                                    sortedCategories.joinToString("\n") {
                                        "${it.label} " + NumberFormat.getPercentInstance()
                                            .format(it.score).trim()
                                    }
                                prediction = sortedCategories[0].label
                                score = NumberFormat.getPercentInstance().format(sortedCategories[0].score)
                            } else {
                                showToast()
                            }
                        }
                    }
            }
        )
        currentImageUri?.let { this.imageClassHelper.classifyStaticImage(it) }
        intent.putExtra(ResultActivity.EXTRA_RESULT, result)
        intent.putExtra(ResultActivity.EXTRA_PREDICTION,prediction)
        intent.putExtra(ResultActivity.EXTRA_SCORE, score)
    }

    private fun moveToResult(intent: Intent) {
        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, currentImageUri.toString())
        startActivity(intent)
    }

    private fun showToast(message: String = "Tidak Ada Hasil Ditemukan") {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private var cropImage = registerForActivityResult(CropImageContract()
    ) { result: CropImageView.CropResult ->
        if (result.isSuccessful) {
            val crop =
                BitmapFactory.decodeFile(result.getUriFilePath(applicationContext, true))
            binding.previewImageView.setImageBitmap(crop)
            currentImageUri = result.uriContent
        }
    }

    private fun cropImage(uri:Uri) {
        cropImage.launch(
            CropImageContractOptions(uri = uri, cropImageOptions = CropImageOptions(
                guidelines = CropImageView.Guidelines.ON
            )
            )
        )
    }
}