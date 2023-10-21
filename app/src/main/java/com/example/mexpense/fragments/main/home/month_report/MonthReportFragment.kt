package com.example.mexpense.fragments.main.home.month_report

import android.graphics.Color
import com.example.mexpense.R
import com.example.mexpense.base.BaseMVVMFragment
import com.example.mexpense.base.SharePreUtil
import com.example.mexpense.databinding.FragmentMonthReportBinding
import com.example.mexpense.services.SqlService
import com.example.mexpense.ultilities.Constants
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MonthReportFragment : BaseMVVMFragment<FragmentMonthReportBinding,MonthReportViewModel>() {
    private lateinit var viewBinding: FragmentMonthReportBinding
    private lateinit var sqlService: SqlService

    lateinit var barData: BarData

    lateinit var barDataSet: BarDataSet

    lateinit var barEntriesList: ArrayList<BarEntry>

    var thisMonthTotal = 0L
    var lastMonthTotal = 0L

    val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val formatMonth = SimpleDateFormat("MM/yyyy", Locale.getDefault())

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
        sqlService = SqlService.getInstance(requireContext())

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
        barEntriesList.add(BarEntry(0.5f, lastMonthTotal.toFloat(),"Last month"))
        barEntriesList.add(BarEntry(1.5f, thisMonthTotal.toFloat(),"This month"))

    }

    private fun getData() {
        thisMonthTotal = 0
        lastMonthTotal = 0
        var maxTrans = 0L
        var maxTransName = ""
        val myId = SharePreUtil.GetShareInt(requireContext(), Constants.KEY_USER_ID);
        val myTrans = sqlService.getMyTransaction(myId)
        for (trans in myTrans) {
            val date = simpleDateFormat.parse(trans.date)
            val calendar = Calendar.getInstance()
            val currentMonth = calendar.get(Calendar.MONTH) + 1
            if (date != null) {
                calendar.time = date
                val addMonth = calendar.get(Calendar.MONTH) + 1
                if (addMonth == currentMonth) {
                    if (maxTrans < trans.amount) {
                        maxTrans = trans.amount
                        maxTransName = trans.name
                    }
                    thisMonthTotal += trans.amount
                }
                val lastCal = Calendar.getInstance()
                lastCal.add(Calendar.MONTH, -1)
                val lastMonth = lastCal.get(Calendar.MONTH) + 1
                if (addMonth == lastMonth) {
                    lastMonthTotal += trans.amount
                }
            }

        }
        viewBinding.tvTopSpend.text = maxTransName
    }

}