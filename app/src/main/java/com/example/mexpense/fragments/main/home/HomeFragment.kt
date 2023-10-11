package com.example.mexpense.fragments.main.home

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mexpense.R
import com.example.mexpense.base.BaseMVVMFragment
import com.example.mexpense.databinding.FragmentHomeBinding
import com.example.mexpense.fragments.main.home.month_report.MonthReportFragment
import com.example.mexpense.fragments.main.home.week_report.WeekReportFragment
import com.example.mexpense.services.SqlService
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : BaseMVVMFragment<FragmentHomeBinding,HomeViewModel>() {
    private lateinit var viewBinding: FragmentHomeBinding
    private lateinit var sqlService: SqlService
    private var isShow = true
    override fun getViewModelClass(): Class<HomeViewModel> {
        return HomeViewModel::class.java
    }

    override fun getLayoutBinding(): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }

    @SuppressLint("ResourceAsColor")
    override fun initialize() {
        sqlService =  SqlService.getInstance(requireContext())
        viewBinding = getViewBinding()

        viewBinding.viewpager.adapter = object : FragmentStateAdapter(this) {

            override fun createFragment(position: Int): Fragment {
                return ResourceStore.pagerFragments[position]
            }

            override fun getItemCount(): Int {
                return ResourceStore.tabList.size
            }
        }

        //renderTabLayer
        TabLayoutMediator(viewBinding.tabLayout, viewBinding.viewpager) { tab, position ->
            tab.text = getString(ResourceStore.tabList[position])
        }.attach()

    }

    override fun registerViewEvent() {
        viewBinding.btnView.setOnClickListener {
            if (isShow) viewBinding.tvTotal.text = "2.000.000"
            else viewBinding.tvTotal.text = "********"
            isShow = !isShow
        }
        viewBinding.mainActionBtn.setOnClickListener {
            Toast.makeText(requireContext(),"show history",Toast.LENGTH_SHORT).show()
        }
    }

    override fun registerViewModelObs() {
    }


}

interface ResourceStore {
    companion object {
        val tabList = listOf(
            R.string.tab1, R.string.tab2
        )
        val pagerFragments = listOf(
            WeekReportFragment(),
            MonthReportFragment())
    }
}