package com.example.mexpense.entity

import android.os.Parcelable

data class ActivitiesModel(
    val id: Long = 0,
    val image: Int,
    val typeActivity: String,
    val valueActivity: String,
    val valueUnitActivity: String,
    val averageTitleActivity: String,
    val averageValueActivity: String,
    val averageUnitActivity: String,
    val averageTitle2Activity: String,
    val averageValue2Activity: String,
    val averageUnit2Activity: String,
    val viewType: Int,
    val timeActivity: String
)