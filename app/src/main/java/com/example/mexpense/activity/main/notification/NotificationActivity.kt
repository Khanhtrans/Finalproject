package com.example.mexpense.activity.main.notification

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mexpense.databinding.ActivityNotificationBinding
import com.example.mexpense.entity.NotificationModel
import com.example.mexpense.exts.setOnDelayClickListener

class NotificationActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityNotificationBinding
    private lateinit var notificationAdapter : NotificationAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initViews()

    }

    private fun initViews() {
        supportActionBar?.hide()
        viewBinding.layoutHeader.tvTitleNameScreen.text = "Notifications"
        viewBinding.layoutHeader.ivBack.setOnDelayClickListener { finish() }
        viewBinding.layoutHeader.tvEdit.text = "Clear all"
        notificationAdapter = NotificationAdapter {
            onDeletedItemNotification(it)
        }
        val notifiList = arrayListOf<NotificationModel>(
            NotificationModel(1,"expanse","Added a new expanse"),
            NotificationModel(2,"trip","Added a new trip"),
            NotificationModel(3,"expanse","Added a new expanse"),
            NotificationModel(4,"trip","Added a new trip"),
            NotificationModel(5,"expanse","Added a new expanse"),
        )
        notificationAdapter.setItems(notifiList)
        viewBinding.rvNotification.apply {
            adapter = notificationAdapter
        }
        viewBinding.layoutHeader.tvEdit.setOnDelayClickListener {
            notificationAdapter.setItems(arrayListOf())
            viewBinding.rvNotification.apply {
                adapter = notificationAdapter
            }
        }
    }

    private fun onDeletedItemNotification(notificationModel: NotificationModel) {
    }
}