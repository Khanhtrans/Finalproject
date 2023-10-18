package com.example.mexpense.fragments.main.budget

import com.example.mexpense.base.BaseMVVMFragment
import com.example.mexpense.base.SharePreUtil
import com.example.mexpense.databinding.FragmentBudgetBinding
import com.example.mexpense.entity.Category
import com.example.mexpense.repository.transCategory
import com.example.mexpense.services.SqlService
import com.example.mexpense.ultilities.Constants

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

        val cate = getSpendingRecorded(transCategory)
        val adapter = BudgetAdapter(cate,requireContext())
        viewBinding.rvTrans.apply {
            this.adapter = adapter
        }
    }

    private fun getSpendingRecorded(initCate: List<Category>): ArrayList<Category> {
        val myId = SharePreUtil.GetShareInt(requireContext(), Constants.KEY_USER_ID);
        val transList = sqlService.getMyTransaction(myId)
        val afterCate = arrayListOf<Category>()
        afterCate.addAll(initCate)
        for (trans in transList) {
            for (cate in afterCate) {
                if (trans.category == cate.name) cate.spend += trans.amount
            }
        }
        return afterCate
    }
    override fun registerViewEvent() {

    }

    override fun registerViewModelObs() {

    }


}