package com.example.mexpense.activity.main.wallet

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mexpense.base.BaseAdapter
import com.example.mexpense.databinding.ItemWalletBinding
import com.example.mexpense.entity.ActivitiesModel
import com.example.mexpense.entity.Wallet

class WalletAdapter(
    private val onClicked: (Wallet) -> Unit,
) : BaseAdapter<ItemWalletBinding, Wallet>()  {
    override fun getLayoutBinding(parent: ViewGroup, viewType: Int): ItemWalletBinding {
        return ItemWalletBinding.inflate(LayoutInflater.from(parent.context),parent,false)
    }

    override fun bind(binding: ItemWalletBinding,
                      item: Wallet,
                      position: Int) {
        binding.tvName.text = item.name
        binding.tvCurrency.text = item.currency
        binding.tvInitialBalance.text = item.initialBalance.toString()
        binding.tvCategory.text = item.category
    }
}