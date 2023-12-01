package com.hartleyv.android.osrsget.api

import kotlinx.coroutines.delay
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class WikiInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()

        // required by the OSRS wiki so they can contact people if necessary
        val requestWithUserAgent = originalRequest.newBuilder()
            .header("User-Agent", "@spindrah - school project")
            .build()

        return chain.proceed(requestWithUserAgent)
    }

}