package com.example.mexpense.fragments.main.budget

import android.graphics.Color
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.mexpense.R
import com.example.mexpense.base.BaseMVVMFragment
import com.example.mexpense.base.SharePreUtil
import com.example.mexpense.databinding.FragmentBudgetBinding
import com.example.mexpense.entity.Category
import com.example.mexpense.exts.gone
import com.example.mexpense.exts.setOnDelayClickListener
import com.example.mexpense.exts.visible
import com.example.mexpense.repository.transCategory
import com.example.mexpense.services.SqlService
import com.example.mexpense.ultilities.Constants
import java.text.SimpleDateFormat
import java.time.YearMonth
import java.util.*

class BudgetFragment : BaseMVVMFragment<FragmentBudgetBinding, BudgetViewModel>() {
    private lateinit var viewBinding: FragmentBudgetBinding
    private lateinit var sqlService: SqlService
    private lateinit var cateList: ArrayList<Category>
    val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val formatMonth = SimpleDateFormat("MM/yyyy", Locale.getDefault())

    companion object {
        fun newInstance() = BudgetFragment()
    }

    override fun getViewModelClass(): Class<BudgetViewModel> {
        return BudgetViewModel::class.java
    }

    override fun getLayoutBinding(): FragmentBudgetBinding {
        return FragmentBudgetBinding.inflate(layoutInflater)
    }

