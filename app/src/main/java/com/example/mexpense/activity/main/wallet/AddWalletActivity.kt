package com.example.mexpense.activity.main.wallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.mexpense.R
import com.example.mexpense.databinding.ActivityAddWalletBinding

class AddWalletActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddWalletBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvClose.setOnClickListener { finish() }

        val categories = resources.getStringArray(R.array.Category)

        val adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, categories)
        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        binding.btnSave.setOnClickListener { finish() }

    }
}