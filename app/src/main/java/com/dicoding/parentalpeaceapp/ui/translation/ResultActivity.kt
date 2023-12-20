package com.dicoding.parentalpeaceapp.ui.translation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.dicoding.parentalpeaceapp.R

class ResultActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_PREDICTION_TEXT = "extra_prediction_text"
        const val EXTRA_SUGGESTION_1 = "extra_suggestion_1"
        const val EXTRA_SUGGESTION_2 = "extra_suggestion_2"
        const val EXTRA_SUGGESTION_3 = "extra_suggestion_3"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val predictionText = intent.getStringExtra(EXTRA_PREDICTION_TEXT)
        val suggestion1 = intent.getStringExtra(EXTRA_SUGGESTION_1)
        val suggestion2 = intent.getStringExtra(EXTRA_SUGGESTION_2)
        val suggestion3 = intent.getStringExtra(EXTRA_SUGGESTION_3)

        // Tampilkan data di antarmuka pengguna (UI)
        // Misalnya, set text pada TextView
        val predictionTextView: TextView = findViewById(R.id.tv_tittle_result)
        val suggestion1TextView: TextView = findViewById(R.id.tv_result1)
        val suggestion2TextView: TextView = findViewById(R.id.tv_result2)
        val suggestion3TextView: TextView = findViewById(R.id.tv_result3)

        predictionTextView.text = predictionText
        suggestion1TextView.text = suggestion1
        suggestion2TextView.text = suggestion2
        suggestion3TextView.text = suggestion3
    }
}