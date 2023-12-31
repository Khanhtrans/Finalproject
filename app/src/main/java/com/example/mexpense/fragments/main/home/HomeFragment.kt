package com.example.mexpense.fragments.main.home

import android.annotation.SuppressLint
import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mexpense.R
import com.example.mexpense.activity.main.notification.NotificationActivity
import com.example.mexpense.activity.main.wallet.WalletActivity
import com.example.mexpense.base.BaseMVVMFragment
import com.example.mexpense.base.SharePreUtil
import com.example.mexpense.databinding.FragmentHomeBinding
import com.example.mexpense.entity.Transaction
import com.example.mexpense.exts.gone
import com.example.mexpense.fragments.main.balance.TransactionAdapter
import com.example.mexpense.fragments.main.home.month_report.MonthReportFragment
import com.example.mexpense.fragments.main.home.week_report.WeekReportFragment
import com.example.mexpense.services.SqlService
import com.example.mexpense.ultilities.Constants
import com.google.android.material.tabs.TabLayoutMediator
import java.text.DecimalFormat

class HomeFragment : BaseMVVMFragment<FragmentHomeBinding,HomeViewModel>() {
    private lateinit var viewBinding: FragmentHomeBinding
    private lateinit var sqlService: SqlService
    private var isShow = true
    private val transactionAdapter by lazy {
        TransactionAdapter(
            onClicked = ::onClicked,
        )
    }
    override fun getViewModelClass(): Class<HomeViewModel> {
        return HomeViewModel::class.java
    }

    override fun getLayoutBinding(): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }

    @SuppressLint("ResourceAsColor")
    override fun initialize() {
        sqlService =  SqlService.getInstance(requireContext())
        viewBinding = getViewBinding()

        viewBinding.viewpager.adapter = object : FragmentStateAdapter(this) {

            override fun createFragment(position: Int): Fragment {
                return ResourceStore.pagerFragments[position]
            }

            override fun getItemCount(): Int {
                return ResourceStore.tabList.size
            }
        }
        val myId = SharePreUtil.GetShareInt(requireContext(), Constants.KEY_USER_ID);
        val trans = sqlService.getMyTransaction(myId)
        if (trans.size !=0) viewBinding.tvNoTrans.gone()
        transactionAdapter.setItems(trans)
        viewBinding.rvTrans.apply {
            adapter = transactionAdapter
        }
        //renderTabLayer
        TabLayoutMediator(viewBinding.tabLayout, viewBinding.viewpager) { tab, position ->
            tab.text = getString(ResourceStore.tabList[position])
        }.attach()

    }

    override fun registerViewEvent() {
        viewBinding.btnView.setOnClickListener {
            val myId = SharePreUtil.GetShareInt(requireContext(), Constants.KEY_USER_ID);
            val total = sqlService.getUser(myId).total
            val formatter = DecimalFormat("#,###,###")
            val totalFormatted: String = formatter.format(total)
            if (isShow) viewBinding.tvTotal.text = totalFormatted
            else viewBinding.tvTotal.text = "********"
            isShow = !isShow
        }
        viewBinding.ivNotification.setOnClickListener {
            val intent = Intent(requireActivity(), NotificationActivity::class.java)
            requireActivity().startActivity(intent)
            Toast.makeText(requireContext(),"show history",Toast.LENGTH_SHORT).show()
        }

        viewBinding.tvSeeMoreWallet.setOnClickListener {
            val intent = Intent(requireActivity(),WalletActivity::class.java)
            requireActivity().startActivity(intent)
        }
    }

    override fun registerViewModelObs() {
    }

    fun onClicked(transaction: Transaction){

    }
}

interface ResourceStore {
    companion object {
        val tabList = listOf(
            R.string.tab1, R.string.tab2
        )
        val pagerFragments = listOf(
            WeekReportFragment(),
            MonthReportFragment())
    }
}