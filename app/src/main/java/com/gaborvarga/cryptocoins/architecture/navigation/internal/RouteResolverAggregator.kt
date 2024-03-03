package com.aldi.cryptocoins.architecture.navigation.internal

import com.aldi.cryptocoins.architecture.navigation.api.RouteResolver
import org.koin.core.annotation.Singleton

@Singleton
class RouteResolverAggregator(
    val routeResolvers: List<RouteResolver>,
)
