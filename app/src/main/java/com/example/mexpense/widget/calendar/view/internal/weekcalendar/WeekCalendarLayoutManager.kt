package com.example.upbrain.shared.widget.calendar.view.internal.weekcalendar

import com.example.upbrain.shared.widget.calendar.view.MarginValues
import com.example.upbrain.shared.widget.calendar.view.WeekCalendarView
import com.example.upbrain.shared.widget.calendar.view.internal.CalendarLayoutManager
import com.example.upbrain.shared.widget.calendar.view.internal.dayTag
import java.time.LocalDate

internal class WeekCalendarLayoutManager(private val calView: WeekCalendarView) :
    CalendarLayoutManager<LocalDate, LocalDate>(calView, HORIZONTAL) {

    private val adapter: WeekCalendarAdapter
        get() = calView.adapter as WeekCalendarAdapter

    override fun getaItemAdapterPosition(data: LocalDate): Int = adapter.getAdapterPosition(data)
    override fun getaDayAdapterPosition(data: LocalDate): Int = adapter.getAdapterPosition(data)
    override fun getDayTag(data: LocalDate): Int = dayTag(data)
    override fun getItemMargins(): MarginValues = calView.weekMargins
    override fun scrollPaged(): Boolean = calView.scrollPaged
    override fun notifyScrollListenerIfNeeded() = adapter.notifyWeekScrollListenerIfNeeded()
}
