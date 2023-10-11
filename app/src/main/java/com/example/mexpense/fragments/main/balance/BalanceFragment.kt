package com.example.mexpense.fragments.main.balance

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mexpense.R
import com.example.mexpense.base.BaseMVVMFragment
import com.example.mexpense.databinding.FragmentBalanceBinding
import com.example.mexpense.services.SqlService

class BalanceFragment : BaseMVVMFragment<FragmentBalanceBinding,BalanceViewModel>() {
    private lateinit var viewBinding: FragmentBalanceBinding
    private lateinit var sqlService: SqlService
    companion object {
        fun newInstance() = BalanceFragment()
    }

    override fun getViewModelClass(): Class<BalanceViewModel> {
        return BalanceViewModel::class.java
    }

    override fun getLayoutBinding(): FragmentBalanceBinding {
        return FragmentBalanceBinding.inflate(layoutInflater)
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