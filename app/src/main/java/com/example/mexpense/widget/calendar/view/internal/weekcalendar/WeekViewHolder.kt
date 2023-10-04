package com.example.upbrain.shared.widget.calendar.view.internal.weekcalendar

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mexpense.widget.calendar.core.Week
import com.example.mexpense.widget.calendar.core.WeekDay
import com.example.upbrain.shared.widget.calendar.view.ViewContainer
import com.example.upbrain.shared.widget.calendar.view.WeekHeaderFooterBinder
import com.example.upbrain.shared.widget.calendar.view.internal.WeekHolder

internal class WeekViewHolder constructor(
    rootLayout: ViewGroup,
    private val headerView: View?,
    private val footerView: View?,
    private val weekHolder: WeekHolder<WeekDay>,
    private var weekHeaderBinder: WeekHeaderFooterBinder<ViewContainer>?,
    private var weekFooterBinder: WeekHeaderFooterBinder<ViewContainer>?,
) : RecyclerView.ViewHolder(rootLayout) {

    private var headerContainer: ViewContainer? = null
    private var footerContainer: ViewContainer? = null

    lateinit var week: Week

    fun bindWeek(week: Week) {
        this.week = week
        headerView?.let { view ->
            val headerContainer = headerContainer ?: weekHeaderBinder!!.create(view).also {
                headerContainer = it
            }
            weekHeaderBinder?.bind(headerContainer, week)
        }
        weekHolder.bindWeekView(week.days)
        footerView?.let { view ->
            val footerContainer = footerContainer ?: weekFooterBinder!!.create(view).also {
                footerContainer = it
            }
            weekFooterBinder?.bind(footerContainer, week)
        }
    }

    fun reloadDay(day: WeekDay) {
        weekHolder.reloadDay(day)
    }
}
