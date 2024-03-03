package com.aldi.cryptocoins.network

import java.io.File

data class RetrofitConfig(
    val baseUrl: String,
    val responseCacheDir: File,
)
