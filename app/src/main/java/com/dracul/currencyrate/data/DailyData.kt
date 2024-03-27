package com.dracul.currencyrate.data

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class DailyData(
    val Date: String,
    val Timestamp: String,
    val Valute: HashMap<String, Valute>,
) {
    fun getListOfValutes(): List<Valute> {
        return Valute.map {
            it.value
        }.toList()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDate(): LocalDateTime? {

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
        val dateTime = LocalDateTime.parse(this.Date, formatter)
        return dateTime
    }
}
