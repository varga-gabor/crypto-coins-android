package com.aldi.cryptocoins.architecture.navigation.internal

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity

data class ActivityWrapper(
    val activity: AppCompatActivity,
    @IdRes val fragmentContainerResId: Int,
)
