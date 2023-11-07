package com.example.mexpense.activity.main.wallet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mexpense.base.BaseActivity
import com.example.mexpense.base.SharePreUtil
import com.example.mexpense.databinding.ActivityWalletBinding
import com.example.mexpense.entity.Notification
import com.example.mexpense.entity.Wallet
import com.example.mexpense.exts.gone
import com.example.mexpense.exts.setOnDelayClickListener
import com.example.mexpense.exts.visible
import com.example.mexpense.services.SqlService
import com.example.mexpense.ultilities.Constants
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class WalletActivity : BaseActivity() {
    private lateinit var binding: ActivityWalletBinding
    private lateinit var databaseHelper: SqlService

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            // Handle the Intent
            val myId = SharePreUtil.GetShareInt(this, Constants.KEY_USER_ID);

            walletAdapter.setItems(databaseHelper.getMyWallets(myId))
            binding.rvWallets.adapter = walletAdapter
        }
    }

    private val walletAdapter by lazy {
        WalletAdapter(
            onClicked = ::onClicked,
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseHelper = SqlService.getInstance(this)

        val myId = SharePreUtil.GetShareInt(this, Constants.KEY_USER_ID);
        val user = databaseHelper.getUser(myId)
        val formatter = DecimalFormat("#,###,###")
        val totalFormatted: String = formatter.format(user.total)
        binding.tvTotalBalance.text = totalFormatted

        binding.btnTotal.setOnDelayClickListener {
            if (binding.zoneAddTotal.visibility == View.VISIBLE)
            binding.zoneAddTotal.gone() else binding.zoneAddTotal.visible()
        }

        binding.btnCancel.setOnDelayClickListener { binding.zoneAddTotal.gone() }

        binding.btnAddTotal.setOnDelayClickListener {
            val total = user.total
            if (binding.edtPlusTotal.text.trim().toString().toLongOrNull() != null) {
                val plus =  binding.edtPlusTotal.text.trim().toString().toLongOrNull() ?:0
                val newTotal = total + plus
                user.total = newTotal
                binding.tvTotalBalance.text = newTotal.toString()
                databaseHelper.updateUser(user)
                hideKeyboard()
                binding.zoneAddTotal.gone()
                val cal = Calendar.getInstance()
                val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault())
                val date = simpleDateFormat.format(cal.time)

                // add notify
                val notify = Notification()
                notify.content = binding.edtPlusTotal.text.trim().toString()
                notify.date =  date
                notify.user_id = user.id
                databaseHelper.addNotify(notify)
            }

        }

        binding.tvClose.setOnClickListener { finish() }

        binding.btnAddWallet.setOnClickListener {
            val intent = Intent(this,AddWalletActivity::class.java)
            startForResult.launch(intent)
        }

        walletAdapter.setItems(databaseHelper.getMyWallets(myId))
        binding.rvWallets.apply {
            adapter = walletAdapter
        }

    }

    private fun onClicked(wallet: Wallet,newAdd: Long) {
        val userId = SharePreUtil.GetShareInt(this, Constants.KEY_USER_ID);

        val totalWallet = databaseHelper.totalMoney(userId)
        val user = databaseHelper.getUser(userId)
        if (totalWallet + newAdd < user.total) {
            wallet.initialBalance += newAdd
            databaseHelper.updateWallet(wallet)

            walletAdapter.setItems(databaseHelper.getMyWallets(userId))
//            binding.rvWallets.apply {
//                adapter = walletAdapter
//            }
        } else {
            showToast("Cannot add more than total wallet")}
    }

}