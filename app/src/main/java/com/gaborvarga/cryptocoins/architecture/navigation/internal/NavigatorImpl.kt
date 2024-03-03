package com.aldi.cryptocoins.architecture.navigation.internal

import androidx.activity.OnBackPressedCallback
import androidx.annotation.IdRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.aldi.cryptocoins.architecture.navigation.api.Destination
import com.aldi.cryptocoins.architecture.navigation.api.Navigator
import com.aldi.cryptocoins.architecture.navigation.api.Route.AlertDialogRoute
import com.aldi.cryptocoins.architecture.navigation.api.Route.ScreenRoute
import org.koin.core.annotation.Singleton

@Singleton
class NavigatorImpl(
    private val globalRouteResolver: GlobalRouteResolver,
) : Navigator {

    private var activityWrapper: ActivityWrapper? = null

    override fun bindToActivity(activity: AppCompatActivity, @IdRes fragmentContainerResId: Int) {
        activityWrapper = ActivityWrapper(activity, fragmentContainerResId)

        activity.lifecycle.addObserver(
            object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    activityWrapper = null
                    owner.lifecycle.removeObserver(this)
                }
            }
        )
        activity.onBackPressedDispatcher.addCallback(
            owner = activity,
            onBackPressedCallback = object : OnBackPressedCallback(enabled = true) {
                override fun handleOnBackPressed() {
                    navigateBack()
                }
            },
        )
    }

    override fun navigateTo(destination: Destination) {
        when (val route = globalRouteResolver.resolveRoute(destination)) {
            is ScreenRoute -> navigateToScreen(route)
            is AlertDialogRoute -> showAlertDialog(route)
        }
    }

    override fun navigateBack() {
        val activityWrapper = activityWrapper ?: throw NullPointerException(
            "ActivityWrapper is null. Could not navigate back!",
        )
        val backStackEntryCount = activityWrapper.activity.supportFragmentManager.backStackEntryCount
        if (backStackEntryCount > 1) {
            activityWrapper.activity.supportFragmentManager.popBackStack()
        } else {
            activityWrapper.activity.finish()
        }
    }

    private fun navigateToScreen(route: ScreenRoute) {
        val activityWrapper = activityWrapper ?: throw NullPointerException(
            "ActivityWrapper is null. Could not navigate to ${route.fragmentClass.name}!",
        )
        val fragment = activityWrapper.activity.supportFragmentManager
            .findFragmentByTag(route.fragmentClass.name)
        if (fragment == null || !fragment.isAdded) {
            createFragment(activityWrapper, route)
        }
    }

    private fun showAlertDialog(route: AlertDialogRoute) {
        val activityWrapper = activityWrapper ?: throw NullPointerException(
            "ActivityWrapper is null. Could not show AlertDialog!",
        )
        AlertDialog.Builder(activityWrapper.activity)
            .setMessage(route.messageResId)
            .setPositiveButton(route.positiveButtonResId) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun createFragment(activityWrapper: ActivityWrapper, route: ScreenRoute) {
        activityWrapper.activity.supportFragmentManager.commit {
            val fragmentFactory = activityWrapper.activity.supportFragmentManager.fragmentFactory
            val fragment = fragmentFactory.instantiate(
                activityWrapper.activity.classLoader,
                route.fragmentClass.name,
            )
            replace(activityWrapper.fragmentContainerResId, fragment, route.fragmentClass.name)
            addToBackStack(route.fragmentClass.name)
        }
    }
}
