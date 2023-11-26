package com.example.mexpense.fragments.main.home.month_report

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.mexpense.base.BaseViewModel
import com.example.mexpense.entity.Transaction
import com.example.mexpense.services.SqlService
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MonthReportViewModel : BaseViewModel() {
    private lateinit var sqlService: SqlService
    private var userId : Int? = null
    private var trans = arrayListOf<Transaction>()
    var thisMonthTotal = 0L
    var lastMonthTotal = 0L

    private val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    fun setUp(context: Context, id: Int) {
        sqlService = SqlService.getInstance(context)
        userId = id
        trans.addAll(sqlService.getMyTransaction(userId!!))
    }

    fun getData(setMaxTrans: (String)-> Unit) {
        thisMonthTotal = 0
        lastMonthTotal = 0
        var maxTrans = 0L
        var maxTransName = ""
        val myTrans = sqlService.getMyTransaction(userId!!)
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
        setMaxTrans(maxTransName)
    }
}