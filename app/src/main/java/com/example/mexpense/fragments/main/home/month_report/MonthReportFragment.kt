package com.example.mexpense.fragments.main.home.month_report

import android.graphics.Color
import android.graphics.Typeface
import com.example.mexpense.R
import com.example.mexpense.base.BaseMVVMFragment
import com.example.mexpense.databinding.FragmentMonthReportBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter


class MonthReportFragment : BaseMVVMFragment<FragmentMonthReportBinding,MonthReportViewModel>() {
    private lateinit var viewBinding: FragmentMonthReportBinding

    lateinit var barData: BarData

    lateinit var barDataSet: BarDataSet

    lateinit var barEntriesList: ArrayList<BarEntry>

    private val MAX_X_VALUE = 13
    private val GROUPS = 2
    private val GROUP_1_LABEL = "Last month"
    private val GROUP_2_LABEL = "This month"
    private val BAR_SPACE = 0.1f
    private val BAR_WIDTH = 0.8f
    private var chart: BarChart? = null
    private var pieChart: PieChart? = null
    protected var tfRegular: Typeface? = null
    protected var tfLight: Typeface? = null

    private val statValues: ArrayList<Float> = ArrayList()

    protected val statsTitles = arrayOf(
        "Last month", "This month"
    )
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
        getBarChartData()

        barDataSet = BarDataSet(barEntriesList, "Bar Chart Data")

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
        barEntriesList.add(BarEntry(1f, 1f,"Last month"))
        barEntriesList.add(BarEntry(2f, 2f,"This month"))


    }

}