    override fun initialize() {
        viewBinding = getViewBinding()
        sqlService = SqlService.getInstance(requireContext())

        val myId = SharePreUtil.GetShareInt(requireContext(), Constants.KEY_USER_ID);

        val totalWallet = sqlService.totalMoney(myId)
        viewBinding.tvTotalWallet.text = totalWallet.toString()

        val totalSpend = sqlService.getMySpend(myId)
        viewBinding.tvTotalSpend.text = totalSpend.toString()

        viewBinding.tvNumberDay.text = getDaysOfMonth(true)
        val calendar = Calendar.getInstance()

        val monthString = formatMonth.format(calendar.time)
        viewBinding.tvTime.text = monthString

        viewBinding.tvRemain.text = (totalWallet - totalSpend).toString()

        val iconSearch =
            viewBinding.svCategory.findViewById<ImageView>(androidx.appcompat.R.id.search_mag_icon)
        val iconClose =
            viewBinding.svCategory.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        val edtSearch =
            viewBinding.svCategory.findViewById<SearchView.SearchAutoComplete>(androidx.appcompat.R.id.search_src_text)
        edtSearch.setTextColor(Color.BLACK)
        edtSearch.setHintTextColor(Color.GRAY)
        iconSearch.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_search
            )
        )
        iconSearch.setColorFilter(Color.BLACK)
        iconClose.setColorFilter(Color.BLACK)

        cateList = getSpendingRecorded(transCategory)
        val adapter = BudgetAdapter(cateList, requireContext())
        viewBinding.rvTrans.apply {
            this.adapter = adapter
            itemAnimator = DefaultItemAnimator()
        }

        viewBinding.tvLastMonth.setOnDelayClickListener {
            viewBinding.viewUnderLastMonth.visible()
            viewBinding.viewUnderThisMonth.gone()
            val listFilter = getSpendListByMonth(false, transCategory)
            adapter.arrayListFiltered.clear()
            adapter.arrayListFiltered.addAll(listFilter)
            adapter.notifyDataSetChanged()
        }

        viewBinding.tvThisMonth.setOnDelayClickListener {
            viewBinding.viewUnderLastMonth.gone()
            viewBinding.viewUnderThisMonth.visible()


            val listFilter = getSpendListByMonth(true, transCategory)
            adapter.arrayListFiltered.clear()
            adapter.arrayListFiltered.addAll(listFilter)
            adapter.notifyDataSetChanged()
        }

        viewBinding.svCategory.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                adapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                adapter.filter.filter(newText)
                return false
            }
        })
    }

    private fun getDaysOfMonth(isThisMonth: Boolean): String {
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH) + 1
        calendar.add(Calendar.MONTH, -1)
        val m = calendar.get(Calendar.MONTH) + 1
        val yearMonthObject: YearMonth = YearMonth.of(2023, if (isThisMonth) currentMonth else m)
        val days = yearMonthObject.lengthOfMonth()
        return "$days days" //28
    }

    private fun getSpendingRecorded(initCate: List<Category>): ArrayList<Category> {
        val myId = SharePreUtil.GetShareInt(requireContext(), Constants.KEY_USER_ID);
        val transList = sqlService.getMyTransaction(myId)
        val afterCate = arrayListOf<Category>()
        afterCate.addAll(initCate)
        for (trans in transList) {
            for (cate in afterCate) {
                if (trans.category == cate.name) cate.spend += trans.amount
            }
        }
        return afterCate
    }

    private fun getSpendListByMonth(
        isThisMonth: Boolean,
        initCate: List<Category>
    ): ArrayList<Category> {
        val myId = SharePreUtil.GetShareInt(requireContext(), Constants.KEY_USER_ID);
        val transList = sqlService.getMyTransaction(myId)
        val afterCate = arrayListOf<Category>()
        var totalSpend = 0L

        afterCate.addAll(initCate)
        for (cate in afterCate) cate.spend = 0

        for (trans in transList) {

            val date = simpleDateFormat.parse(trans.date)

            for (cate in afterCate) {
                val calendar = Calendar.getInstance()
                val currentMonth = calendar.get(Calendar.MONTH) + 1

                if (isThisMonth && trans.category == cate.name && date != null) {
                    totalSpend = 0L
                    calendar.time = date
                    val month = calendar.get(Calendar.MONTH) + 1
                    if (month == currentMonth) {
                        totalSpend += trans.amount
                        cate.spend += trans.amount
                    }
                    val monthString = formatMonth.format(date)
                    viewBinding.tvTime.text = monthString
                    viewBinding.tvNumberDay.text = getDaysOfMonth(true)
                } else if (!isThisMonth && trans.category == cate.name && date != null) {
                    totalSpend = 0L
                    calendar.time = date
                    calendar.add(Calendar.MONTH, -1)
                    val m = calendar.get(Calendar.MONTH) + 1
                    val month = calendar.get(Calendar.YEAR) * 100 + calendar.get(Calendar.MONTH) - 1

                    if (month == currentMonth) {
                        cate.spend += trans.amount
                        totalSpend += trans.amount
                    }

                    val newDate = simpleDateFormat.parse(simpleDateFormat.format(calendar.time))
                    val monthString = formatMonth.format(newDate)
                    viewBinding.tvTime.text = monthString
                    viewBinding.tvNumberDay.text = getDaysOfMonth(false)
                }
            }

        }
        viewBinding.tvTotalSpend.text = totalSpend.toString()

        viewBinding.tvTotalWallet.text = getTotalBudget(myId, isThisMonth).toString()
        viewBinding.tvRemain.text = (getTotalBudget(myId, isThisMonth) - totalSpend).toString()
        return afterCate
    }

    override fun registerViewEvent() {
    }

    override fun registerViewModelObs() {

    }

    fun getTotalBudget(userId: Int, isThisMonth: Boolean): Long {
        val myWallet = sqlService.getMyWallets(userId)
        var total = 0L
        for (w in myWallet) {
            val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            var monthAdd = 0
            if (w.dayAdd != null) {
                val dateAdd = simpleDateFormat.parse(w.dayAdd)
                val calendar = Calendar.getInstance()
                calendar.time = dateAdd
                monthAdd = calendar.get(Calendar.MONTH) + 1


                val currentCalendar = Calendar.getInstance()
                val currentMont = currentCalendar.get(Calendar.MONTH) + 1
                currentCalendar.add(Calendar.MONTH, -1)
                val lastMonth = currentCalendar.get(Calendar.MONTH) + 1
                if (isThisMonth) {
                    if (currentMont == monthAdd) total += w.initialBalance
                } else {
                    if (monthAdd == lastMonth) total += w.initialBalance
                }
            } else total += 0L
        }
        return total
    }


}