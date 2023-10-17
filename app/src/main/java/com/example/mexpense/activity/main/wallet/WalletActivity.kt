package com.example.mexpense.activity.main.wallet

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mexpense.R
import com.example.mexpense.base.BaseActivity
import com.example.mexpense.base.SharePreUtil
import com.example.mexpense.databinding.ActivityWalletBinding
import com.example.mexpense.entity.Wallet
import com.example.mexpense.services.SqlService
import com.example.mexpense.ultilities.Constants

class WalletActivity : BaseActivity() {
    private lateinit var binding: ActivityWalletBinding
    private lateinit var databaseHelper: SqlService

    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            // Handle the Intent
            val myId = SharePreUtil.GetShareInt(this, Constants.KEY_USER_ID);

            walletAdapter.setItems(databaseHelper.getMyWallets(myId))
            binding.rvWallets.adapter = walletAdapter
            binding.tvTotalBalance.text = databaseHelper.totalMoney(myId).toString()
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
        val total = databaseHelper.totalMoney(myId)
        binding.tvTotalBalance.text = total.toString()
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

    fun onClicked(wallet: Wallet) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val myId = SharePreUtil.GetShareInt(this, Constants.KEY_USER_ID);

        walletAdapter.setItems(databaseHelper.getMyWallets(myId))
        walletAdapter.notifyDataSetChanged()
    }
}