package com.dicoding.asclepius.view.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.databinding.ActivityPredictBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.view.viewmodel.PredictViewModel
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File

class PredictActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPredictBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper

    private val viewModel by viewModels<PredictViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPredictBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.currentImage?.let {
            showImage()
        }

        binding.galleryButton.setOnClickListener {
            startGallery()
        }

        binding.previewImageView.setOnClickListener {
            startGallery()
        }

        binding.analyzeButton.setOnClickListener {
            viewModel.currentImage?.let {
                analyzeImage()
                imageClassifierHelper.classifyStaticImage(it)
            } ?: run {
                Toast.makeText(this, "Image cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showImage() {
        viewModel.currentImage?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun analyzeImage() {
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    runOnUiThread {
                        Toast.makeText(this@PredictActivity, error, Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                    runOnUiThread {
                        results?.let { s ->
                            if (s.isNotEmpty() && s[0].categories.isNotEmpty()) {
                                println(s)
                                val result =
                                    s[0].categories.sortedByDescending { it?.score }.first()
                                moveToResult(
                                    label = result.label,
                                    confidence = result.score,
                                    imageUri = viewModel.currentImage!!
                                )
                            } else {
                                Toast.makeText(
                                    this@PredictActivity,
                                    "An unexpected error happened",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }

            }
        )
    }

    private fun moveToResult(label : String, confidence : Float, imageUri : Uri) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_LABEL, label)
        intent.putExtra(ResultActivity.EXTRA_CONFIDENCE, confidence)
        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, imageUri)
        startActivity(intent)
    }

    private fun launchUCrop(sourceUri: Uri) {
        val destinationUri =
            Uri.fromFile(File(cacheDir, "cropped_${System.currentTimeMillis()}.jpg"))
        UCrop.of(sourceUri, destinationUri)
            .start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = UCrop.getOutput(data!!)
            resultUri?.let {
                viewModel.currentImage = it
                showImage()
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            Toast.makeText(this, "Crop error: ${cropError?.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            launchUCrop(uri)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }
}