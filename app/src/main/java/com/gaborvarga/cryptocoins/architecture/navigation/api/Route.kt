package com.aldi.cryptocoins.architecture.navigation.api

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

sealed interface Route {

    data class ScreenRoute(
        val fragmentClass: Class<out Fragment>,
    ) : Route

    data class AlertDialogRoute(
        @StringRes val messageResId: Int,
        @StringRes val positiveButtonResId: Int,
    ) : Route
}
