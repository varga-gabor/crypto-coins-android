package com.aldi.cryptocoins.features.coindetails.navigation

import android.os.Bundle
import com.aldi.cryptocoins.architecture.navigation.api.Destination
import com.aldi.cryptocoins.architecture.navigation.api.Route
import com.aldi.cryptocoins.architecture.navigation.api.Route.ScreenRoute
import com.aldi.cryptocoins.architecture.navigation.api.RouteResolver
import com.aldi.cryptocoins.features.coindetails.CoinDetailsFragment
import com.aldi.cryptocoins.features.coindetails.navigation.CoinDetailsDestination.Companion.EXTRA_COIN_ID
import org.koin.core.annotation.Factory

@Factory
class CoinDetailsRouteResolver : RouteResolver {

    override fun resolveRoute(destination: Destination): Route? =
        when (destination) {
            is CoinDetailsDestination -> {
                ScreenRoute(
                    fragmentClass = CoinDetailsFragment::class.java,
                    arguments = Bundle().apply {
                        putString(EXTRA_COIN_ID, destination.coinId)
                    }
                )
            }

            else -> null
        }
}
