package com.example.mexpense.fragments.main.balance

import android.app.DatePickerDialog
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.mexpense.R
import com.example.mexpense.activity.main.wallet.WalletActivity
import com.example.mexpense.base.BaseActivity
import com.example.mexpense.base.BaseMVVMFragment
import com.example.mexpense.base.SharePreUtil
import com.example.mexpense.databinding.FragmentBalanceBinding
import com.example.mexpense.entity.Transaction
import com.example.mexpense.entity.Wallet
import com.example.mexpense.exts.gone
import com.example.mexpense.exts.setOnDelayClickListener
import com.example.mexpense.exts.visible
import com.example.mexpense.services.SqlService
import com.example.mexpense.ultilities.Constants
import java.text.SimpleDateFormat
import java.util.*

class BalanceFragment : BaseMVVMFragment<FragmentBalanceBinding,BalanceViewModel>() {
    private lateinit var viewBinding: FragmentBalanceBinding
    private lateinit var sqlService: SqlService
    private var selectWallet : Int? = null
    private var mode = 0

    private val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    private val formatMonth = SimpleDateFormat("MM-yyyy", Locale.getDefault())

    private val transactionAdapter by lazy {
        TransactionAdapter(
            onClicked = ::onClicked,
        )
    }

    private fun onClicked(transaction: Transaction) {
        val alertDialog = ViewDialog()
        // hiển thị ảnh lên dialog
        alertDialog.showDialog(requireActivity(),transaction.bill)
    }

    companion object {
        fun newInstance() = BalanceFragment()
    }

    override fun getViewModelClass(): Class<BalanceViewModel> {
        return BalanceViewModel::class.java
    }

    override fun getLayoutBinding(): FragmentBalanceBinding {
        return FragmentBalanceBinding.inflate(layoutInflater)
    }

    override fun initialize() {
        viewBinding = getViewBinding()
        sqlService = SqlService.getInstance(requireContext())

        val myId = SharePreUtil.GetShareInt(requireContext(), Constants.KEY_USER_ID);
        val trans = sqlService.getMyTransaction(myId)
        if (trans.size !=0) viewBinding.tvNoTrans.gone()

        // set list cho adapter
        transactionAdapter.setItems(trans)

        // set adapter cho recyclerview , view hiển thị list transaction
        viewBinding.rvTrans.apply {
            adapter = transactionAdapter
        }
        viewBinding.btnWallet.setOnDelayClickListener {
            // hiển thị 1 dialog chứa danh sách ví của user
            // sau đó lọc danh sách transaction theo ví đã chọn
            withMultiChoiceList(trans)
        }
        val calendar = Calendar.getInstance()
        val monthString = formatMonth.format(calendar.time)
        viewBinding.tvMonth.text = monthString

    }

    override fun registerViewEvent() {
        viewBinding.tvThisMonth.setOnDelayClickListener {
            updateTransList(true)
            viewBinding.zoneSelectDate.gone()
            viewBinding.viewUnderThisMonth.visible()
            viewBinding.viewUnderLastMonth.gone()
        }

        viewBinding.tvLastMonth.setOnDelayClickListener {
            updateTransList(false)
            viewBinding.zoneSelectDate.gone()
            viewBinding.viewUnderLastMonth.visible()
            viewBinding.viewUnderThisMonth.gone()
        }

        viewBinding.btnChooseZone.setOnDelayClickListener {
            if (viewBinding.zoneSelectDate.visibility == View.VISIBLE) {
                viewBinding.zoneSelectDate.gone()
                viewBinding.tvMonth.visible()
                updateTransList(true)
            } else {
                viewBinding.zoneSelectDate.visible()
                viewBinding.tvMonth.gone()
            }
        }
        val cal = Calendar.getInstance()

        // sự kiện sau khi chọn ngày start
        val startDateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            viewBinding.tvTimeStart.text = simpleDateFormat.format(cal.time) // hiển thị lên view end date theo định dạng mm-yyyy

            if (isSelectValidDate()) updateTransListByFilter()
            else  (activity as BaseActivity).showToast("Select date bigger than start date")
        }

        // sự kiện sau khi chon ngày end trên dialog
        val returnDateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            viewBinding.tvTimeEnd.text = simpleDateFormat.format(cal.time) // hiển thị lên view end date theo định dạng mm-yyyy

