package com.aldi.cryptocoins.features.coinlist.navigation

import com.aldi.cryptocoins.architecture.navigation.api.Destination
import com.aldi.cryptocoins.architecture.navigation.api.Route
import com.aldi.cryptocoins.architecture.navigation.api.Route.ScreenRoute
import com.aldi.cryptocoins.architecture.navigation.api.RouteResolver
import com.aldi.cryptocoins.features.coinlist.CoinListFragment
import org.koin.core.annotation.Factory

@Factory
class CoinListRouteResolver : RouteResolver {

    override fun resolveRoute(destination: Destination): Route? =
        when (destination) {
            is CoinListDestination -> ScreenRoute(CoinListFragment::class.java)
            else -> null
        }
}
