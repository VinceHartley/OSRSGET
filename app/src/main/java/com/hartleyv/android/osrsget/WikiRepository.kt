package com.hartleyv.android.osrsget

import android.content.Context
import android.util.Log
import com.hartleyv.android.osrsget.api.OSRSWikiApi
import com.hartleyv.android.osrsget.api.WikiInterceptor
import com.hartleyv.android.osrsget.entities.InstantPrice
import com.hartleyv.android.osrsget.entities.ItemMapping
import com.hartleyv.android.osrsget.entities.ItemTimestamp
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create

private const val TAG = "Wiki repository"

class WikiRepository {
    private val osrsWikiApi: OSRSWikiApi

    init {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(WikiInterceptor())
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://prices.runescape.wiki/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(okHttpClient)
            .build()

        osrsWikiApi = retrofit.create()
    }

    suspend fun fetchDailyAverage(): MutableList<ItemTimestamp> {
        delay(1000)
        val response = osrsWikiApi.fetchDailyAverages()
        val jsonObject = JSONObject(response)
        val itemPrices = jsonObject.get("data") as JSONObject

        Log.d(TAG, "Fetching daily averages")


        val ret = mutableListOf<ItemTimestamp>()

        for (id in itemPrices.keys()) {
            val itemPrice = itemPrices.get(id) as JSONObject

            val item = ItemTimestamp(
                id.toInt(),
                "24h",
                jsonObject.get("timestamp") as? Int,
                itemPrice.get("avgHighPrice") as? Int,
                itemPrice.get("avgLowPrice") as? Int,
                itemPrice.get("highPriceVolume") as? Int,
                itemPrice.get("lowPriceVolume") as? Int
            )

//            Log.d(TAG, "$item")
            ret += item
        }


        return ret
    }

    suspend fun fetchInstantPrices(): MutableList<InstantPrice> {
        delay(1000)
        val response = osrsWikiApi.fetchInstantPrices()
        val jsonObject = JSONObject(response)
        val itemPrices: JSONObject = jsonObject.get("data") as JSONObject

        val ret = mutableListOf<InstantPrice>()

        Log.d(TAG, "Fetching instant prices")

        for (id in itemPrices.keys()) {
            val itemPrice = itemPrices.get(id) as JSONObject


            val item = InstantPrice(
                id.toInt(),
                itemPrice.get("high") as? Int,
                itemPrice.get("highTime") as? Int,
                itemPrice.get("low") as? Int,
                itemPrice.get("lowTime") as? Int
            )

//            Log.d(TAG, "$item")

            ret += item
        }

        return ret
    }

    suspend fun fetchItemMapping(): MutableList<ItemMapping> {
        delay(1000)
        val response = osrsWikiApi.fetchItemMapping()
        val jsonArray = JSONArray(response)
//
        val ret = mutableListOf<ItemMapping>()

        Log.d(TAG, "Fetching item mapping")

        for (i in 0 until jsonArray.length()) {
            var item = jsonArray.getJSONObject(i)

            val imap = ItemMapping(
                item.get("id") as Int,
                item.opt("members") as Boolean,
                item.opt("lowalch") as? Int,
                item.opt("limit") as? Int,
                item.opt("highalch") as? Int,
                item.opt("name") as String,
                item.opt("examine") as String,
                item.opt("icon") as String
            )


//            Log.d(TAG, "$imap")
            ret += imap
        }


        return ret
    }
    suspend fun fetchTimeseries(timestep: String, id: String): MutableList<ItemTimestamp> {
        delay(1000)
        // 5m timestep is hardcoded for now until i can get this working reliably

        val response = osrsWikiApi.fetchTimeseries("5m", id)
        val jsonObject = JSONObject(response)
        val itemPrices = jsonObject.get("data") as JSONArray

        val ret = mutableListOf<ItemTimestamp>()

        for (i in 0 until itemPrices.length())  {
            var itemPrice = itemPrices.get(0) as JSONObject

            val iprice = ItemTimestamp(
                id.toInt(),
                "5m",
                itemPrice.get("timestamp") as? Int,
                itemPrice.get("avgHighPrice") as? Int,
                itemPrice.get("avgLowPrice") as? Int,
                itemPrice.get("highPriceVolume") as? Int,
                itemPrice.get("lowPriceVolume") as? Int
            )

            ret += iprice
        }

        return ret

    }

    companion object {
        private var INSTANCE: WikiRepository? = null

        fun initalize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = WikiRepository()
            }
        }

        fun get(): WikiRepository {
            return INSTANCE ?:
            throw IllegalStateException("Wiki repo must be initalized")
        }


    }
}