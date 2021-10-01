package com.example.budget.fragments.tabs

import android.content.res.TypedArray
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.budget.R
import com.example.budget.databinding.FragmentTabsBinding
import com.example.budget.fragments.expense.ExpenseFragment
import com.example.budget.fragments.expense.IncomeFragment
import com.example.budget.fragments.expense.LocalExchangeFragment
import com.example.budget.fragments.expense.PlannedExpenseFragment
import com.example.budget.fragments.history.ExpenseHistoryFragment
import com.example.budget.fragments.history.LocalExchangeHistoryFragment
import com.google.android.material.tabs.TabLayout.MODE_FIXED
import com.google.android.material.tabs.TabLayoutMediator

abstract class AbstractTabsFragment : Fragment() {

    protected abstract val tabsFragments: List<Class<out Fragment>>
    protected abstract val textRes: List<String>
    protected abstract val drawableRes: TypedArray

    protected open fun FragmentTabsBinding.tabsSettings() {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreate(savedInstanceState)
        val binding = FragmentTabsBinding.inflate(inflater, container, false)
        findNavController()
        with(binding) {
            viewPager.adapter = FragmentAdapter(tabsFragments, requireActivity())
            tabsSettings()
            tabLayoutMediator()
        }

        return binding.root
    }

    override fun onDestroy() {
        drawableRes.recycle()
        super.onDestroy()

    }

    private fun FragmentTabsBinding.tabLayoutMediator() {
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = textRes[position]
            tab.icon = drawableRes.getDrawable(position)
        }.attach()
    }
}

class ExpenseTabsFragment : AbstractTabsFragment() {

    override val tabsFragments: List<Class<out Fragment>> = listOf(
        ExpenseFragment::class.java,
        IncomeFragment::class.java,
        LocalExchangeFragment::class.java,
        PlannedExpenseFragment::class.java
    )
    override val textRes: List<String> by lazy {
        resources.getStringArray(R.array.expense_tabs_title).asList()
    }
    override val drawableRes: TypedArray by lazy {
        resources.obtainTypedArray(R.array.expense_tabs_drawable)
    }

    override fun FragmentTabsBinding.tabsSettings() {}
}

class HistoryTabsFragment : AbstractTabsFragment() {
    override val tabsFragments: List<Class<out Fragment>> = listOf(
        ExpenseHistoryFragment::class.java,
        LocalExchangeHistoryFragment::class.java,
    )

    override val textRes: List<String> by lazy {
        resources.getStringArray(R.array.history_tabs_title).asList()
    }

    override val drawableRes: TypedArray by lazy {
        resources.obtainTypedArray(R.array.history_tabs_drawable)
    }

    override fun FragmentTabsBinding.tabsSettings() {
        tabs.tabMode = MODE_FIXED
    }
}
