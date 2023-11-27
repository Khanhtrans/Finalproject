package com.example.mexpense.fragments.main.home

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mexpense.R
import com.example.mexpense.base.BaseMVVMFragment
import com.example.mexpense.databinding.FragmentHomeBinding
import com.example.mexpense.entity.Transaction
import com.example.mexpense.exts.gone
import com.example.mexpense.fragments.main.balance.TransactionAdapter
import com.example.mexpense.fragments.main.home.month_report.MonthReportFragment
import com.example.mexpense.fragments.main.home.week_report.WeekReportFragment
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : BaseMVVMFragment<FragmentHomeBinding,HomeViewModel>() {
    private lateinit var viewBinding: FragmentHomeBinding
    private var isShow = true
    private val transactionAdapter by lazy {
        TransactionAdapter(
            onClicked = ::onClicked,
        )
    }
    override fun getViewModelClass(): Class<HomeViewModel> {
        return HomeViewModel::class.java
    }

    override fun getLayoutBinding(): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }

    @SuppressLint("ResourceAsColor")
    override fun initialize() {
        getViewModel().setUp(requireContext())
        viewBinding = getViewBinding()

        // set viewpager hiển thị month report và weekreport
        viewBinding.viewpager.adapter = object : FragmentStateAdapter(this) {

            override fun createFragment(position: Int): Fragment {
                return ResourceStore.pagerFragments[position]
            }

            override fun getItemCount(): Int {
                return ResourceStore.tabList.size
            }
        }

        if (getViewModel().trans.size !=0) viewBinding.tvNoTrans.gone()
        transactionAdapter.setItems(getViewModel().trans)
        viewBinding.rvTrans.apply {
            adapter = transactionAdapter
        }
        //renderTabLayer
        TabLayoutMediator(viewBinding.tabLayout, viewBinding.viewpager) { tab, position ->
            tab.text = getString(ResourceStore.tabList[position])
        }.attach()

    }

    override fun registerViewEvent() {
        viewBinding.btnView.setOnClickListener {
            if (isShow) viewBinding.tvTotal.text = getViewModel().formatTotal()
            else viewBinding.tvTotal.text = "********"
            isShow = !isShow
        }

        viewBinding.ivNotification.setOnClickListener {
            requireActivity().startActivity(getViewModel().intentNotify(requireActivity()))
        }

        viewBinding.tvSeeMoreWallet.setOnClickListener {
            requireActivity().startActivity(getViewModel().intentWallet(requireActivity()))
        }
    }

    override fun registerViewModelObs() {
    }

    fun onClicked(transaction: Transaction){

    }
}

interface ResourceStore {
    // set up danh sách fragment trong viewpager
    companion object {
        val tabList = listOf(
            R.string.tab1, R.string.tab2
        )
        val pagerFragments = listOf(
            WeekReportFragment(),
            MonthReportFragment())
    }
}