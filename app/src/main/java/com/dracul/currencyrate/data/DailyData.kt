package com.dracul.currencyrate.data

data class DailyData(
    val Date:String,
    val Timestamp:String,
    val Valute:HashMap<String, Valute>,

)
