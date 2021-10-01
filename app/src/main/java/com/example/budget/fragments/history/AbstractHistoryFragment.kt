package com.example.budget.fragments.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.budget.databinding.FragmentHistoryBinding

abstract class AbstractHistoryFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        super.onCreate(savedInstanceState)
        val binding = FragmentHistoryBinding.inflate(inflater, container, false)
        findNavController()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = adapterSettings()
        binding.recyclerView.adapter = adapter

        return binding.root
    }

    abstract fun adapterSettings(): RecyclerView.Adapter<RecyclerView.ViewHolder>
}