package com.dicoding.parentalpeaceapp.ui.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dicoding.parentalpeaceapp.databinding.FragmentNotificationsBinding
import com.dicoding.parentalpeaceapp.ui.ui.ViewModelFactory

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: NotificationsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Menggunakan ViewModel Factory
        val factory = context?.let { ViewModelFactory(it) }
        viewModel = factory?.let {
            ViewModelProvider(this,
                it
            ).get(NotificationsViewModel::class.java)
        }!!

        // Mendapatkan nilai string dari NotificationsViewModel
        viewModel.text.observe(viewLifecycleOwner, Observer {
            binding.textNotifications.text = it
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}