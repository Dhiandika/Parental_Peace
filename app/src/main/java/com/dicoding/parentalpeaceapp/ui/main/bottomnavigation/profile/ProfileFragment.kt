package com.dicoding.parentalpeaceapp.ui.main.bottomnavigation.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dicoding.parentalpeaceapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        
        return root
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_EMAIL = "extra_email"
        const val EXTRA_PHONE = "extra_phone"

        fun newInstance(username: String?, email: String?, phone: String?): ProfileFragment {
            val fragment = ProfileFragment()
            val args = Bundle().apply {
                putString(EXTRA_USERNAME, username)
                putString(EXTRA_EMAIL, email)
                putString(EXTRA_PHONE, phone)
            }
            fragment.arguments = args
            return fragment
        }
    }
}