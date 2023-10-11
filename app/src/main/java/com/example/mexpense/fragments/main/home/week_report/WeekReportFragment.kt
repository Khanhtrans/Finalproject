package com.example.mexpense.fragments.main.home.week_report

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mexpense.R
import com.example.mexpense.base.BaseMVVMFragment
import com.example.mexpense.databinding.FragmentWeekReportBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class WeekReportFragment : BaseMVVMFragment<FragmentWeekReportBinding,WeekReportViewModel>() {
    lateinit var viewBinding: FragmentWeekReportBinding

    // on below line we are creating
    // a variable for bar data
    lateinit var barData: BarData

    // on below line we are creating a
    // variable for bar data set
    lateinit var barDataSet: BarDataSet

    // on below line we are creating array list for bar data
    lateinit var barEntriesList: ArrayList<BarEntry>
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
        getBarChartData()

        // on below line we are initializing our bar data set
        barDataSet = BarDataSet(barEntriesList, "Bar Chart Data")

        // on below line we are initializing our bar data
        barData = BarData(barDataSet)

        // on below line we are setting data to our bar chart
            viewBinding.idBarChart.data = barData

        // on below line we are setting colors for our bar chart text
        barDataSet.valueTextColor = Color.BLACK

        // on below line we are setting color for our bar data set
        barDataSet.color = resources.getColor(R.color.purple_200)

        // on below line we are setting text size
        barDataSet.valueTextSize = 16f

        // on below line we are enabling description as false
        viewBinding.idBarChart.description.isEnabled = false
    }

    override fun registerViewEvent() {
    }

    override fun registerViewModelObs() {
    }

    private fun getBarChartData() {
        barEntriesList = ArrayList()

        // on below line we are adding data
        // to our bar entries list
        barEntriesList.add(BarEntry(1f, 1f))
        barEntriesList.add(BarEntry(2f, 2f))

    }
}