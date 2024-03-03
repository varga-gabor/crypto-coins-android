package com.aldi.cryptocoins.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

object NetworkTrafficLogger {

    private const val BINARY_CHARACTER = "�" // Used to prevent files from being dumped into the logcat

    fun init(httpClientBuilder: OkHttpClient.Builder) {
        httpClientBuilder.addInterceptor(
            HttpLoggingInterceptor { message ->
                if (!message.contains(BINARY_CHARACTER)) {
                    Timber.v(message)
                }
            }.apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
    }
}
