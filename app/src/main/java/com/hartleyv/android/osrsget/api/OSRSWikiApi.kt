package com.hartleyv.android.osrsget.api

import retrofit2.http.GET
import retrofit2.http.Query

interface OSRSWikiApi {
    @GET("/api/v1/osrs/latest")
    suspend fun fetchInstantPrices(): String

    @GET("/api/v1/osrs/mapping")
    suspend fun fetchItemMapping(): String

    @GET("/api/v1/osrs/24h")
    suspend fun fetchDailyAverages(): String


    @GET("/api/v1/osrs/timeseries")
    suspend fun fetchTimeseries(@Query("timestep") param1: String, @Query("id") param2: String): String

}