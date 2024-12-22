package com.dicoding.asclepius.view.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.entity.Bookmarks
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.view.viewmodel.MainViewModel
import com.dicoding.asclepius.view.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch
import java.text.NumberFormat

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(application)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val label = intent.getStringExtra(EXTRA_LABEL)
        val confidenceValue = intent.getFloatExtra(EXTRA_CONFIDENCE, 0.0f)
        val confidence = NumberFormat.getPercentInstance().format(confidenceValue)
        val imageUri = intent.getParcelableExtra<Uri>(EXTRA_IMAGE_URI)

        binding.resultText.text = "$label ($confidence)"
        binding.resultImage.setImageURI(imageUri)
        binding.closeButton.setOnClickListener {
            saveResultAndFinish(label, confidenceValue, imageUri)
            val intent = Intent(this@ResultActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val label = intent.getStringExtra(EXTRA_LABEL)
        val confidenceValue = intent.getFloatExtra(EXTRA_CONFIDENCE, 0.0f)
        val imageUri = intent.getParcelableExtra<Uri>(EXTRA_IMAGE_URI)

        saveResultAndFinish(label, confidenceValue, imageUri)
        super.onBackPressed()
    }

    private fun saveResultAndFinish(label: String?, confidence: Float, imageUri: Uri?) {
        label?.let {
            val bookmark = Bookmarks(label = it, confidence = confidence, image = imageUri.toString())
            lifecycleScope.launch {
                viewModel.setBookmark(bookmark)
                finish()
            }
        } ?: run {
            finish()
        }
    }

    companion object {
        const val EXTRA_LABEL = "label"
        const val EXTRA_CONFIDENCE = "confidence"
        const val EXTRA_IMAGE_URI = "uri"
    }
}
