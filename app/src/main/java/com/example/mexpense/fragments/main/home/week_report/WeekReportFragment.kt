package com.example.mexpense.fragments.main.home.week_report

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mexpense.R
import com.example.mexpense.base.BaseMVVMFragment
import com.example.mexpense.databinding.FragmentWeekReportBinding

class WeekReportFragment : BaseMVVMFragment<FragmentWeekReportBinding,WeekReportViewModel>() {
    lateinit var viewBinding: FragmentWeekReportBinding
    companion object {
        fun newInstance() = WeekReportFragment()
    }

    override fun getViewModelClass(): Class<WeekReportViewModel> {
        return WeekReportViewModel::class.java
    }

    override fun getLayoutBinding(): FragmentWeekReportBinding {
        return FragmentWeekReportBinding.inflate(layoutInflater)
    }

    override fun initialize() {
        viewBinding = getViewBinding()
    }

    override fun registerViewEvent() {
    }

    override fun registerViewModelObs() {
    }


}