package com.dracul.currencyrate.api

import com.dracul.currencyrate.data.DailyData
import retrofit2.http.GET


interface ValuteApi {
    @GET("daily_json.js")
    suspend fun getData(): DailyData
}