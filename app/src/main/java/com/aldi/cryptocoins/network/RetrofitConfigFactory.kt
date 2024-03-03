package com.aldi.cryptocoins.network

import android.content.Context

object RetrofitConfigFactory {

    private const val COINCAP_BASE_URL = "https://api.coincap.io/v2/"

    fun create(context: Context): RetrofitConfig =
        RetrofitConfig(
            baseUrl = COINCAP_BASE_URL,
            responseCacheDir = context.cacheDir,
        )
}
