package com.example.mexpense.activity.main.notification

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mexpense.base.BaseAdapter
import com.example.mexpense.databinding.ItemNotificationBinding
import com.example.mexpense.entity.Notification
import com.example.mexpense.entity.NotificationModel
import com.example.mexpense.exts.setOnDelayClickListener

class NotificationAdapter(
    private val onDeletedClicked: (Notification) -> Unit,
) : BaseAdapter<ItemNotificationBinding, Notification>() {
    override fun getLayoutBinding(parent: ViewGroup, viewType: Int): ItemNotificationBinding {
        return ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    @SuppressLint("SetTextI18n")
    override fun bind(
        binding: ItemNotificationBinding,
        item: Notification,
        position: Int
    ) {
        binding.tvTitleNotification.text = item.date
        binding.tvContentNotification.text = "Add ${item.content}"
        binding.ivNotificationRemove.setOnDelayClickListener {
            onDeletedClicked.invoke(item)
        }
    }
}