package com.example.mexpense.fragments.main.home

import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.example.mexpense.activity.main.notification.NotificationActivity
import com.example.mexpense.activity.main.wallet.WalletActivity
import com.example.mexpense.base.BaseViewModel
import com.example.mexpense.base.SharePreUtil
import com.example.mexpense.entity.Transaction
import com.example.mexpense.services.SqlService
import com.example.mexpense.ultilities.Constants
import java.text.DecimalFormat

class HomeViewModel : BaseViewModel() {
    private lateinit var sqlService: SqlService
    var userId: MutableLiveData<Int> = MutableLiveData()
    var trans = arrayListOf<Transaction>()

    fun setUp(context: Context) {
        sqlService = SqlService.getInstance(context)
        userId.value = SharePreUtil.GetShareInt(context, Constants.KEY_USER_ID);
        trans.addAll(sqlService.getMyTransaction(userId.value!!))
    }
    fun intentNotify(context: Context): Intent {
        return Intent(context, NotificationActivity::class.java)
    }

    fun intentWallet(context: Context): Intent {
        return Intent(context, WalletActivity::class.java)
    }

    fun formatTotal() : String {
        val formatter = DecimalFormat("#,###,###")
        val total = sqlService.getUser(userId.value!!).total
        return formatter.format(total)
    }
}