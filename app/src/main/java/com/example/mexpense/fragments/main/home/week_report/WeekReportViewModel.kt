package com.example.mexpense.fragments.main.home.week_report

import android.content.Context
import com.example.mexpense.base.BaseViewModel
import com.example.mexpense.entity.Transaction
import com.example.mexpense.services.SqlService
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class WeekReportViewModel : BaseViewModel() {
    private lateinit var sqlService: SqlService
    private var userId : Int? = null
    private var trans = arrayListOf<Transaction>()
    var thisWeekTotal = 0L
    var lastWeekTotal = 0L
    private val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    fun setUp(context: Context, id: Int) {
        sqlService = SqlService.getInstance(context)
        userId = id
        trans.addAll(sqlService.getMyTransaction(userId!!))
    }

    fun getData(setMaxTrans: (String)-> Unit) {
        thisWeekTotal = 0
        lastWeekTotal = 0
        var maxTrans = 0L
        var maxTransName = ""
        val myTrans = sqlService.getMyTransaction(userId!!)
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
            setMaxTrans(maxTransName)
        }
    }
}