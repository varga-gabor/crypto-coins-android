package com.aldi.cryptocoins.network

import okhttp3.OkHttpClient

object NetworkTrafficLogger {

    fun init(httpClientBuilder: OkHttpClient.Builder) {
        // Do nothing in release builds
    }
}
