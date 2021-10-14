package com.example.budget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.budget.databinding.FragmentMainBinding
import com.example.budget.repository.PersistentRepository
import com.example.budget.viewModel.AuthViewModel
import com.example.budget.viewModel.Event
import com.example.budget.viewModel.ViewModelProviderFactory

class MainFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)

        val binding = FragmentMainBinding.inflate(inflater, container, false)

        val navController = findNavController()

        val viewModel = ViewModelProvider(this, ViewModelProviderFactory).get(AuthViewModel::class.java)

        val refreshToken = viewModel.loadRefreshToken(requireContext())
        val email = PersistentRepository.loadJsonObject<String>(requireContext(), getString(R.string.user_email))

        viewModel.generateAccessToken(requireContext(), email, refreshToken) {
            when (it) {
                is Event.Success -> navController.navigate(R.id.action_mainFragment_to_expenseTabsFragment)
                is Event.Error -> navController.navigate(R.id.action_mainFragment_to_loginFragment)
                Event.Loading -> Unit
            }
        }
        return binding.root
    }

}