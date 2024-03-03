package com.aldi.cryptocoins.architecture.navigation.api

interface RouteResolver {
    fun resolveRoute(destination: Destination): Route?
}
