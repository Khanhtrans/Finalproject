package com.example.mexpense.activity.main.notification

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mexpense.base.SharePreUtil
import com.example.mexpense.databinding.ActivityNotificationBinding
import com.example.mexpense.entity.Notification
import com.example.mexpense.exts.setOnDelayClickListener
import com.example.mexpense.services.SqlService
import com.example.mexpense.ultilities.Constants

class NotificationActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityNotificationBinding
    private lateinit var notificationAdapter : NotificationAdapter
    private lateinit var sqlService: SqlService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        sqlService = SqlService.getInstance(this)
        initViews()

    }

    private fun initViews() {
        val myId = SharePreUtil.GetShareInt(this@NotificationActivity, Constants.KEY_USER_ID);
        supportActionBar?.hide()
        viewBinding.layoutHeader.tvTitleNameScreen.text = "Notifications"
        viewBinding.layoutHeader.ivBack.setOnDelayClickListener { finish() }
        notificationAdapter = NotificationAdapter {
            onDeletedItemNotification(it)
        }
        val notifyList = arrayListOf<Notification>()
        notifyList.addAll(sqlService.getMyNotify(myId))
        notificationAdapter.setItems(notifyList)
        viewBinding.rvNotification.apply {
            adapter = notificationAdapter
        }
        viewBinding.layoutHeader.tvEdit.setOnDelayClickListener {

        }
    }

    private fun onDeletedItemNotification(notificationModel: Notification) {
    }
}