package com.example.mexpense.activity.main.wallet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mexpense.base.BaseAdapter
import com.example.mexpense.databinding.ItemWalletBinding
import com.example.mexpense.entity.ActivitiesModel
import com.example.mexpense.entity.Wallet
import com.example.mexpense.exts.gone
import com.example.mexpense.exts.setOnDelayClickListener
import java.text.DecimalFormat

class WalletAdapter(
    private val onClicked: (Wallet,Long) -> Unit,
) : BaseAdapter<ItemWalletBinding, Wallet>()  {
    override fun getLayoutBinding(parent: ViewGroup, viewType: Int): ItemWalletBinding {
        return ItemWalletBinding.inflate(LayoutInflater.from(parent.context),parent,false)
    }

    override fun bind(binding: ItemWalletBinding,
                      item: Wallet,
                      position: Int) {
        binding.tvName.text = item.name
        binding.tvCurrency.text = item.currency
        val formatter = DecimalFormat("#,###,###")
        val initialFormat: String = formatter.format(item.initialBalance)
        binding.tvInitialBalance.text = initialFormat
        binding.tvCategory.text = item.category
        binding.container.setOnClickListener {
            binding.zoneAddTotal.visibility = if (binding.zoneAddTotal.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }

        binding.btnCancel.setOnDelayClickListener { binding.zoneAddTotal.gone() }
        binding.btnAddTotal.setOnDelayClickListener {
            if (binding.edtPlusTotal.text.trim().toString().toLongOrNull() != null) {
                val newAdd = binding.edtPlusTotal.text.trim().toString().toLongOrNull()
                binding.zoneAddTotal.gone()
                onClicked(item,newAdd?:0)
            }
        }
    }
}