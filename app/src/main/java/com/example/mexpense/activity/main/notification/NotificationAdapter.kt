package com.example.mexpense.activity.main.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mexpense.base.BaseAdapter
import com.example.mexpense.databinding.ItemNotificationBinding
import com.example.mexpense.entity.NotificationModel
import com.example.mexpense.exts.setOnDelayClickListener

class NotificationAdapter(
    private val onDeletedClicked: (NotificationModel) -> Unit,
) : BaseAdapter<ItemNotificationBinding, NotificationModel>() {
    override fun getLayoutBinding(parent: ViewGroup, viewType: Int): ItemNotificationBinding {
        return ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(
        binding: ItemNotificationBinding,
        item: NotificationModel,
        position: Int
    ) {
        binding.tvTitleNotification.text = item.title
        binding.tvContentNotification.text = item.content
        binding.ivNotificationRemove.setOnDelayClickListener {
            onDeletedClicked.invoke(item)
        }
    }
}