package com.example.mexpense.fragments.main.home.month_report

import android.graphics.Color
import com.example.mexpense.R
import com.example.mexpense.base.BaseMVVMFragment
import com.example.mexpense.base.SharePreUtil
import com.example.mexpense.databinding.FragmentMonthReportBinding
import com.example.mexpense.ultilities.Constants
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlin.collections.ArrayList


class MonthReportFragment : BaseMVVMFragment<FragmentMonthReportBinding,MonthReportViewModel>() {
    private lateinit var viewBinding: FragmentMonthReportBinding

    private lateinit var barData: BarData

    private lateinit var barDataSet: BarDataSet

    private lateinit var barEntriesList: ArrayList<BarEntry>

    companion object {
        fun newInstance() = MonthReportFragment()
    }

    override fun getViewModelClass(): Class<MonthReportViewModel> {
        return MonthReportViewModel::class.java
    }

    override fun getLayoutBinding(): FragmentMonthReportBinding {
        return FragmentMonthReportBinding.inflate(layoutInflater)
    }

    override fun initialize() {
        viewBinding = getViewBinding()
        val myId = SharePreUtil.GetShareInt(requireContext(), Constants.KEY_USER_ID);

        getViewModel().setUp(requireContext(),myId)
        getData()
        getBarChartData()

        barDataSet = BarDataSet(barEntriesList, "Month Report")

        barData = BarData(barDataSet)

        viewBinding.idBarChart.data = barData

        barDataSet.valueTextColor = Color.BLACK

        barDataSet.color = resources.getColor(R.color.purple_200)

        barDataSet.valueTextSize = 16f

        viewBinding.idBarChart.description?.isEnabled = false
    }

    override fun registerViewEvent() {

    }

    override fun registerViewModelObs() {

    }

    private fun getBarChartData() {
        barEntriesList = ArrayList()
        val xVals = ArrayList<String>()
        xVals.add("Last month")
        xVals.add("This month")

        val xAxis = viewBinding.idBarChart.xAxis
        xAxis.labelCount = 2
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.setCenterAxisLabels(true)
        xAxis.setDrawGridLines(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        // Below line will add labels on your xAxis:


        // Below line will add labels on your xAxis:
        xAxis.valueFormatter = IndexAxisValueFormatter(xVals)
        // on below line we are adding data
        // to our bar entries list
        barEntriesList.add(BarEntry(0.5f, getViewModel().lastMonthTotal.toFloat(),"Last month"))
        barEntriesList.add(BarEntry(1.5f, getViewModel().thisMonthTotal.toFloat(),"This month"))

    }

    private fun getData() {
        getViewModel().getData {  viewBinding.tvTopSpend.text = it }

    }

}