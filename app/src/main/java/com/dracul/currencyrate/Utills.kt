package com.dracul.currencyrate

import java.math.RoundingMode
import java.text.DecimalFormat


fun Float.roundOffDecimal(): Float {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.FLOOR
    return df.format(this).toFloat()
}
