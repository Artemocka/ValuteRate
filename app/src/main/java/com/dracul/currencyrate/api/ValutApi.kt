package com.dracul.currencyrate.api

import com.dracul.currencyrate.data.DailyData
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ValutApi {
    @GET("daily_json.js")
    suspend fun getData(): DailyData
}