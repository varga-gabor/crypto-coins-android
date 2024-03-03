package com.aldi.cryptocoins.architecture.navigation.api

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity

interface Navigator {

    fun bindToActivity(activity: AppCompatActivity, @IdRes fragmentContainerResId: Int)

    fun navigateTo(destination: Destination)

    fun navigateBack()
}
