package com.dicoding.parentalpeaceapp.ui.main.bottomnavigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.widget.PopupMenu
import com.dicoding.parentalpeaceapp.R

class TransactionHistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_history)

        val imageButton: ImageButton = findViewById(R.id.filter_menu)

        imageButton.setOnClickListener { view ->
            showPopupMenu(view)
        }
    }
    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.transaction_filter_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.transaction_filter1 -> {
                    // Tindakan saat Menu Item 1 dipilih
                    true
                }
                R.id.transaction_filter2 -> {
                    // Tindakan saat Menu Item 2 dipilih
                    true
                }
                R.id.transaction_filter3 -> {
                    // Tindakan saat Menu Item 2 dipilih
                    true
                }
                R.id.transaction_filter4 -> {
                    // Tindakan saat Menu Item 2 dipilih
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }
}