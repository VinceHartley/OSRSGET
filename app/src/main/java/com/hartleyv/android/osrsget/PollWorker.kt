package com.hartleyv.android.osrsget

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

private const val TAG = "PollWorker"
class PollWorker(
    private val context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        Log.e(TAG, "in poll worker")

        return try {
            val wikiRepo = WikiRepository()
            val instantPrices = wikiRepo.fetchInstantPrices()
            val itemRepo = ItemRepository.get()

            itemRepo.addInstantPrices(instantPrices)

            Log.e(TAG, "instant prices updated")
            Result.success()
        } catch (ex: Exception) {
            Log.e(TAG, "background update failed", ex)
            Result.failure()
        }


    }
}