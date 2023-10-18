package com.example.mexpense.fragments.main.balance

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mexpense.R
import com.example.mexpense.base.BaseMVVMFragment
import com.example.mexpense.base.SharePreUtil
import com.example.mexpense.databinding.FragmentBalanceBinding
import com.example.mexpense.entity.Transaction
import com.example.mexpense.exts.gone
import com.example.mexpense.services.SqlService
import com.example.mexpense.ultilities.Constants

class BalanceFragment : BaseMVVMFragment<FragmentBalanceBinding,BalanceViewModel>() {
    private lateinit var viewBinding: FragmentBalanceBinding
    private lateinit var sqlService: SqlService
    private val transactionAdapter by lazy {
        TransactionAdapter(
            onClicked = ::onClicked,
        )
    }

    private fun onClicked(transaction: Transaction) {

    }

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

        val myId = SharePreUtil.GetShareInt(requireContext(), Constants.KEY_USER_ID);
        val trans = sqlService.getMyTransaction(myId)
        if (trans.size !=0) viewBinding.tvNoTrans.gone()
        transactionAdapter.setItems(trans)
        viewBinding.rvTrans.apply {
            adapter = transactionAdapter
        }
    }

    override fun registerViewEvent() {

    }

    override fun registerViewModelObs() {

    }
}