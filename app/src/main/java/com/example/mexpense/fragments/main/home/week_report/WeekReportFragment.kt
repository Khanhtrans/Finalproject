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
    private lateinit var sqlService: SqlService

    lateinit var barData: BarData
    lateinit var barDataSet: BarDataSet
    lateinit var barEntriesList: ArrayList<BarEntry>

    var thisWeekTotal = 0L
    var lastWeekTotal = 0L
    val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
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
        sqlService = SqlService.getInstance(requireContext())
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
        barEntriesList.add(BarEntry(0.5f, lastWeekTotal.toFloat()))
        barEntriesList.add(BarEntry(1.5f, thisWeekTotal.toFloat()))
    }

    private fun getData() {
        thisWeekTotal = 0
        lastWeekTotal = 0
        var maxTrans = 0L
        var maxTransName = ""
        val myId = SharePreUtil.GetShareInt(requireContext(), Constants.KEY_USER_ID);
        val myTrans = sqlService.getMyTransaction(myId)
        for (trans in myTrans) {
            val date = simpleDateFormat.parse(trans.date)
            val calendar = Calendar.getInstance()
            val currentWeek = calendar.get(Calendar.WEEK_OF_MONTH)
            if (date != null) {
                calendar.time = date
                val addWeek = calendar.get(Calendar.WEEK_OF_MONTH)
                if (addWeek == currentWeek) {
                    if (maxTrans < trans.amount) {
                        maxTrans = trans.amount
                        maxTransName = trans.name
                    }
                    thisWeekTotal += trans.amount
                }

                val lastCal = Calendar.getInstance()
                lastCal.add(Calendar.WEEK_OF_MONTH, -1)
                val lastWeek = lastCal.get(Calendar.WEEK_OF_MONTH)
                if (addWeek == lastWeek) {
                    lastWeekTotal += trans.amount
                }
            }
        }
        viewBinding.tvTopSpend.text = maxTransName
    }
}