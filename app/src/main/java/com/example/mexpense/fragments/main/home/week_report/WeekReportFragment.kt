package com.example.mexpense.fragments.main.home.week_report

import android.graphics.Color
import com.example.mexpense.R
import com.example.mexpense.base.BaseMVVMFragment
import com.example.mexpense.base.SharePreUtil
import com.example.mexpense.databinding.FragmentWeekReportBinding
import com.example.mexpense.services.SqlService
import com.example.mexpense.ultilities.Constants
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.slider.LabelFormatter
import java.text.SimpleDateFormat
import java.util.*


class WeekReportFragment : BaseMVVMFragment<FragmentWeekReportBinding,WeekReportViewModel>() {
    lateinit var viewBinding: FragmentWeekReportBinding

    lateinit var barData: BarData
    lateinit var barDataSet: BarDataSet
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
        val myId = SharePreUtil.GetShareInt(requireContext(), Constants.KEY_USER_ID)
        getViewModel().setUp(requireContext(), myId)
        getData()
        getBarChartData()

        barDataSet = BarDataSet(barEntriesList, "Week Report")

        barData = BarData(barDataSet)
            viewBinding.idBarChart.data = barData
        barDataSet.valueTextColor = Color.BLACK
        barDataSet.color = resources.getColor(R.color.purple_200)
        barDataSet.valueTextSize = 16f
        viewBinding.idBarChart.description.isEnabled = false
    }

    override fun registerViewEvent() {
    }

    override fun registerViewModelObs() {
    }

    private fun getBarChartData() {
        barEntriesList = ArrayList()


        val xVals = ArrayList<String>()
        xVals.add("Last week")
        xVals.add("This week")
        val xAxis = viewBinding.idBarChart.xAxis
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.setCenterAxisLabels(true)
        xAxis.setDrawGridLines(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        // Below line will add labels on your xAxis:
        xAxis.valueFormatter = IndexAxisValueFormatter(xVals)
        barEntriesList.add(BarEntry(0.5f, getViewModel().lastWeekTotal.toFloat()))
        barEntriesList.add(BarEntry(1.5f, getViewModel().thisWeekTotal.toFloat()))
    }

    private fun getData() {
        getViewModel().getData() {
            viewBinding.tvTopSpend.text = it
        }

    }
}