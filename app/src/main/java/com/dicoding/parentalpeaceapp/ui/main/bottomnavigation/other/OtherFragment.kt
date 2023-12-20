package com.dicoding.parentalpeaceapp.ui.main.bottomnavigation.other

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dicoding.parentalpeaceapp.R
import com.dicoding.parentalpeaceapp.databinding.FragmentOtherBinding
import com.dicoding.parentalpeaceapp.ui.ViewModelFactory
import com.dicoding.parentalpeaceapp.ui.signin.SignInViewModel
import com.dicoding.parentalpeaceapp.ui.welcome.WelcomeActivity

class OtherFragment : Fragment() {

    private var _binding: FragmentOtherBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<OtherViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOtherBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val logoutButton: Button = root.findViewById(R.id.logout_btn)
        logoutButton.setOnClickListener {
            AlertDialog.Builder(requireContext()).apply {
                setTitle(getString(R.string.logout))
                setMessage(getString(R.string.logout_reminder))
                setPositiveButton(getString(R.string.logout)) { _, _ ->
                    viewModel.logout()
                    val intent = Intent(requireContext(), WelcomeActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
                setNegativeButton(getString(R.string.cancel)) { _, _ ->
                    // Do Nothing
                }
                create()
                show()
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}