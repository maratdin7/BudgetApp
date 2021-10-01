package com.example.budget.fragments.menu

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.budget.databinding.FragmentCashAccountBinding
import com.example.budget.repository.view.DialogBuilder


abstract class AbstractMenuFragment : Fragment() {

    protected fun addRecyclerView(binding: FragmentCashAccountBinding) {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = adapterSettings()
        binding.recyclerView.adapter = adapter
    }


    protected abstract fun DialogBuilder.createDialog()

    abstract fun adapterSettings(): RecyclerView.Adapter<RecyclerView.ViewHolder>

}