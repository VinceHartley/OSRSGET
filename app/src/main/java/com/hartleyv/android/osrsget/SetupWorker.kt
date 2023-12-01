package com.hartleyv.android.osrsget

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters


private const val TAG = "SetupWorker"
class SetupWorker(private val context: Context,
                  workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        Log.e(TAG, "in setup worker")


        return try {
            val wikiRepo = WikiRepository()
            val itemMappings = wikiRepo.fetchItemMapping()
            ItemRepository.get().addItemMappings(itemMappings)

            val itemDailyAverages = wikiRepo.fetchDailyAverage()
            ItemRepository.get().addItemTimestamps(itemDailyAverages)

            val instantPrices = wikiRepo.fetchInstantPrices()
            ItemRepository.get().addInstantPrices(instantPrices)


            Log.e(TAG, "setup worker finished")
            Result.success()
        } catch(ex: Exception){
            Log.e(TAG, "setup worker exception", ex)
            Result.failure()
        }
    }

}
