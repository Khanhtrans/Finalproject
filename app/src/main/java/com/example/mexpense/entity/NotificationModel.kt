package com.example.mexpense.entity

import android.os.Parcel
import android.os.Parcelable

data class NotificationModel(
    val id: Long,
    val title: String,
    val content: String
)