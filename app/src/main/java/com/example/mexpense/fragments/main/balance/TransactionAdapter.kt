package com.example.mexpense.fragments.main.balance

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mexpense.base.BaseAdapter
import com.example.mexpense.databinding.ItemTransactionBinding
import com.example.mexpense.entity.Transaction
import com.example.mexpense.entity.Wallet

class TransactionAdapter(
    private val onClicked: (Transaction) -> Unit,
): BaseAdapter<ItemTransactionBinding,Transaction>() {
    override fun getLayoutBinding(parent: ViewGroup, viewType: Int): ItemTransactionBinding {
        return ItemTransactionBinding.inflate(LayoutInflater.from(parent.context),parent,false)
    }

    override fun bind(binding: ItemTransactionBinding, item: Transaction, position: Int) {
            binding.tvName.text = item.name
        val amount = String.format("%,d", item.amount)
        binding.tvAmount.text = amount
        binding.tvNote.text = item.note
        binding.tvDate.text = item.date
        binding.container.setOnClickListener { onClicked(item) }
    }
}