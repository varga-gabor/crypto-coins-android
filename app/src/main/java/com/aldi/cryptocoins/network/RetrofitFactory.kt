package com.aldi.cryptocoins.network

import com.squareup.moshi.Moshi
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitFactory {

    private const val RESPONSE_CACHE_SIZE = 4L * 1024L * 1024L

    fun create(moshi: Moshi, retrofitConfig: RetrofitConfig): Retrofit {
        val httpClientBuilder = OkHttpClient.Builder()
            .apply {
                cache(Cache(retrofitConfig.responseCacheDir, RESPONSE_CACHE_SIZE))
            }

        NetworkTrafficLogger.init(httpClientBuilder)

        return Retrofit.Builder()
            .baseUrl(retrofitConfig.baseUrl)
            .client(httpClientBuilder.build())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
}
