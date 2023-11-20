package com.example.mexpense.activity.main.wallet

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mexpense.R
import com.example.mexpense.base.SharePreUtil
import com.example.mexpense.databinding.ActivityAddWalletBinding
import com.example.mexpense.entity.Wallet
import com.example.mexpense.services.SqlService
import com.example.mexpense.ultilities.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
                (parent.getChildAt(0) as TextView).setTextColor(Color.WHITE)

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
                selectCate = categories[0]
                (parent.getChildAt(0) as TextView).setTextColor(Color.WHITE)

            }
        }

        binding.btnSave.setOnClickListener {
            val name = binding.edtName.text.toString().trim()
            val initial = binding.edtInitialBalance.text.toString().trim().toLongOrNull()?:0
            val currency = binding.edtCurrency.text.toString().trim()
            if (initial <=0) {
                Toast.makeText(this,"Please input initial balance!",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (name.isNotEmpty() && initial>0 && currency.isNotEmpty()) {
                val userId = SharePreUtil.GetShareInt(this, Constants.KEY_USER_ID);

                val totalWallet = databaseHelper.totalMoney(userId)
                val user = databaseHelper.getUser(userId)
                if (totalWallet + initial > user.total) {
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    val view = this.currentFocus
                    imm.hideSoftInputFromWindow(view?.windowToken, 0)
                    Toast.makeText(this,
                        "Over current total money, re-input initial balance",
                        Toast.LENGTH_SHORT).show()

                    return@setOnClickListener
                }

                val calendar = Calendar.getInstance()
                val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val date = simpleDateFormat.format(calendar.time)

                val wallet = Wallet().apply {
                    this.name = name
                    this.currency = currency
                    initialBalance = initial
                    category = selectCate
                    dayAdd = date
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