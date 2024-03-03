package com.aldi.cryptocoins.architecture.navigation.internal

import com.aldi.cryptocoins.architecture.navigation.api.Destination
import com.aldi.cryptocoins.architecture.navigation.api.Route
import org.koin.core.annotation.Factory

@Factory
class GlobalRouteResolver(
    private val routeResolverAggregator: RouteResolverAggregator,
) {

    fun resolveRoute(destination: Destination): Route =
        routeResolverAggregator.routeResolvers.firstNotNullOfOrNull { resolver ->
            resolver.resolveRoute(destination)
        } ?: throw IllegalArgumentException("Route resolver is not implemented for ':$destination'")
}
