package com.example.mexpense.activity.main.wallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mexpense.R
import com.example.mexpense.databinding.ActivityWalletBinding

class WalletActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWalletBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvClose.setOnClickListener { finish() }

        binding.btnAddWallet.setOnClickListener {
            val intent = Intent(this,AddWalletActivity::class.java)
            startActivity(intent)
        }
    }
}