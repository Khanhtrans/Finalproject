package com.example.mexpense.fragments.main.budget

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mexpense.R
import com.example.mexpense.base.BaseMVVMFragment
import com.example.mexpense.databinding.FragmentBudgetBinding
import com.example.mexpense.services.SqlService

class BudgetFragment : BaseMVVMFragment<FragmentBudgetBinding,BudgetViewModel>() {
    private lateinit var viewBinding: FragmentBudgetBinding
    private lateinit var sqlService: SqlService
    companion object {
        fun newInstance() = BudgetFragment()
    }

    override fun getViewModelClass(): Class<BudgetViewModel> {
        return BudgetViewModel::class.java
    }

    override fun getLayoutBinding(): FragmentBudgetBinding {
        return FragmentBudgetBinding.inflate(layoutInflater)
    }

    override fun initialize() {
        viewBinding = getViewBinding()
        sqlService = SqlService.getInstance(requireContext())
    }

    override fun registerViewEvent() {

    }

    override fun registerViewModelObs() {

    }


}