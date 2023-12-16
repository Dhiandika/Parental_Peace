package com.dicoding.parentalpeaceapp.ui.fragment.other

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dicoding.parentalpeaceapp.R
import com.dicoding.parentalpeaceapp.databinding.FragmentOtherBinding
import com.dicoding.parentalpeaceapp.ui.WelcomeActivity

class OtherFragment : Fragment() {

    private var _binding: FragmentOtherBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOtherBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val logoutButton: Button = root.findViewById(R.id.logout_btn)
        logoutButton.setOnClickListener {
            // Tindakan yang akan dilakukan saat tombol Logout diklik
            // Misalnya, buka aktivitas WelcomeActivity
            val intent = Intent(activity, WelcomeActivity::class.java)
            startActivity(intent)
            activity?.finish()

            // Menampilkan Toast logout berhasil
            Toast.makeText(requireContext(), "Logout Succes", Toast.LENGTH_SHORT).show()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}