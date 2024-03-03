package com.aldi.cryptocoins.architecture.navigation.api

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

sealed interface Route {

    data class ScreenRoute(
        val fragmentClass: Class<out Fragment>,
        val arguments: Bundle? = null,
    ) : Route

    data class AlertDialogRoute(
        @StringRes val messageResId: Int,
        @StringRes val positiveButtonResId: Int,
    ) : Route
}
