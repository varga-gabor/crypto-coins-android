package com.aldi.cryptocoins.di

import android.content.Context
import com.aldi.cryptocoins.network.MoshiFactory
import com.aldi.cryptocoins.network.RetrofitConfig
import com.aldi.cryptocoins.network.RetrofitConfigFactory
import com.aldi.cryptocoins.network.RetrofitFactory
import com.aldi.cryptocoins.store.datasource.AssetsApi
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton
import retrofit2.Retrofit

@Module
@ComponentScan("com.aldi.cryptocoins")
class AppModule {

    @OptIn(DelicateCoroutinesApi::class)
    @Singleton
    fun externalScope(): CoroutineScope = GlobalScope

    @Factory
    fun retrofitConfig(context: Context): RetrofitConfig =
        RetrofitConfigFactory.create(context)

    @Singleton
    fun moshi(): Moshi =
        MoshiFactory.create()

    @Singleton
    fun retrofit(moshi: Moshi, retrofitConfig: RetrofitConfig): Retrofit =
        RetrofitFactory.create(moshi, retrofitConfig)

    @Singleton
    fun assetsApi(retrofit: Retrofit): AssetsApi =
        retrofit.create(AssetsApi::class.java)
}
