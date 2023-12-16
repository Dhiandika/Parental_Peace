package com.dicoding.parentalpeaceapp.ui.fragment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.dicoding.parentalpeaceapp.R
import com.dicoding.parentalpeaceapp.ui.FavoriteActivity

class ConsultationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consultation)

        val favoriteButton: ImageButton = findViewById(R.id.favorite_btn)
        favoriteButton.setOnClickListener {
            // Tindakan yang akan dilakukan saat tombol Favorite diklik
            // Misalnya, buka aktivitas FavoriteActivity
            val intent = Intent(this, FavoriteActivity::class.java)
            startActivity(intent)
        }

    }
}