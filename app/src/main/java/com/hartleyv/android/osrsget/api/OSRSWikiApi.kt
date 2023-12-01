package com.hartleyv.android.osrsget.api

import org.json.JSONObject
import retrofit2.http.GET

interface OSRSWikiApi {
    @GET("/api/v1/osrs/latest")
    suspend fun fetchInstantPrices(): String

    @GET("/api/v1/osrs/mapping")
    suspend fun fetchItemMapping(): String

    @GET("/api/v1/osrs/24h")
    suspend fun fetchDailyAverages(): String

}