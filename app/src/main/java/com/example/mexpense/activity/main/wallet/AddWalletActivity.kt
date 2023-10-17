package com.example.mexpense.activity.main.wallet

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.example.mexpense.R
import com.example.mexpense.base.SharePreUtil
import com.example.mexpense.databinding.ActivityAddWalletBinding
import com.example.mexpense.entity.Wallet
import com.example.mexpense.services.SqlService
import com.example.mexpense.ultilities.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class AddWalletActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddWalletBinding
    private lateinit var databaseHelper: SqlService

    var selectCate = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseHelper = SqlService.getInstance(this)


        binding.tvClose.setOnClickListener { finish() }

        val categories = resources.getStringArray(R.array.wallet_category_array)
        selectCate = categories[0]

        val adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, categories)
        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                selectCate = categories[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
                selectCate = categories[0]
            }
        }

        binding.btnSave.setOnClickListener {
            val name = binding.edtName.text.toString().trim()
            val initial = binding.edtInitialBalance.text.toString().trim()
            val currency = binding.edtCurrency.text.toString().trim()
            if (name.isNotEmpty() && initial.isNotEmpty() && currency.isNotEmpty()) {
                val userId = SharePreUtil.GetShareInt(this, Constants.KEY_USER_ID);
                val wallet = Wallet().apply {
                    this.name = name
                    this.currency = currency
                    initialBalance = initial.toLong()
                    category = selectCate
                    this.userId = userId
                }

                databaseHelper.addWallet(wallet)
                lifecycleScope.launch {
                    delay(1000L)
                }

                val data = Intent()
                setResult(RESULT_OK, data)
            }
            lifecycleScope.launch {
                delay(1000L)
                finish()
            }
        }

    }

}