package com.aldi.cryptocoins.network

import com.squareup.moshi.Moshi

object MoshiFactory {

    fun create(): Moshi =
        Moshi.Builder()
            .build()
}
