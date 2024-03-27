package com.dracul.currencyrate.data

data class Valute(
    val ID: String,
    val NumCode: String,
    val CharCode: String,
    val Nominal: Int,
    val Name: String,
    val Value: Float,
    val Previous: Float
)