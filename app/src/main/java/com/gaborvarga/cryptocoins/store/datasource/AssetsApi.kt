package com.aldi.cryptocoins.store.datasource

import com.aldi.cryptocoins.model.AssetsResponseDTO
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface AssetsApi {

    @GET("assets")
    suspend fun getAssets(
        @QueryMap queryMap: Map<String, String>,
    ): AssetsResponseDTO
}
