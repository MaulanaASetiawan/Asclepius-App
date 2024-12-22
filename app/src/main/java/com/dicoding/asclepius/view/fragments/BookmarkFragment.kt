package com.dicoding.asclepius.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.asclepius.databinding.FragmentHistoryBinding
import com.dicoding.asclepius.view.adapters.PredictAdapter
import com.dicoding.asclepius.view.viewmodel.MainViewModel
import com.dicoding.asclepius.view.viewmodel.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

class BookmarkFragment : Fragment() {
    private lateinit var predictAdapter: PredictAdapter
    private lateinit var mainViewModel: MainViewModel

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireActivity())
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        predictAdapter = PredictAdapter()

        binding.rvHistory.adapter = predictAdapter
        binding.rvHistory.layoutManager = GridLayoutManager(requireContext(), 2)

        observeBookmarks()
    }

    private fun observeBookmarks() {
        mainViewModel.getBookmark().observe(viewLifecycleOwner) { bookmarks ->
            if(bookmarks.isEmpty()){
                binding.progressBar.visibility = View.VISIBLE
                Snackbar.make(binding.root, "No data available", Snackbar.LENGTH_SHORT).show()
            } else {
                predictAdapter.submitList(bookmarks)
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}