            if (isSelectValidDate()) updateTransListByFilter()
            else  (activity as BaseActivity).showToast("Select end date bigger than start date")
        }


        // set suwj kien khi bấm vào start date và end date
        viewBinding.tvTimeStart.setOnDelayClickListener {
            // hiển thị 1 dialog chọn ngày
            DatePickerDialog(requireContext(), startDateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        viewBinding.tvTimeEnd.setOnDelayClickListener {
            DatePickerDialog(requireContext(), returnDateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    override fun registerViewModelObs() {

    }

    // so sánh 2 ngày xem start Date có < hoặc = end Date ko
    private fun isSelectValidDate(): Boolean {
        val calendar = Calendar.getInstance().time

        val startDate = try {
            simpleDateFormat.parse(viewBinding.tvTimeStart.text.toString())
        } catch (e: Exception) {
            val today = simpleDateFormat.format(calendar.time)
            simpleDateFormat.parse(today)
        }

        val endDate = try {
            simpleDateFormat.parse(viewBinding.tvTimeEnd.text.toString())
        } catch (e: Exception) {
            val today = simpleDateFormat.format(calendar.time)
            simpleDateFormat.parse(today)
        }
        return startDate <= endDate
    }

    private fun updateTransList(isThisMonth : Boolean) {
        // lấy danh sách transaction của user vừa login và lọc theo tháng
        val myId = SharePreUtil.GetShareInt(requireContext(), Constants.KEY_USER_ID);
        val transList = sqlService.getMyTransaction(myId)


        val afterList = arrayListOf<Transaction>()
        val calendar = Calendar.getInstance()

        val currentMonthString = formatMonth.format(calendar.time) // lấy ra tháng này là gì và hiển thị theo định dạng MM-yyyy


        val currentMonth = calendar.get(Calendar.MONTH) + 1 // thag này là gì
        calendar.add(Calendar.MONTH, -1)
        val lastMonth = calendar.get(Calendar.MONTH) + 1 // tháng trc là gì, return Date()
        val lastMonthString = formatMonth.format(calendar.time) // đưa tháng về định dạng mm-yyyy , return String

        for (trans in transList) {
            val date = simpleDateFormat.parse(trans.date)
            val calendarAdd = Calendar.getInstance()
            calendarAdd.time = date
            val addMonth = calendarAdd.get(Calendar.MONTH) + 1
            if (selectWallet != null && trans.wallet == selectWallet) {
                // hễ thỏa mãn điều kiện là tháng nào theo filter đã chọn thì thêm vào afterList
                if (isThisMonth && currentMonth == addMonth ) afterList.add(trans)
                else if (!isThisMonth && addMonth == lastMonth) afterList.add(trans)
            } else {
                if (isThisMonth && currentMonth == addMonth) afterList.add(trans)
                else if (!isThisMonth && addMonth == lastMonth) afterList.add(trans)
            }

        }

        viewBinding.tvMonth.text = if (isThisMonth) currentMonthString else lastMonthString

        transList.clear()
        transList.addAll(afterList)
        transactionAdapter.setItems(transList)
    }


    // filter danh sách transaction theo range date đã chọn
    private fun updateTransListByFilter() {
        val myId = SharePreUtil.GetShareInt(requireContext(), Constants.KEY_USER_ID);
        val transList = sqlService.getMyTransaction(myId)
        val afterList = arrayListOf<Transaction>()
        val calendar = Calendar.getInstance().time



        // parse text trên view sang dạng Date, lấy ra startDate
        val startDate = try {
            simpleDateFormat.parse(viewBinding.tvTimeStart.text.toString())
        } catch (e: Exception) {
            val today = simpleDateFormat.format(calendar.time)
            simpleDateFormat.parse(today)
        }


        // parse text lấy ra endDate
        val endDate = try {
            simpleDateFormat.parse(viewBinding.tvTimeEnd.text.toString())
        } catch (e: Exception) {
           val today = simpleDateFormat.format(calendar.time)
           simpleDateFormat.parse(today)
        }


        for (trans in transList) {
            val date = simpleDateFormat.parse(trans.date)
            // kiểm tra đã chọn Ví nào để xem chưa
            if (selectWallet != null) {
                // kiểm tra date của transaction có nằm trong khoảng từ start đến end ko, và có dùng ví đã chọn ko
                if (date!= null && date <= endDate && date >= startDate && trans.wallet == selectWallet)
                    afterList.add(trans)
            } else {
                // nếu chưa chọn ví thì chỉ check có nằm trong khoảng ngày đã chọn ko
                if (date!= null && date <= endDate && date >= startDate)
                    afterList.add(trans)
            }

        }
        transList.clear()
        transList.addAll(afterList)
        transactionAdapter.setItems(transList)
    }

    private fun withMultiChoiceList(trans: List<Transaction>) {
        val userId = SharePreUtil.GetShareInt(requireContext(), Constants.KEY_USER_ID);
        val myWallets = sqlService.getMyWallets(userId)
        val items = Array(myWallets.size){""}
        for (i in myWallets.indices) {
            items[i] = myWallets[i].name?:""
        }

        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("This is list choice dialog box")
        builder.setSingleChoiceItems(items, -1) { dialogInterface, i ->
            // hiển thị tên ví lên nút wallet
            viewBinding.btnWallet.text = items[i]

            // lọc ra danh sách transaction sử dụng ví đã chọn
            val filetered = trans.filter { trans ->   trans.wallet == myWallets[i].id}
            transactionAdapter.setItems(filetered)
            selectWallet = myWallets[i].id
            viewBinding.viewUnderThisMonth.gone()
            viewBinding.viewUnderLastMonth.gone()
            dialogInterface.dismiss()
        }
        // Set the neutral/cancel button click listener
        builder.setNeutralButton("Cancel") { dialog, which ->
            // Do something when click the neutral button
            dialog.cancel()
        }

        builder.show()

    }

}