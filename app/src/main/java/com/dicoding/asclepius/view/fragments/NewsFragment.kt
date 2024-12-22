package com.dicoding.asclepius.view.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.databinding.FragmentNewsBinding
import com.dicoding.asclepius.view.adapters.NewsAdapter
import com.dicoding.asclepius.view.viewmodel.MainViewModel
import com.dicoding.asclepius.view.viewmodel.ViewModelFactory
import com.dicoding.asclepius.helper.Result
import com.dicoding.asclepius.view.activities.PredictActivity
import com.google.android.material.snackbar.Snackbar

class NewsFragment : Fragment() {
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var mainViewModel: MainViewModel

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        newsAdapter = NewsAdapter()

        binding.rvNews.adapter = newsAdapter
        binding.rvNews.layoutManager = LinearLayoutManager(requireContext())

        binding.fab.setOnClickListener {
            val intent = Intent(requireContext(), PredictActivity::class.java)
            startActivity(intent)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        mainViewModel.getNews().observe(viewLifecycleOwner) { result ->
            when(result) {
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    newsAdapter.setNews(result.data)
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(requireView(), result.error, Snackbar.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }
}