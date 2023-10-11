package com.example.mexpense.fragments.main.home.month_report

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mexpense.R

class MonthReportFragment : Fragment() {

    companion object {
        fun newInstance() = MonthReportFragment()
    }

    private lateinit var viewModel: MonthReportViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_month_report, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MonthReportViewModel::class.java)
        // TODO: Use the ViewModel
    }

}