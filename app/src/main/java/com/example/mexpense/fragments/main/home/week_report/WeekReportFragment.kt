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
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class WeekReportFragment : BaseMVVMFragment<FragmentWeekReportBinding,WeekReportViewModel>() {
    lateinit var viewBinding: FragmentWeekReportBinding
    private lateinit var sqlService: SqlService
    private var calendar = Calendar.getInstance()

    lateinit var barData: BarData
    lateinit var barDataSet: BarDataSet
    lateinit var barEntriesList: ArrayList<BarEntry>

    var thisWeekTotal = 0L
    var lastWeekTotal = 0L
    val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val format: DateFormat = SimpleDateFormat("M-dd", Locale.getDefault())

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
        val currentW = getCurrentWeek()
        val preWeek = getPreviousWeek()
        val nextW = getNextWeek()

        for (trans in myTrans) {
            val date = simpleDateFormat.parse(trans.date)
            val calendarTrans = Calendar.getInstance()
            val currentWeek = calendarTrans.get(Calendar.WEEK_OF_MONTH)
            if (date != null) {
                calendarTrans.time = date
                val dateStr = format.format(date)

                if (currentW?.any { wek -> wek == dateStr } == true) {
                    if (maxTrans < trans.amount) {
                        maxTrans = trans.amount
                        maxTransName = trans.name
                    }
                    thisWeekTotal += trans.amount
                }

                if (preWeek?.any { wek -> wek == dateStr } == true) {
                    lastWeekTotal += trans.amount
                }
            }
        }
        viewBinding.tvTopSpend.text = maxTransName
    }

    fun getCurrentWeek(): Array<String?>? {
        calendar = Calendar.getInstance()
        this.calendar.firstDayOfWeek = Calendar.SUNDAY
        this.calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        return getNextWeek()
    }

    fun getNextWeek(): Array<String?>? {
        val days = arrayOfNulls<String>(7)
        for (i in 0..6) {
            days[i] = format.format(this.calendar.getTime())
            this.calendar.add(Calendar.DATE, 1)
        }
        return days
    }

    fun getPreviousWeek(): Array<String?>? {
        this.calendar.add(Calendar.DATE, -14)
        return getNextWeek()
    }
}