package com.dicoding.parentalpeaceapp.ui.main.bottomnavigation.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.parentalpeaceapp.R
import com.dicoding.parentalpeaceapp.adapter.ArticlesListAdapter
import com.dicoding.parentalpeaceapp.databinding.FragmentHomeBinding
import com.dicoding.parentalpeaceapp.response.DataItem
import com.dicoding.parentalpeaceapp.ui.ViewModelFactory
import com.dicoding.parentalpeaceapp.ui.consultation.ConsultationActivity
import com.dicoding.parentalpeaceapp.ui.information.InformationActivity
import com.dicoding.parentalpeaceapp.ui.main.bottomnavigation.TransactionHistoryActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: HomeViewModel by viewModels {
            factory
        }

        showRv()

        viewModel.listArticle().observe(viewLifecycleOwner) { articles ->
            if (articles != null) {
                setArticleList(articles)
            } else {
                showToast(getString(R.string.no_notification))
            }
        }

        binding.imbConsultation.setOnClickListener {
            val intent = Intent(activity, ConsultationActivity::class.java)
            startActivity(intent)
        }

        binding.imbTransaction.setOnClickListener {
            val intent = Intent(activity, TransactionHistoryActivity::class.java)
            startActivity(intent)
        }

        binding.imbInformation.setOnClickListener {
            val intent = Intent(activity, InformationActivity::class.java)
            startActivity(intent)
        }

    }

    private fun showRv() {
        val layoutManager = LinearLayoutManager(context)
        binding.rvArticlesHome.layoutManager = layoutManager
    }

    private fun setArticleList(articles: List<DataItem>) {
        val adapter = ArticlesListAdapter()
        adapter.submitList(articles)
        binding.rvArticlesHome.adapter = adapter
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}